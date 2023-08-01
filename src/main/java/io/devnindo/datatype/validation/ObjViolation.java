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

package io.devnindo.datatype.validation;

import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.SchemaField;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.json.JsonObject;

public class ObjViolation<D extends DataBean> extends Violation<JsonObject> {


    public ObjViolation(String name$) {

        super(name$, new JsonObject());
    }


    public boolean hasRequirement() {
        return !ctxData.isEmpty();
    }


    public ObjViolation check(SchemaField<D, ?> f$, Either<Violation, ?> either$) {
        if (either$.isLeft())
            ctxData.put(f$.name, either$.left());

        return this;
    }


}

