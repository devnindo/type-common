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

package io.devnindo.datatype.validation;

import io.devnindo.datatype.util.Either;

public interface Validator<T, R> {

    public Either<Violation, R> apply(T value);


    public default <U> Validator<T, U> compose(Validator<? super R, U> after) {
        return (t) ->
        {
            Either<Violation, R> retEither = this.apply(t);
            if (retEither.isLeft())
                return Either.left(retEither.left());
            else return after.apply(retEither.right());

        };
    }

    public default Validator<T, R> or(Validator<T, R> after) {
        return (t) ->
        {
            Either<Violation, R> retEither = this.apply(t);
            if (retEither.isRight())
                return retEither;
            else return after.apply(t);

        };
    }

}

