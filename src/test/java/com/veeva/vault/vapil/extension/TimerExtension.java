package com.veeva.vault.vapil.extension;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.logging.Logger;


public class TimerExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final String BEGIN_TIME = "begin_time";
    private static final Logger logger = Logger.getLogger(TimerExtension.class.getName());

    private ExtensionContext.Store getStoreForMethodContext(ExtensionContext ctx) {
        return ctx.getStore(ExtensionContext.Namespace.create(getClass(), ctx.getRequiredTestMethod(), ctx.getDisplayName()));

    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        long endTime = System.currentTimeMillis();
        long duration = endTime - getStoreForMethodContext(extensionContext).remove(BEGIN_TIME,long.class);
        Method method = extensionContext.getRequiredTestMethod();
        logger.info( () -> String.format("Method [%s].{%s} took %s ms.",method.getName(), extensionContext.getDisplayName(), duration));
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        getStoreForMethodContext(extensionContext).put(BEGIN_TIME, System.currentTimeMillis());
    }
}
