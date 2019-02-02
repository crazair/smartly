package ru.sbtqa.smartly.common.utils;

import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import static ru.sbtqa.smartly.common.utils.PropertyUtils.prop;

/** Утилитный класс для работы с Allure FW */
public final class AllureUtils {

    public static final Logger LOG = Logger.getRootLogger();
    private static final String PATH_ENV_PROP = "target/allure-results/";
    private static final String NAME_ENV_FILE = PATH_ENV_PROP + "environment.properties";

    private AllureUtils() throws IllegalAccessException {
        throw new IllegalAccessException("AllureUtils is utility class!");
    }

    /**
     * Метод для создания environment.properties - настройки запуска тестов в отчёте Allure
     */
    public static void createEnvironmentProperties() {
        LOG.info("Start createEnvironmentProperties");
        try {
            if (!Paths.get(PATH_ENV_PROP).toFile().exists()) {
                Files.createDirectories(Paths.get(PATH_ENV_PROP));
            }
        } catch (IOException e) {
            LOG.error("AllureUtils createEnvironmentProperties IOException1", e);
        }

        try (FileOutputStream fos = new FileOutputStream(NAME_ENV_FILE)) {
            Properties props = new Properties();

            Optional.ofNullable(prop("url")).ifPresent(s -> props.setProperty("url", s));
            Optional.ofNullable(prop("login")).ifPresent(s -> props.setProperty("login", s));

            Optional.ofNullable(prop("webdriver.drivers.path")).ifPresent(s -> props.setProperty("driver", s));
            Optional.ofNullable(prop("selenide.browser")).ifPresent(s -> props.setProperty("browser", s));

            props.store(fos, "-- NO COMMENTS--");
        } catch (IOException e) {
            LOG.error("AllureUtils createEnvironmentProperties IOException2", e);
        }
    }
}

