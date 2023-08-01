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

public class DataDiff<T> {
    public final JsonObject delta;
    public final T merged;

    public DataDiff(JsonObject delta$, T merged$) {
        this.delta = delta$;
        this.merged = merged$;
    }


    public static DataDiff<JsonObject> jsonObjDiff(JsonObject from$, JsonObject to$) {
        JsonObject merged = new JsonObject();
        JsonObject delta = new JsonObject();

        if (from$ == null)
            return new DataDiff<>(delta, to$);

        if (to$ == null)
            return new DataDiff<>(null, from$);


        if (from$.isEmpty())
            return new DataDiff<>(to$, from$);


        from$.forEach(entry$ -> {

            String key = entry$.getKey();
            Object fromVal = entry$.getValue();
            Object toVal = to$.getValue(key);

            if (fromVal != null && !fromVal.equals(toVal)) {
                merged.put(key, fromVal);
                delta.put(key, toVal);
            } else {
                merged.put(key, toVal);
            }
        });

        return new DataDiff<JsonObject>(delta, merged);
    }
}

