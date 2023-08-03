/*
 * Copyright 2023 devnindo
 *
 * Portions Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * The part of this software written by devnindo is licensed under the Apache License, Version 2.0 (the "License");
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
 *
 * The part of this software derived from the project by the Contributors to the Eclipse Foundation
 * is available under the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package io.devnindo.datatype.json;

import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.SchemaField;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.util.JsonUtil;
import io.devnindo.datatype.validation.Violation;


import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * A representation of a <a href="http://json.org/">JSON</a> object in Java.
 * To enable JSON to be used easily with business service implementation
 * The implementation adheres to the <a href="http://rfc-editor.org/rfc/rfc7493.txt">RFC-7493</a> to support Temporal
 * data types as well as binary data.
 */
public class JsonObject implements
        Json, Iterable<Map.Entry<String, Object>> {

    private Map<String, Object> map;

    /**
     * Create an instance from a string of JSON
     *
     * @param json the string of JSON
     */
    public JsonObject(String json) {
        if (json == null) {
            throw new NullPointerException();
        }
        fromString(json);
        if (map == null) {
            throw new DecodeException("Invalid JSON object: " + json);
        }
    }

    /**
     * Create a new, empty instance
     */
    public JsonObject() {
        map = new LinkedHashMap<>();
    }

    /**
     * Create an instance from a Map. The Map is not copied.
     *
     * @param map the map to create the instance from.
     */
    public JsonObject(Map<String, Object> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        this.map = map;
    }

    /**
     * Create an instance from a buffer.
     *
     * @param buf the buffer to create the instance from.
     */
  /*public JsonObject(Buffer buf) {
    if (buf == null) {
      throw new NullPointerException();
    }
    fromBuffer(buf);
    if (map == null) {
      throw new DecodeException("Invalid JSON object: " + buf);
    }
  }*/

    /**
     * Create an instance from a buffer.
     *
     * @param byteData the buffer to create the instance from.
     */
    public JsonObject(byte[] byteData) {
        if (byteData == null) {
            throw new NullPointerException();
        }
        fromString(new String(byteData, StandardCharsets.UTF_8));
    }

    /**
     * Get the string value with the specified key, special cases are addressed for extended JSON types {@code Instant},
     * {@code byte[]} and {@code Enum} which can be converted to String.
     *
     * @param key the key to return the value for
     * @return the value string representation or null if no value for that key
     */
    public String getString(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        if (val == null) {
            return null;
        }

        if (val instanceof Instant) {
            return ISO_INSTANT.format((Instant) val);
        } else if (val instanceof byte[]) {
            return JsonUtil.BASE64_ENCODER.encodeToString((byte[]) val);
        } else if (val instanceof Enum) {
            return ((Enum) val).name();
        } else {
            return val.toString();
        }
    }

    public <D extends DataBean> Either<Violation, D> toBeanEither(Class<D> beanType) {
        return BeanSchema.of(beanType).apply(this);
    }

    public <D extends DataBean> D toBean(Class<D> beanType) {

        return BeanSchema.of(beanType).apply(this).right();
    }

    /**
     * Get the Number value with the specified key
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException if the value is not a Number
     */
    public Number getNumber(String key) {
        Objects.requireNonNull(key);
        return (Number) map.get(key);
    }

    /**
     * Get the Integer value with the specified key
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException if the value is not an Integer
     */
    public Integer getInteger(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Integer) {
            return (Integer) number;  // Avoids unnecessary unbox/box
        } else {
            return number.intValue();
        }
    }

    /**
     * Get the Long value with the specified key
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException if the value is not a Long
     */
    public Long getLong(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Long) {
            return (Long) number;  // Avoids unnecessary unbox/box
        } else {
            return number.longValue();
        }
    }

    /**
     * Get the Double value with the specified key
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException if the value is not a Double
     */
    public Double getDouble(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Double) {
            return (Double) number;  // Avoids unnecessary unbox/box
        } else {
            return number.doubleValue();
        }
    }

    /**
     * Get the Float value with the specified key
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException if the value is not a Float
     */
    public Float getFloat(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Float) {
            return (Float) number;  // Avoids unnecessary unbox/box
        } else {
            return number.floatValue();
        }
    }

    /**
     * Get the Boolean value with the specified key
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException if the value is not a Boolean
     */
    public Boolean getBoolean(String key) {
        Objects.requireNonNull(key);
        return (Boolean) map.get(key);
    }

    /**
     * Get the JsonObject value with the specified key
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException if the value is not a JsonObject
     */
    public JsonObject getJsonObject(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        if (val instanceof Map) {
            val = new JsonObject((Map) val);
        }
        return (JsonObject) val;
    }

    /**
     * Get the JsonArray value with the specified key
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException if the value is not a JsonArray
     */
    public JsonArray getJsonArray(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        if (val instanceof List) {
            val = new JsonArray((List) val);
        }
        return (JsonArray) val;
    }

    public Jsonable getJsonable(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        return (Jsonable) val;
    }


    public <T extends DataBean> T getDataBean(String key, Class<T> beanClz) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(beanClz);
        Object val = map.get(key);
        if (val instanceof JsonObject)
            return JsonObject.class.cast(val).toBean(beanClz);
        else
            return beanClz.cast(val);
    }


    /**
     * Get the binary value with the specified key.
     * <p>
     * JSON itself has no notion of a binary, this extension complies to the RFC-7493, so this method assumes there is a
     * String value with the key and it contains a Base64 encoded binary, which it decodes if found and returns.
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException       if the value is not a String
     * @throws IllegalArgumentException if the String value is not a legal Base64 encoded value
     */
    public byte[] getBinary(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        // no-op
        if (val == null) {
            return null;
        }
        // no-op if value is already an byte[]
        if (val instanceof byte[]) {
            return (byte[]) val;
        }

        // assume that the value is in String format as per RFC
        String encoded = (String) val;
        // parse to proper type
        return JsonUtil.BASE64_DECODER.decode(encoded);
    }


    /**
     * Get the instant value with the specified key.
     * <p>
     * JSON itself has no notion of a temporal types, this extension allows ISO 8601 string formatted dates with timezone
     * always set to zero UTC offset, as denoted by the suffix "Z" to be parsed as a instant value.
     * {@code YYYY-MM-DDTHH:mm:ss.sssZ} is the default format used by web browser scripting. This extension complies to
     * the RFC-7493 with all the restrictions mentioned before. The method will then decode and return a instant value.
     *
     * @param key the key to return the value for
     * @return the value or null if no value for that key
     * @throws ClassCastException                      if the value is not a String
     * @throws java.time.format.DateTimeParseException if the String value is not a legal ISO 8601 encoded value
     */
    public Instant getInstant(String key) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        // no-op
        if (val == null) {
            return null;
        }
        // no-op if value is already an Instant
        if (val instanceof Instant) {
            return (Instant) val;
        }
        // assume that the value is in String format as per RFC
        String encoded = (String) val;
        // parse to proper type
        return Instant.from(ISO_INSTANT.parse(encoded));
    }

    /**
     * Get the value with the specified key, as an Object with types respecting the limitations of JSON.
     * <ul>
     *   <li>{@code Map} will be wrapped to {@code JsonObject}</li>
     *   <li>{@code List} will be wrapped to {@code JsonArray}</li>
     *   <li>{@code Instant} will be converted to {@code String}</li>
     *   <li>{@code byte[]} will be converted to {@code String}</li>
     *   <li>{@code Enum} will be converted to {@code String}</li>
     * </ul>
     *
     * @param key the key to lookup
     * @return the value
     */
    public Object getValue(String key) {
        Objects.requireNonNull(key);
        return JsonUtil.wrapJsonValue(map.get(key));
    }

    /**
     * Like {@link #getString(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public String getString(String key, String def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getString(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getNumber(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Number getNumber(String key, Number def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getNumber(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getInteger(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Integer getInteger(String key, Integer def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getInteger(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getLong(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Long getLong(String key, Long def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getLong(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getDouble(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Double getDouble(String key, Double def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getDouble(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getFloat(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Float getFloat(String key, Float def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getFloat(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getBoolean(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Boolean getBoolean(String key, Boolean def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getBoolean(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getJsonObject(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public JsonObject getJsonObject(String key, JsonObject def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getJsonObject(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getJsonArray(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public JsonArray getJsonArray(String key, JsonArray def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getJsonArray(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getBinary(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public byte[] getBinary(String key, byte[] def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getBinary(key);
        } else {
            return def;
        }
    }


    /**
     * Like {@link #getInstant(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Instant getInstant(String key, Instant def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getInstant(key);
        } else {
            return def;
        }
    }

    /**
     * Like {@link #getValue(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Object getValue(String key, Object def) {
        Objects.requireNonNull(key);
        if (map.containsKey(key)) {
            return getValue(key);
        } else {
            return def;
        }
    }

    /**
     * Does the JSON object contain the specified key?
     *
     * @param key the key
     * @return true if it contains the key, false if not.
     */
    public boolean containsKey(String key) {
        Objects.requireNonNull(key);
        return map.containsKey(key);
    }

    /**
     * Return the set of field names in the JSON objects
     *
     * @return the set of field names
     */
    public Set<String> fieldNames() {
        return map.keySet();
    }

    /**
     * Put a null value into the JSON object with the specified key.
     *
     * @param key the key
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject putNull(String key) {
        Objects.requireNonNull(key);
        map.put(key, null);
        return this;
    }

    /**
     * Put an Object into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Object value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    public JsonObject put(SchemaField field, Object val) {
        Objects.requireNonNull(field);
        map.put(field.name, val);
        return this;
    }

    /**
     * Put a {@link Jsonable} into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value an instance of {@link Jsonable}
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Jsonable value) {
        Objects.requireNonNull(key);
        map.put(key, value.toJson());
        return this;
    }

    /**
     * Put a Jsonable into the JSON object with the specified key.
     *
     * @param field any schema field
     * @param value an instance of {@link Jsonable}
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(SchemaField field, Jsonable value) {
        Objects.requireNonNull(field);
        map.put(field.name, value);
        return this;
    }

    /**
     * Put a {@link Jsonable} into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value an instance of {@link Jsonable}
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, DataBean value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    /**
     * Put a Jsonable into the JSON object with the specified key.
     *
     * @param field any schema field
     * @param value an instance of {@link Jsonable}
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(SchemaField field, DataBean value) {
        Objects.requireNonNull(field);
        map.put(field.name, value);
        return this;
    }

    /**
     * Remove an entry from this object.
     *
     * @param key the key
     * @return the value that was removed, or null if none
     */
    public Object remove(String key) {
        Objects.requireNonNull(key);
        return JsonUtil.wrapJsonValue(map.remove(key));
    }

    /**
     * Merge in another JSON object.
     * <p>
     * This is the equivalent of putting all the entries of the other JSON object into this object. This is not a deep
     * merge, entries containing (sub) JSON objects will be replaced entirely.
     *
     * @param other the other JSON object
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject mergeIn(JsonObject other) {
        return mergeIn(other, false);
    }

    /**
     * Merge in another JSON object.
     * A deep merge (recursive) matches (sub) JSON objects in the existing tree and replaces all
     * matching entries. JsonArrays are treated like any other entry, i.e. replaced entirely.
     *
     * @param other the other JSON object
     * @param deep  if true, a deep merge is performed
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject mergeIn(JsonObject other, boolean deep) {
        return mergeIn(other, deep ? Integer.MAX_VALUE : 1);
    }

    /**
     * Merge in another JSON object.
     * The merge is deep (recursive) to the specified level. If depth is 0, no merge is performed,
     * if depth is greater than the depth of one of the objects, a full deep merge is performed.
     *
     * @param other the other JSON object
     * @param depth depth of merge
     * @return a reference to this, so the API can be used fluently
     */
    @SuppressWarnings("unchecked")
    public JsonObject mergeIn(JsonObject other, int depth) {
        if (depth < 1) {
            return this;
        }
        if (depth == 1) {
            map.putAll(other.map);
            return this;
        }
        for (Map.Entry<String, Object> e : other.map.entrySet()) {
            if (e.getValue() == null) {
                map.put(e.getKey(), null);
            } else {
                map.merge(e.getKey(), e.getValue(), (oldVal, newVal) -> {
                    if (oldVal instanceof Map) {
                        oldVal = new JsonObject((Map) oldVal);
                    }
                    if (newVal instanceof Map) {
                        newVal = new JsonObject((Map) newVal);
                    }
                    if (oldVal instanceof JsonObject && newVal instanceof JsonObject) {
                        return ((JsonObject) oldVal).mergeIn((JsonObject) newVal, depth - 1);
                    }
                    return newVal;
                });
            }
        }
        return this;
    }


    /**
     * Deep copy of this JSON object.
     *
     * @return a copy where all elements have been copied recursively
     * @throws IllegalStateException when a nested element cannot be copied
     */
    //@Override
    public JsonObject copy() {
        return copy(JsonUtil.DEFAULT_CLONER);
    }

    /**
     * Deep copy of this JSON object.
     *
     * <p> Unlike {@link #copy()} that can fail when an unknown element cannot be copied, this method
     * delegates the copy of such element to the {@code cloner} function and will not fail.
     *
     * @param cloner a function that copies custom values not supported by the JSON implementation
     * @return a copy where all elements have been copied recursively
     */
    public JsonObject copy(Function<Object, ?> cloner) {
        Map<String, Object> copiedMap;
        if (map instanceof LinkedHashMap) {
            copiedMap = new LinkedHashMap<>(map.size());
        } else {
            copiedMap = new HashMap<>(map.size());
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object val = JsonUtil.deepCopy(entry.getValue(), cloner);
            copiedMap.put(entry.getKey(), val);
        }
        return new JsonObject(copiedMap);
    }

    /**
     * Get the underlying {@code Map} as is.
     * <p>
     * This map may contain values that are not the types returned by the {@code JsonObject} and
     * with an unpredictable representation of the value, e.g you might get a JSON object
     * as a {@link JsonObject} or as a {@link Map}.
     *
     * @return the underlying Map.
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * Get a Stream over the entries in the JSON object. The values in the stream will follow
     * the same rules as defined in {@link #getValue(String)}, respecting the JSON requirements.
     * <p>
     * To stream the raw values, use the storage object stream instead:
     * <pre>{@code
     *   jsonObject
     *     .getMap()
     *     .stream()
     * }</pre>
     *
     * @return a Stream
     */
    public Stream<Map.Entry<String, Object>> stream() {
        return JsonUtil.asStream(iterator());
    }

    /**
     * Get an Iterator of the entries in the JSON object.
     *
     * @return an Iterator of the entries
     */
    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return new Iter(map.entrySet().iterator());
    }

    /**
     * Get the number of entries in the JSON object
     *
     * @return the number of entries
     */
    public int size() {
        return map.size();
    }

    /**
     * Remove all the entries in this JSON object
     */
    public JsonObject clear() {
        map.clear();
        return this;
    }

    /**
     * Is this object entry?
     *
     * @return true if it has zero entries, false if not.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public String toString() {
        return encode();
    }

    @Override
    public boolean equals(Object o) {
        // null check
        if (o == null)
            return false;
        // self check
        if (this == o)
            return true;
        // type check and cast
        if (getClass() != o.getClass())
            return false;

        JsonObject other = (JsonObject) o;
        // size check
        if (this.size() != other.size())
            return false;
        // value comparison
        for (String key : map.keySet()) {
            if (!other.containsKey(key)) {
                return false;
            }

            Object thisValue = this.getValue(key);
            Object otherValue = other.getValue(key);
            // identity check
            if (thisValue == otherValue) {
                continue;
            }
            // special case for numbers
            if (thisValue instanceof Number && otherValue instanceof Number && thisValue.getClass() != otherValue.getClass()) {
                Number n1 = (Number) thisValue;
                Number n2 = (Number) otherValue;
                // floating point values
                if (thisValue instanceof Float || thisValue instanceof Double || otherValue instanceof Float || otherValue instanceof Double) {
                    // compare as floating point double
                    if (n1.doubleValue() == n2.doubleValue()) {
                        // same value check the next entry
                        continue;
                    }
                }
                if (thisValue instanceof Integer || thisValue instanceof Long || otherValue instanceof Integer || otherValue instanceof Long) {
                    // compare as integer long
                    if (n1.longValue() == n2.longValue()) {
                        // same value check the next entry
                        continue;
                    }
                }
            }
            // special case for char sequences
            if (thisValue instanceof CharSequence && otherValue instanceof CharSequence && thisValue.getClass() != otherValue.getClass()) {
                CharSequence s1 = (CharSequence) thisValue;
                CharSequence s2 = (CharSequence) otherValue;

                if (Objects.equals(s1.toString(), s2.toString())) {
                    // same value check the next entry
                    continue;
                }
            }
            // fallback to standard object equals checks
            if (!Objects.equals(thisValue, otherValue)) {
                return false;
            }
        }
        // all checks passed
        return true;
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }


    private void fromString(String json) {
        map = Json.CODEC.fromString(json, Map.class);
    }


    private static class Iter implements Iterator<Map.Entry<String, Object>> {

        final Iterator<Map.Entry<String, Object>> mapIter;

        Iter(Iterator<Map.Entry<String, Object>> mapIter) {
            this.mapIter = mapIter;
        }

        @Override
        public boolean hasNext() {
            return mapIter.hasNext();
        }

        @Override
        public Map.Entry<String, Object> next() {
            final Map.Entry<String, Object> entry = mapIter.next();
            final Object val = entry.getValue();
            // perform wrapping
            final Object wrapped = JsonUtil.wrapJsonValue(val);

            if (val != wrapped) {
                return new Entry(entry.getKey(), wrapped);
            }

            return entry;
        }

        @Override
        public void remove() {
            mapIter.remove();
        }
    }

    private static final class Entry implements Map.Entry<String, Object> {
        final String key;
        final Object value;

        public Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
