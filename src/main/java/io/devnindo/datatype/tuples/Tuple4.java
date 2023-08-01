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

public final class Tuple4<V1, V2, V3, V4> {
    public final V1 first;
    public final V2 second;
    public final V3 third;
    public final V4 fourth;

    private Tuple4(V1 first$, V2 second$, V3 third$, V4 fourth$) {
        first = first$;
        second = second$;
        third = third$;
        fourth = fourth$;
    }

    public static final <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4>
    of(V1 first$, V2 second$, V3 third$, V4 fourth$) {
        return new Tuple4<>(first$, second$, third$, fourth$);
    }

    public static final <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4>
    of(Pair<V1, V2> pair$, V3 third$, V4 fourth$) {
        return new Tuple4<>(pair$.first, pair$.second, third$, fourth$);
    }

    public static final <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4>
    of(Pair<V1, V2> pair$, Pair<V3, V4> pair_2$) {
        return new Tuple4<>(pair$.first, pair$.second, pair_2$.first, pair_2$.second);
    }

    public static final <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4>
    of(Tuple3<V1, V2, V3> tuple3$, V4 fourth$) {
        return new Tuple4<>(tuple3$.first, tuple3$.second, tuple3$.third, fourth$);
    }


}

