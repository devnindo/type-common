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

import java.util.function.Consumer;

public interface SimpleTypeResolverIF<T> extends TypeResolverIF<T> {

    // SchemaField will do null-safety check before propagation
    @Override
    default Object toJsonVal(T t) {
        return t;
    }

    ;

    @Override
    default T diff(T from, T to, Consumer changeConsumer) {
        if (from == null || from.equals(to))
            return to;

        changeConsumer.accept(to);
        return from;
    }

    ;
}

