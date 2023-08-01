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
package io.devnindo.datatype.schema.typeresolver;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.DataDiff;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.TypeViolations;

import java.util.function.Consumer;

public class BeanResolver<D extends DataBean> implements TypeResolverIF<D> {
    private final Class<D> beanType;
    private final Violation beanViolation;

    public BeanResolver(Class<D> beanType$) {
        beanType = beanType$;
        beanViolation = TypeViolations.beanType(beanType);
    }

    @Override
    public Either<Violation, D> evalJsonVal(Object val) {
        if (val instanceof JsonObject == false)
            return Either.left(beanViolation);
        JsonObject obj = (JsonObject) val;

        return obj.toBeanEither(beanType);
    }

    @Override
    public Object toJsonVal(D d) {
        return d.toJson();
    }

    @Override
    public D diff(D from, D to, Consumer changeConsumer) {
        if (from == null)
            return to;
        if (to == null) {
            changeConsumer.accept(null);
            return from;
        }

        DataDiff<D> dataDiff = BeanSchema
                .of(beanType)
                .diff(from, to);

        if (!dataDiff.delta.isEmpty())
            changeConsumer.accept(dataDiff.delta);

        return dataDiff.merged;
    }
}

