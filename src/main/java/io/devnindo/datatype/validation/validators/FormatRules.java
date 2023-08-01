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
import io.devnindo.datatype.validation.Violation;

import java.util.regex.Pattern;

public class FormatRules {
    private static final Pattern emailPattern = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    public static final <T> Validator<T, String> asEmail() {
        return (t) -> {

            if (t instanceof String) {
                String email = (String) t;

                if (emailPattern.matcher(email).matches())
                    return Either.right(email);

                else return Either.left(Violation.of("EMAIL_FORMAT"));
            } else return Either.left(Violation.of("EMAIL_FORMAT"));

        };
    }
}

