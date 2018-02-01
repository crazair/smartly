package ru.sbtqa.smartly.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest extends ContextPageObject {

    @BeforeAll
    public void before() {
        log.info("BaseTest before");
        System.setProperty("selenide.browser", "chrome");
        ctx.open();
    }

    @AfterAll
    public void after() {
        log.info("BaseTest after");
        ctx.quit();
    }

}
