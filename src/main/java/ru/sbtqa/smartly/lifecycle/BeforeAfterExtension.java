package ru.sbtqa.smartly.lifecycle;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BeforeAfterExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    public static Logger log = Logger.getRootLogger();

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if(context.getExecutionException().isPresent()){
            log.warn("Test Execution!!!");
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {

    }
}
