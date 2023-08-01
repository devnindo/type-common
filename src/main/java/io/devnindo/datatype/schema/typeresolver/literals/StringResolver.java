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
package io.devnindo.datatype.schema.typeresolver.literals;

import io.devnindo.datatype.schema.typeresolver.SimpleTypeResolverIF;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.TypeViolations;

public class StringResolver implements SimpleTypeResolverIF<String> {
    @Override
    public Either<Violation, String> evalJsonVal(Object val) {
        if (val instanceof String == false)
            return Either.left(TypeViolations.STRING_TYPE);
        return Either.right((String) val);
    }
}

