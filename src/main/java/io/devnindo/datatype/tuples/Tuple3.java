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
package io.devnindo.datatype.tuples;

public final class Tuple3<V1, V2, V3> {
    public final V1 first;
    public final V2 second;
    public final V3 third;

    private Tuple3(V1 first$, V2 second$, V3 third$) {
        first = first$;
        second = second$;
        third = third$;
    }

    public static final <V1, V2, V3> Tuple3<V1, V2, V3> of(V1 first$, V2 second$, V3 third$) {
        return new Tuple3<>(first$, second$, third$);
    }

    public static final <V1, V2, V3> Tuple3<V1, V2, V3> of(Pair<V1, V2> pair$, V3 third$) {
        return new Tuple3<>(pair$.first, pair$.second, third$);
    }
}

