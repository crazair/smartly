package ru.sbtqa.smartly.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

public final class AllureUtils {

    public static Logger log = Logger.getRootLogger();
    private static final String PATH_ENV_PROP = "target/allure-results/";
    private static final String NAME_ENV_FILE = "environment.properties";

    private AllureUtils() throws IllegalAccessException {
        throw new IllegalAccessException("AllureUtils is utility class!");
    }

    public static void createEnvironmentProperties() {
        try {
            if(!Files.exists(Paths.get(PATH_ENV_PROP))){
                Files.createDirectories(Paths.get(PATH_ENV_PROP));
            }

            Properties props = new Properties();
            FileOutputStream fos = new FileOutputStream(PATH_ENV_PROP + NAME_ENV_FILE);

            Optional.ofNullable("Chrome").ifPresent(s -> props.setProperty("Browser", s));

            props.store(fos, "-- NO COMMENTS--");

            fos.close();
        } catch (IOException e) {
            log.error("AllureUtils createEnvironmentProperties IOException", e);
        }
    }
}
