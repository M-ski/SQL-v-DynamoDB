package com.mski.spring.jv.demo.locks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LockWith {

    String name();

    int lockAtMostForSeconds() default 600;

    int lockAtLeastForSeconds() default 0;
}
