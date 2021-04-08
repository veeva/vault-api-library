package com.veeva.vault.vapil.extension;

import java.io.File;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class TestRunHelper {

    private final static String USER_HOME = "user.home";
    private final static String MAC_HOME = "HOME";
    private final static String VAPIL_PATH = "/.vapil";
    private final static String TEST_SESSION = "vapil.test.session";

    private static final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

    public static String getJVMId() {
        return runtime.getName();
    }

    public static String getFilesafeTestRunID() {
        return getJVMId()
                .replace('@','_')
                .replace('.','_');
    }

    public static File getVapilTestHomePath() {
        String homeVar = System.getenv().containsKey(USER_HOME)?System.getenv(USER_HOME):System.getenv(MAC_HOME);
        File home = new File(homeVar);
            if (home.exists() && home.canWrite()) {
                File vapil = new File(home,VAPIL_PATH);
                if(vapil.exists() || (vapil.mkdir())) {
                    return vapil;
                }
            }
            return null;
    }

    public static File getVapilSessionFile() {
        final File vapilHome = getVapilTestHomePath();
        if (vapilHome != null) {
            return new File(vapilHome,TEST_SESSION);
        }
        return null;
    }

}
