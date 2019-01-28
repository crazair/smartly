package ru.sbtqa.smartly.common.utils;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/** Утилитный класс для временного хранения тестовых данных */
public class Storage {

    public static final Logger LOG = Logger.getRootLogger();
    private static final Map<String, Object> STASH = new HashMap<>();

    private Storage() {
        throw new IllegalAccessError("Storage is utility class");
    }

    /** Метод добавляет данные в хранилище по ключу */
    public static void putToStorage(String key, Object value) {
        LOG.info("Добавляем в хранилище объект с ключём: " + key + " Значением: " + value);
        STASH.put(key, value);
    }

    /** Метод возвращает данные из хранилища по ключу */
    public static <T> T getFromStorage(String key) {
        LOG.info("Получаем данные из хранилища по ключу: " + key + " Значение: " + STASH.get(key));
        return (T) STASH.get(key);
    }

    /** Метод удаляет данные в хранилище по ключу */
    public static <T> T removeFromStorage(String key) {
        return (T) STASH.remove(key);
    }

    /** Метод полностью очищает хранилище */
    public static void clearStorage() {
        STASH.clear();
    }
}


