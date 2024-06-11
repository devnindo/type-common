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

import io.devnindo.datatype.json.Jsonable;
import io.devnindo.datatype.json.JsonObject;

import java.util.Objects;

/**
 *  A marker interface for generating schema of a Java Bean.
 *  The schemagen plugin finds all implementations of DataBean
 *  and generates Schema following class extension hierarchy.
 * */
public interface DataBean extends Jsonable {


    @Override
    public default JsonObject toJson() {
        BeanSchema  schema = BeanSchema.of(this.getClass().getName());
        return schema.apply(this);
    }

    /**
     * helps diff-and-merge two DataBean of same type T
     * @param from to put data from
     * @param to to put data into
     *
     * This diff-merge algorithm works as follows:
     * <ol>
     *     <li>create a merge object of type T and empty JsonObject to hold diff</li>
     *     <li>traverse field-by-field</li>
     *     <li>
     *           for a field f, if from.f not-null and from.f not-equals to.f
     *             then merge.f = from.f, diff.put(F.name, )
     *           else merge.f = to.f
     *     </li>
     * </ol>
     * */
    public static <T extends DataBean> DataDiff<T> diffMerge(T from,  T to)
    {
        Objects.requireNonNull(from);
        BeanSchema<T> schema = BeanSchema.of(from.getClass().getName());
        return schema.diff(from,  to);
    }

}

