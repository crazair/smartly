package ru.sbtqa.smartly.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface PageObject {
    String value() default "";
}
