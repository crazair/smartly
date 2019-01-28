package ru.sbtqa.smartly.common.lifecycle;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import ru.sbtqa.smartly.common.utils.SSHUtils;
import java.io.*;

import static ru.sbtqa.smartly.common.utils.PropertyUtils.prop;

public class BeforeAfterExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback  {

    public static final Logger LOG = Logger.getRootLogger();

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        LOG.info("###############################################################");
        LOG.info("Запуск теста: \"" + context.getDisplayName() + "\"");
        LOG.info("Класс и метод: " + context.getRequiredTestMethod());
        LOG.info("Теги: " + context.getTags());
        LOG.info("###############################################################");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
            takeScreenshot();
            if (Boolean.parseBoolean(prop("isLoadServerLogs"))) {
                takeServerLog();
            }
        }
        LOG.info("###############################################################");
        LOG.info("######################### КОНЕЦ ТЕСТА #########################");
        LOG.info("###############################################################");
    }

    @Attachment(type = "image/png", value = "Screenshot")
    public static byte[] takeScreenshot() {
        try {
            return Files.toByteArray(Screenshots.takeScreenShotAsFile());
        } catch (IOException e) {
            LOG.error("BeforeAfterExtension takeScreenshot IOException: ", e);
            return new byte[0];
        }
    }

    @Attachment(type = "plain/text", value = "server.log")
    public static byte[] takeServerLog(){
        try {
            return SSHUtils.getTailOfRemoteLog().getBytes();
        } catch (ExceptionInInitializerError e) {
            LOG.error("BeforeAfterExtension takeServerLog ExceptionInInitializerError: ", e);
        } catch (Exception e) {
            LOG.error("BeforeAfterExtension takeServerLog Exception: ", e);
        }
        return new byte[0];
    }

}
