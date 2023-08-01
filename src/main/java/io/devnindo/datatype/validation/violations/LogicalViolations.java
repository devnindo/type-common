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

import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.json.JsonObject;

import java.util.Arrays;


public final class LogicalViolations {

    public static Violation notNull() {
        return Violation.of("NOT_NULL");
    }

    public static <T> Violation setBound(T[] tArr$) {
        return Violation.withCtx("SET_BOUND", Arrays.asList(tArr$));
    }

    public static <T> Violation equalBound(T t$) {
        return Violation.withCtx("EQUAL_BOUND", t$);
    }

    public static <T extends Comparable<T>> Violation maxBound(T t$) {
        return Violation.withCtx("MAX_BOUND", t$);
    }

    public static <T extends Comparable<T>> Violation lessThanBound(T t$) {
        return Violation.withCtx("LESS_THAN_BOUND", t$);
    }

    public static <T extends Comparable<T>> Violation minBound(T t$) {
        return Violation.withCtx("MIN_BOUND", t$);

    }

    public static <T extends Comparable<T>> Violation greaterThanBound(T t$) {
        return Violation.withCtx("GREATER_THAN_BOUND", t$);

    }

    public static <T extends Comparable<T>> Violation openRangeBound(T left$, T right$) {
        JsonObject ctxData = new JsonObject().put("cardinal_left", left$).put("cardinal_right", right$);
        return Violation.withCtx("RANGE_OPEN", ctxData);

    }

    public static <T extends Comparable<T>> Violation openCloseRangeBound(T t, T left$, T right$) {
        JsonObject ctxData = new JsonObject().put("cardinal_left", left$).put("cardinal_right", right$);
        return Violation.withCtx("RANGE_OPEN_CLOSE", ctxData);

    }

    public static <T extends Comparable<T>> Violation closeOpenRangeBound(T t, T left$, T right$) {
        JsonObject ctxData = new JsonObject().put("cardinal_left", left$).put("cardinal_right", right$);
        return Violation.withCtx("RANGE_CLOSE_OPEN", ctxData);
    }

    public static <T extends Comparable<T>> Violation closeRangeBound(T t, T left$, T right$) {
        JsonObject ctxData = new JsonObject().put("cardinal_left", left$).put("cardinal_right", right$);
        return Violation.withCtx("RANGE_CLOSE", ctxData);

    }

}

