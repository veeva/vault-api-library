package com.veeva.vault.vapil.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TestRunHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestRunHelper.class);
    private final static String WINDOWS_HOME = "HOMEPATH";
    private final static String MAC_HOME = "HOME";
    private final static String VAPIL_PATH = "/.vapil";
    private final static String VAPIL_SETTINGS = "settings_vapil_testing.json";

    public static File getVapilTestHomePath() {
        String homeVar = System.getenv().containsKey(WINDOWS_HOME)?System.getenv(WINDOWS_HOME):System.getenv(MAC_HOME);
        File home = new File(homeVar);
            if (home.exists() && home.canWrite()) {
                File vapil = new File(home,VAPIL_PATH);
                if(vapil.exists() || (vapil.mkdir())) {
                    return vapil;
                }
            }
            return null;
    }

    public static File getVapilSettingsFile() {
        File vapilHome = getVapilTestHomePath();
        File settingsFile = null;
        if (vapilHome != null) {
            settingsFile = new File(vapilHome, VAPIL_SETTINGS);
        }
        return settingsFile;
    }

}
