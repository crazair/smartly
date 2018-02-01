package ru.sbtqa.smartly.lifecycle;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public interface TestSuitesLifeCycle {

    public static Logger log = Logger.getRootLogger();

    @BeforeAll
    default void beforeAllTests() {
        log.info("Before all tests");
    }

    @AfterAll
    default void afterAllTests() {
        log.info("After all tests");
    }

    @BeforeEach
    default void beforeEachTest(TestInfo testInfo) {
        log.info(String.format("About to execute [%s]",
                testInfo.getDisplayName()));
    }

    @AfterEach
    default void afterEachTest(TestInfo testInfo) {
        log.info(String.format("Finished executing [%s]",
                testInfo.getDisplayName()));
    }
}
