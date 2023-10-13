package io.devnindo.datatype.schema.typeresolver;

import java.lang.annotation.*;
/**
 *  To define a type resolver should be used instead of default one.
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Resolver
{

}
