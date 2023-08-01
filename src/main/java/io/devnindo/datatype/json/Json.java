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
package io.devnindo.datatype.json;

import io.devnindo.datatype.json.jackson.JacksonCodec;

import java.nio.charset.StandardCharsets;

public interface Json {

    JacksonCodec CODEC = new JacksonCodec();

    public default byte[] toByteData() {
        return encode().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Encode this JSON object as a string.
     *
     * @return the string encoding.
     */
    public default String encode() {
        return Json.CODEC.encodeToString(this, false);
    }


    public Json copy();

    /**
     * Encode this JSON object a a string, with whitespace to make the object easier to read by a human, or other
     * sentient organism.
     *
     * @return the pretty string encoding.
     */
    public default String encodePrettily() {
        return Json.CODEC.encodeToString(this, true);
    }


}

