package ru.sbtqa.smartly.utils;

import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class PropertyUtils {

    public static Logger log = Logger.getRootLogger();
    private static final String APP_PROPERTIES_URL = "app.properties";
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(new FileReader(APP_PROPERTIES_URL));
        } catch (IOException e) {
            log.error("PropertyUtils static: ", e);
        }
    }

    public static String getProperty(String propertyKey) {
        log.info("PropertyUtils getProperty: " + propertyKey + " Value: " + properties.getProperty(propertyKey));
        return properties.getProperty(propertyKey);
    }

    public static String getProp(String propertyKey){
        return getProperty(propertyKey);
    }

    public static String prop(String propertyKey){
        return getProperty(propertyKey);
    }

}
