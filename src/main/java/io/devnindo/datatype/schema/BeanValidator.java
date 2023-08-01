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
package io.devnindo.datatype.schema;


import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Validator;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.LogicalViolations;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class BeanValidator<D extends DataBean>
        implements Validator<D, D> {


    public final String constraintName;
    public final Class<D> beanClz;
    private Map<SchemaField<D, ?>, Constraint> condition;

    protected BeanValidator(String constraintName$, Class<D> beanClz$) {
        constraintName = constraintName$;
        beanClz = beanClz$;
        condition = new HashMap<>();

    }

    public static final <D extends DataBean> BeanValidator<D>
    create(String constraintName$, Class<D> beanClz$, Consumer<BeanValidator<D>> config$) {
        BeanValidator<D> validator = new BeanValidator<>(constraintName$, beanClz$);
        config$.accept(validator);
        return validator;
    }


    @Override
    public Either<Violation, D> apply(D beanObj$) {

        ObjViolation violation = new ObjViolation(constraintName);
        condition.forEach((field, constraint) -> {

            Object data = field.accessor.apply(beanObj$);
            Either<Violation, Object> either = constraint.apply(data);
            violation.check(field, either);
        });

        if (violation.hasRequirement())
            return Either.left(violation);
        else return Either.right(beanObj$);


    }

    public Either<Violation, D> apply(JsonObject dataObj$) {


        Either<Violation, D> dataBeanEither = BeanSchema.of(beanClz).apply(dataObj$);

        if (dataBeanEither.isLeft())
            return Either.left(dataBeanEither.left());

        D dataBean = dataBeanEither.right();
        return apply(dataBean);

    }

    public <F> Constraint<F> required(SchemaField<D, F> field$) {
        Constraint<F> constraint = Constraint.required();
        condition.put(field$, constraint);
        return constraint;
    }

    public <F> Constraint<F> optional(SchemaField<D, F> field$) {
        Constraint<F> constraint = Constraint.optional();
        condition.put(field$, constraint);
        return constraint;
    }


    public static class Constraint<T> {
        final boolean required;
        private Validator validator;

        protected Constraint(boolean requred$) {
            this.required = requred$;
            validator = (v) -> Either.right(v);
        }

        public static final <T> Constraint<T> required() {
            return new Constraint(true);
        }

        public static final <T> Constraint<T> optional() {
            return new Constraint(false);
        }

        public Either<Violation, T> apply(T val) {
            if (val == null)
                if (required)
                    return Either.left(LogicalViolations.notNull());
                else return Either.right(null);
            else
                return validator.apply(val);
        }

        public Constraint<T> and(Validator<T, T> rule$) {
            validator = validator.compose(rule$);
            return this;
        }

        public Constraint<T> and(Validator<T, T>... ruleArr$) {
            for (Validator rule : ruleArr$)
                validator = validator.compose(rule);
            return this;
        }


    }

}
