package ru.sbtqa.smartly.testsuites;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.sbtqa.smartly.common.BaseTest;
import ru.sbtqa.smartly.lifecycle.BeforeAfterExtension;
import ru.sbtqa.smartly.lifecycle.TestSuitesLifeCycle;

@ExtendWith(BeforeAfterExtension.class)
public class MailExampleTests extends BaseTest implements TestSuitesLifeCycle {

    @Test
    void mailSendingTest() {
        ctx.login("1","2");

    }

}

