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

import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.typeresolver.jsons.JsonArrayResolver;
import io.devnindo.datatype.schema.typeresolver.jsons.JsonObjectResolver;
import io.devnindo.datatype.schema.typeresolver.lists.BeanListResolver;
import io.devnindo.datatype.schema.typeresolver.lists.DataListResolver;
import io.devnindo.datatype.schema.typeresolver.literals.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TypeResolverFactory {
    private static final Map<String, TypeResolverIF> plainMap;
    private static final Map<String, TypeResolverIF> plainListMap;

    private static final Map<String, TypeResolverIF> enumMap;
    private static final Map<String, TypeResolverIF> beanMap;
    private static final Map<String, TypeResolverIF> beanListMap;

    static {
        plainMap = new HashMap<>();
        enumMap = new HashMap<>();
        beanMap = new HashMap<>();
        plainListMap = new HashMap<>();
        beanListMap = new HashMap<>();

        plainMap.put(Integer.class.getName(), new IntegerResolver());
        plainMap.put(Long.class.getName(), new LongResolver());
        plainMap.put(String.class.getName(), new StringResolver());
        plainMap.put(Double.class.getName(), new DoubleResolver());
        plainMap.put(Boolean.class.getName(), new BooleanResolver());
        plainMap.put(Instant.class.getName(), new InstantUTCResolver());
        plainMap.put(JsonObject.class.getName(), new JsonObjectResolver());
        plainMap.put(JsonArray.class.getName(), new JsonArrayResolver());

        plainListMap.put(Integer.class.getName(), new DataListResolver(Integer.class));
        plainListMap.put(String.class.getName(), new DataListResolver(String.class));
        plainListMap.put(JsonObject.class.getName(), new DataListResolver(JsonObject.class));


    }

    public static final <T> TypeResolverIF<T> plain(Class<T> typeClz$) {
        return plainMap.get(typeClz$.getName());
    }


    public static final <T> TypeResolverIF<List<T>> plainDataList(Class<T> typeClz$) {
        return plainListMap.get(typeClz$.getName());
    }

    public static final <T extends Enum<T>> TypeResolverIF<T> enumType(Class<T> enumType$) {
        TypeResolverIF<T> resolver = enumMap.get(enumType$.getName());
        if (resolver == null) {
            resolver = new EnumResolver<>(enumType$);
            enumMap.put(enumType$.getName(), resolver);
        }
        return resolver;
    }

    public static final <T extends DataBean> TypeResolverIF<T> beanType(Class<T> beanType) {
        TypeResolverIF<T> resolver = beanMap.get(beanType.getName());
        if (resolver == null) {
            resolver = new BeanResolver<>(beanType);
            beanMap.put(beanType.getName(), resolver);
        }
        return resolver;
    }


    public static final <T extends DataBean> TypeResolverIF<List<T>> beanList(Class<T> beanType) {
        TypeResolverIF<List<T>> resolver = beanListMap.get(beanType.getName());
        if (resolver == null) {
            resolver = new BeanListResolver<>(beanType);
            beanMap.put(beanType.getName(), resolver);
        }
        return resolver;
    }


}

