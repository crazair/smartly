package ru.sbtqa.smartly.pageobjects.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface PageObject {
    String value();
}
