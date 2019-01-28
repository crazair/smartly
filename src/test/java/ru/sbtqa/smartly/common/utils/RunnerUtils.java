package ru.sbtqa.smartly.common.utils;

import ru.sbtqa.smartly.pageobjects.base.CommonPage;
import static com.codeborne.selenide.Selenide.close;
import static com.codeborne.selenide.Selenide.open;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.getBooleanProperty;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.prop;
import static ru.sbtqa.smartly.pageobjects.base.CommonPage.login;

/** Утилитный класс для запуска функциональности перед/после тестов */
public class RunnerUtils {

    private static final String INIT_DICT_PROP_NAME = "initDictionaries";

    private RunnerUtils() throws IllegalAccessException {
        throw new IllegalAccessException("RunnerUtils is utility class!");
    }

    /** Запуск инициализации справочников по-необходимости */
    public static void initDicts() {
        if (getBooleanProperty(INIT_DICT_PROP_NAME)) {
            authRunner(() -> new CommonPage().initDictionaries());
        }
    }

    /** Метод для запуска кода в авторизованной среде:-) */
    private static void authRunner(Runnable runnable) {
        open(prop("url"));
        login();
        runnable.run();
        close();
    }
}
