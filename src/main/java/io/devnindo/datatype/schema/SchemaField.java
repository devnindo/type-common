/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.devnindo.datatype.schema;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.typeresolver.TypeResolver;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.LogicalViolations;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class SchemaField<D extends DataBean, VAL> {
    public final String name;
    public final Function<D, VAL> accessor;
    public final TypeResolver<VAL> typeResolver;
    private final boolean required;

    public SchemaField(String name, Function<D, VAL> accessor, TypeResolver<VAL> typeResolver, boolean required) {
        this.name = name;
        this.accessor = accessor;
        this.typeResolver = typeResolver;
        this.required = required;
    }

    public Either<Violation, VAL> fromJson(JsonObject jsObj) {
        Object val = jsObj.getValue(name);
        if (val == null) {
            if (required)
                return Either.left(LogicalViolations.notNull());
            else return Either.right(null);
        }

        return typeResolver.evalJsonVal(val);
    }

    public Object toJson(D dataBean) {
        VAL val = accessor.apply(dataBean);
        if (val == null)
            return null;
        return typeResolver.toJsonVal(val);
    }

    public VAL diff(D from, D to, BiConsumer<String, Object> changeBiConsumer) {
        VAL fromVal = accessor.apply(from);
        VAL toVal = accessor.apply(to);

        Consumer<VAL> changeConsumer = (val) -> changeBiConsumer.accept(name, val);
        return typeResolver.diff(fromVal, toVal, changeConsumer);
    }

}

