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
package io.devnindo.datatype.schema.typeresolver.lists;

import io.devnindo.datatype.json.JsonArray;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.typeresolver.TypeResolverIF;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.datatype.validation.violations.TypeViolations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BeanListResolver<T extends DataBean> implements TypeResolverIF<List<T>> {
    private final Violation listJsonObjViolation;
    // Integer, String, Double, JsonObject
    Class<T> beanType;

    public BeanListResolver(Class<T> dataType$) {
        beanType = dataType$;
        listJsonObjViolation = TypeViolations.plainDataList(JsonObject.class);
    }

    @Override
    public Either<Violation, List<T>> evalJsonVal(Object val) {
        if (val instanceof JsonArray == false)
            return Either.left(listJsonObjViolation);
        JsonArray array = (JsonArray) val;
        for (int idx = 0; idx < array.size(); idx++) {
            Object obj = array.getValue(idx);
            if (obj instanceof JsonObject == false)
                return Either.left(listJsonObjViolation);
        }

        List<T> beanList = new ArrayList<>();
        for (int idx = 0; idx < array.size(); idx++) {
            JsonObject js = array.getJsonObject(idx);
            Either<Violation, T> beanEither = js.toBeanEither(beanType);

            if (beanEither.isLeft())
                return Either.left(TypeViolations.validBeanItem(idx, beanEither.left()));

            beanList.add(beanEither.right());
        }

        return Either.right(beanList);
    }

    @Override
    public Object toJsonVal(List<T> dataList) {
        JsonArray array = new JsonArray();
        dataList.forEach(bean -> array.add(bean.toJson()));
        return dataList;
    }

    @Override
    public List<T> diff(List<T> from, List<T> to, Consumer changeConsumer) {
        if (from == null || allEqual(from, to))
            return to;

        changeConsumer.accept(to);
        return from;
    }

    private final boolean allEqual(List<T> from, List<T> to) {
        for (int idx = 0; idx < from.size(); idx++) {
            T fromData = from.get(idx);
            T toData = to.get(idx);
            if (!fromData.equals(toData))
                return false;
        }

        return true;
    }
}

