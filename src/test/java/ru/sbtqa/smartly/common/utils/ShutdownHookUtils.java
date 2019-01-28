package ru.sbtqa.smartly.common.utils;

/** Утилитный класс для встраивания выполнения кода при Shutdown*/
public class ShutdownHookUtils {

    private static ShutdownHookUtils instance;

    private ShutdownHookUtils() {
    }

    /**
     * Метод для встраивания выполнения кода при Shutdown через Runnable
     * в данный момент использвется для выполнения кода 1 раз после всех
     * прогонов тестов и при условии параллельного запуска тестов
     */
    public static synchronized ShutdownHookUtils setHook(Runnable hook) {
        if (instance == null) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> hook.run()));
            instance = new ShutdownHookUtils();
        }
        return instance;
    }

}
