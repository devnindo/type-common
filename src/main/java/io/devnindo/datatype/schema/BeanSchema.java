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
import io.devnindo.datatype.schema.typeresolver.TypeResolverFactory;
import io.devnindo.datatype.schema.typeresolver.TypeResolverIF;
import io.devnindo.datatype.util.ClzUtil;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BeanSchema<T extends DataBean> {

    private static Map<String, BeanSchema> SCHEMA_MAP;

    static {
        SCHEMA_MAP = new HashMap<>();
        List<Class<?>> schemaList = ClzUtil.findSubClzList(BeanSchema.class);
        schemaList.forEach(schema -> {
            //calculate beanName according to convention
            // NAMING: For a Bean, schema name is $Bean;
            // PACKAGE: Generated Schema has same package as the bean
            // omit prefix "$"
            String beanSimpleName = schema.getSimpleName().substring(1);
            String beanName = schema.getPackage().getName() + "." + beanSimpleName;
            try {
                BeanSchema instance = (BeanSchema) schema.getConstructor().newInstance();
                SCHEMA_MAP.put(beanName, instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                Logger.getLogger(BeanSchema.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static <D extends DataBean> BeanSchema<D> of(Class<D> modelClz$) {
        BeanSchema schema = SCHEMA_MAP.get(modelClz$.getName());
        if (schema == null)
            throw new IllegalStateException("No schema found for model: " + modelClz$.getName());

        return schema;
    }


    protected static final <T extends DataBean> ObjViolation newViolation(Class<T> beanClz) {

        return new ObjViolation("SCHEMA::" + beanClz.getSimpleName());
    }

    public static final <D extends DataBean, VAL> SchemaField<D, VAL>
    plainField(String name$, Function<D, VAL> getter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolverIF resolverIF = TypeResolverFactory.plain(typeClz$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }

    public static final <D extends DataBean, VAL> SchemaField<D, List<VAL>>
    plainListField(String name$, Function<D, List<VAL>> getter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolverIF resolverIF = TypeResolverFactory.plainDataList(typeClz$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }

    public static final <D extends DataBean, VAL extends DataBean> SchemaField<D, VAL>
    beanField(String name$, Function<D, VAL> getter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolverIF resolverIF = TypeResolverFactory.beanType(typeClz$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }

    public static final <D extends DataBean, VAL extends DataBean> SchemaField<D, List<VAL>>
    beanListField(String name$, Function<D, List<VAL>> getter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolverIF resolverIF = TypeResolverFactory.beanList(typeClz$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }

    public static final <D extends DataBean, VAL extends Enum<VAL>> SchemaField<D, VAL>
    enumField(String name$, Function<D, VAL> getter$, Class<VAL> typeClz$, boolean required$) {
        TypeResolverIF resolverIF = TypeResolverFactory.enumType(typeClz$);
        return new SchemaField<>(name$, getter$, resolverIF, required$);
    }

    public abstract JsonObject apply(T dataBean$);

    public abstract Either<Violation, T> apply(JsonObject reqObj);

    public abstract DataDiff<T> diff(T from$, T to$);


}

