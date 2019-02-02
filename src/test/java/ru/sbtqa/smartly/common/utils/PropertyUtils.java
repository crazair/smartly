package ru.sbtqa.smartly.common.utils;

import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Утилитный класс для работы с файлом настроек в автотестах
 */
public final class PropertyUtils {

    /** ссылка на файл настроек приложения */
    private static final String APP_PROPERTIES_URL = "app.properties";
    /** ссылка на файл настроек log4j */
    private static final String LOG4J_PROPERTIES_URL = "target/test-classes/log4j.properties";
    /** кодировка файла настроек по-умолчанию */
    private static final Charset APP_PROPERTIES_CHARSET = Charset.forName("UTF-8");

    private static Properties properties = new Properties();
    public static final Logger LOG = Logger.getRootLogger();

    /** Загрузка файла настроек log4j.properties */
    static {
        PropertyConfigurator.configure(LOG4J_PROPERTIES_URL);
    }

    /** Загрузка файла при первом обращении к классу PropertyUtils */
    static {
        LOG.info("Загружаем файл настроек " + APP_PROPERTIES_URL + " с кодировкой " + APP_PROPERTIES_CHARSET);
        try (Reader reader = Files.newReader(new File(APP_PROPERTIES_URL), APP_PROPERTIES_CHARSET)) {
            properties.load(reader);
        } catch (IOException e) {
            LOG.error("PropertyUtils static: ", e);
        }
    }

    private PropertyUtils() throws IllegalAccessException {
        throw new IllegalAccessException("PropertyUtils is utility class!");
    }

    /** Метод возвращает значение настройки по ключу */
    public static String getProperty(String propertyKey) {
        LOG.info("PropertyUtils getProperty: " + propertyKey + " Value: " + properties.getProperty(propertyKey));
        return properties.getProperty(propertyKey);
    }

    /** Метод возвращает значение настройки по ключу */
    public static String prop(String propertyKey) {
        return getProperty(propertyKey);
    }

    /** Метод возвращает значение настройки по ключу. Со значением по-умолчанию! */
    public static String prop(String propertyKey, String defaultValue) {
        String value = getProperty(propertyKey);
        return value == null ? defaultValue : value;
    }

    /**
     * Метод возвращает значение типа Boolean настройки по ключу
     * Если getProperty == "true" то вернётся true
     * Если getProperty != "true" (в том числе null) то вернётся false
     **/
    public static Boolean getBooleanProperty(String propertyKey) {
        return Boolean.valueOf(getProperty(propertyKey));
    }

    /**
     * Метод установливает значение настройки по ключу
     *
     * @param propertyKey - ключ настройки
     * @param propertyValue - значение настройки
     */
    public static void setProperty(String propertyKey, String propertyValue) {
        LOG.info("PropertyUtils setProperty: " + propertyKey + " Value: " + propertyValue);
        properties.setProperty(propertyKey, propertyValue);
    }
}


