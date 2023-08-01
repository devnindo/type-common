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
package io.devnindo.datatype.validation.violations;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.validation.Violation;

public class TypeViolations {
    private static final String TYPE_CONSTRAINT = "DATA_TYPE";
    public static final Violation STRING_TYPE = Violation.withCtx(TYPE_CONSTRAINT, "String");
    public static final Violation INTEGER_TYPE = Violation.withCtx(TYPE_CONSTRAINT, "Integer");
    public static final Violation LONG_TYPE = Violation.withCtx(TYPE_CONSTRAINT, "LONG_STRING");
    public static final Violation BOOLEAN_TYPE = Violation.withCtx(TYPE_CONSTRAINT, "Boolean");
    public static final Violation DOUBLE_TYPE = Violation.withCtx(TYPE_CONSTRAINT, "Double");
    public static final Violation INSTANT_UTC_TYPE = Violation.withCtx(TYPE_CONSTRAINT, "Instant UTC[YYYY-MM-DDTHH:MM:ss.SSSZ]");
    public static final Violation JSON_OBJ_TYPE = Violation.withCtx(TYPE_CONSTRAINT, "JsonObject");
    public static final Violation JSON_ARR_TYPE = Violation.withCtx(TYPE_CONSTRAINT, "JsonArray");

    public static final Violation enumVal(Class<? extends Enum> enumType) {
        Enum[] constArr = enumType.getEnumConstants();
        String[] nameArr = new String[constArr.length];
        for (int idx = 0; idx < constArr.length; idx++) {
            nameArr[idx] = constArr[idx].name();
        }
        String domain = "{ " + String.join(", ", nameArr) + " }";
        return Violation.withCtx("ENUM_TYPE", enumType.getSimpleName() + domain);
    }

    // plain type => JsonObject, Integer, String
    public static final Violation plainDataList(Class<?> plainType) {
        return Violation.withCtx("PLAIN_DATA_LIST_TYPE", plainType.getSimpleName());
    }

    public static final Violation validBeanItem(int idx, Violation violation) {

        JsonObject data = new JsonObject();
        data.put("idx-" + idx, violation);
        return Violation.withCtx("BEAN_LIST_TYPE", data);
    }

    public static final Violation beanType(Class<?> plainType) {
        return Violation.withCtx("BEAN_TYPE", plainType.getSimpleName());
    }

}

