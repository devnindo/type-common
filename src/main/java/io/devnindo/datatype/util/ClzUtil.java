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
package io.devnindo.datatype.util;


import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.joor.Reflect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClzUtil {


    public static List<Class<?>> findSubClzList(Class<?> superClz$) {
        List<Class<?>> clzList;
        try (ScanResult scanResult = new ClassGraph().enableClassInfo().scan()) {
            ClassInfoList clzInfoList;
            if (superClz$.isInterface())
                clzInfoList = scanResult.getClassesImplementing(superClz$);
            else
                clzInfoList = scanResult.getSubclasses(superClz$);
            clzList = new ArrayList<>(clzInfoList.size());
            clzInfoList.forEach(clzInfo -> clzList.add(clzInfo.loadClass()));
//
        }
        return clzList;
    }

    public static List<Class<?>> findSubClzList(Class<?> superClz$, String tPackage$)
    //    throws IllegalAccessException
    {
        List<Class<?>> clzList;
        try (ScanResult scanResult = new ClassGraph().acceptPackages(tPackage$)
                .enableClassInfo().scan()) {

            ClassInfoList clzInfoList = scanResult.getSubclasses(superClz$);
            clzList = new ArrayList<>(clzInfoList.size());
            clzInfoList.forEach(clzInfo -> clzList.add(clzInfo.loadClass()));

        }
        return clzList;

    }

    public static <T> T findPackageClzAndReflect(Class<?> superClz$, String tPackge$, Object... initArgs$)
            throws IllegalAccessException {
        List<Class<?>> clzList = findSubClzList(superClz$, tPackge$);

        if (clzList.isEmpty())
            throw new IllegalAccessException("No Subclass found of class: " + superClz$ + " in package: " + tPackge$);

        if (clzList.size() > 1)
            throw new IllegalAccessException("More Than One Subclass found of class: " + superClz$ + " in package: " + tPackge$);

        T t = Reflect.onClass(clzList.get(0)).create(initArgs$).get();

        return t;
    }

    public static <T> T findClzAndReflect(Class<?> superClz$, Object... initArgs$)
            throws IllegalAccessException {
        List<Class<?>> clzList = findSubClzList(superClz$);

        if (clzList.isEmpty())
            throw new IllegalAccessException("No Subclass found of class: " + superClz$);

        if (clzList.size() > 1) {
            StringBuilder builder = new StringBuilder();
            builder.append("More Than One implementation found of class: ")
                    .append(superClz$).append("\n");
            clzList.forEach(clz -> builder.append("\t").append(clz.getName()).append("\n"));
            throw new IllegalAccessException(builder.toString());

        }

        T t = Reflect.onClass(clzList.get(0)).create(initArgs$).get();

        return t;
    }

    public static String throwableString(Throwable throwable) {
        StringWriter strWrt = new StringWriter();
        PrintWriter printWrt = new PrintWriter(strWrt);
        throwable.printStackTrace(printWrt);
        String excp = strWrt.toString();

        return excp;
    }

    public static boolean methodHasAnnotation(Method method, Class<? extends Annotation> annClz) {
        return method.getAnnotation(annClz) != null;

    }

}

