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
package io.devnindo.datatype.schema.typeresolver.jsons;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.CommonDiff;
import io.devnindo.datatype.schema.DataDiff;
import io.devnindo.datatype.schema.typeresolver.TypeResolver;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.TypeViolations;

import java.util.function.Consumer;

public class JsonObjectResolver implements TypeResolver<JsonObject> {

    @Override
    public Either<Violation, JsonObject> evalJsonVal(Object val) {
        if (val instanceof JsonObject == false)
            return Either.left(TypeViolations.JSON_OBJ_TYPE);

        return Either.right((JsonObject) val);
    }

    @Override
    public Object toJsonVal(JsonObject jsObj) {
        return jsObj;
    }

    @Override
    public JsonObject diff(JsonObject from, JsonObject to, Consumer changeConsumer) {

        DataDiff<JsonObject> dataDiff = CommonDiff.jsonObjDiff(from, to);

        // this delta calculation will need to be changed
        if (dataDiff.delta == null || !dataDiff.delta.isEmpty()) {
            changeConsumer.accept(dataDiff.delta);
        }

        return dataDiff.merged;
    }
}

