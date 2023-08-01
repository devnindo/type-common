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
package io.devnindo.datatype.validation.validators;

import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Validator;
import io.devnindo.datatype.validation.violations.LogicalViolations;

public class ValueRules {

    public static final Validator REQUIRED = t -> {
        if (t == null)
            return Either.left(LogicalViolations.notNull());
        else return Either.right(t);
    };

    public static final <T> Validator<T, T> equal(T t$) {
        return (t) -> {
            if (t$.equals(t))
                return Either.right(t);
            else return Either.left(LogicalViolations.equalBound(t$));

        };
    }

    public static final <T extends Comparable> Validator<T, T> inSet(T... tVar$) {
        return (t) -> {
            for (T t0 : tVar$) {
                if (t0.equals(t))
                    return Either.right(t);
            }
            return Either.left(LogicalViolations.setBound(tVar$));
        };
    }

    public static final <T extends Comparable<T>> Validator<T, T> max(T t$) {
        return (t) -> {

            int comp = t$.compareTo(t);
            if (comp < 0)
                return Either.left(LogicalViolations.maxBound(t$));
            else return Either.right(t);

        };
    }

    public static final <T extends Comparable<T>> Validator<T, T> min(T t$) {
        return (t) -> {

            int comp = t$.compareTo(t);
            if (comp <= 0)
                return Either.right(t);
            else return Either.left(LogicalViolations.minBound(t$));

        };
    }

    public static final <T extends Comparable<T>> Validator<T, T> lessThan(T t$) {
        return (t) -> {

            int comp = t$.compareTo(t);
            if (comp <= 0)
                return Either.left(LogicalViolations.lessThanBound(t$));
            else return Either.right(t);

        };
    }

    public static final <T extends Comparable<T>> Validator<T, T> gtThan(T t$) {
        return (t) -> {

            int comp = t$.compareTo(t);
            if (comp < 0)
                return Either.right(t);
            else return Either.left(LogicalViolations.greaterThanBound(t$));

        };
    }

    public static final <T extends Comparable<T>> Validator<T, T> rangeOpen(T left$, T right$) {
        return t -> {
            if (t.compareTo(left$) >= 0 && t.compareTo(right$) <= 0)
                return Either.right(t);
            else return Either.left(LogicalViolations.openRangeBound(left$, right$));

        };
    }

    public static final <T extends Comparable<T>> Validator<T, T> rangeOpenClose(T left$, T right$) {
        return t -> {
            if (t.compareTo(left$) >= 0 && t.compareTo(right$) < 0)
                return Either.right(t);
            else return Either.left(LogicalViolations.openCloseRangeBound(t, left$, right$));

        };
    }

    public static final <T extends Comparable<T>> Validator<T, T> rangeCloseOpen(T left$, T right$) {
        return t -> {
            if (t.compareTo(left$) > 0 && t.compareTo(right$) <= 0)
                return Either.right(t);
            else return Either.left(LogicalViolations.closeOpenRangeBound(t, left$, right$));

        };
    }

    public static final <T extends Comparable<T>> Validator<T, T> rangeClose(T left$, T right$) {
        return t -> {
            if (t.compareTo(left$) > 0 && t.compareTo(right$) < 0)
                return Either.right(t);
            else return Either.left(LogicalViolations.closeRangeBound(t, left$, right$));

        };
    }

}
