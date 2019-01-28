package ru.sbtqa.smartly.common;

import com.codeborne.selenide.WebDriverProvider;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

/**
 * Class for Selenoid
 */
public class SelenoidProvider implements WebDriverProvider {

    public static final Logger LOG = Logger.getRootLogger();
    public static final String SELENOID_HUB_URL = "http://test:test-password@moon.ibz.test-ose.ca.sbrf.ru/wd/hub";

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        String randomUUID = UUID.randomUUID().toString();
        desiredCapabilities.setBrowserName("chrome");
        desiredCapabilities.setVersion("65.0");

        desiredCapabilities.setCapability("screenResolution", "1920x1080x24");
        desiredCapabilities.setCapability("videoScreenSize", "1920x1080");
        desiredCapabilities.setCapability("enableVNC", false);
        desiredCapabilities.setCapability("enableVideo", false); // запись видео включать для отладки, не для прогонов большого кол-ва тестов
        desiredCapabilities.setCapability("videoName", "autotest" + randomUUID);
        desiredCapabilities.setCapability("name", "autotest" + randomUUID);
        desiredCapabilities.setCapability("timeZone", "Europe/Moscow");
        RemoteWebDriver driver = null;

        LOG.info("Connecting to the selenoid cluster...");
        try {
            driver = new RemoteWebDriver(URI.create(SELENOID_HUB_URL).toURL(), desiredCapabilities);
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } catch (MalformedURLException e) {
            LOG.error("Selenoid cluster connection error!", e);
        }
        return driver;
    }
}
