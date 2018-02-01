package ru.sbtqa.smartly.common;

import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;

import static ru.sbtqa.smartly.utils.PropertyUtils.*;

public class ContextPageObject {

    public static Logger log = Logger.getRootLogger();
    public static ContextPageObject ctx = new ContextPageObject();

    public void open() {
        log.info("ContextPageObject open baseurl: " + prop("baseurl"));
        Selenide.open(prop("baseurl"));
    }

    public void quit() {
        log.info("ContextPageObject quit");
        Selenide.close();
    }

    public void login(String user, String password) {
        log.info("ContextPageObject login user=" + user + " password=" + password);
    }

}
