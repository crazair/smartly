package ru.sbtqa.smartly.utils;

import ru.sbtqa.smartly.pageobjects.base.BasePage;
import sun.reflect.Reflection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class PagesLoader {

    private static final String PAGES_OBJECT_PACKAGE = "ru.sbtqa.smartly";
    public Map<String, Class<? extends BasePage>> pagesMap = new HashMap<>();

    public PagesLoader() {
        //Reflection reflection = new Reflection("my.project.prefix");
    }

    public Class<? extends BasePage> getPageByName(String pageName) {
        return pagesMap.get(pageName);
    }

    public void addPage(String pageName, Class<? extends BasePage> clazz) {
        pagesMap.put(pageName, clazz);
    }


}
