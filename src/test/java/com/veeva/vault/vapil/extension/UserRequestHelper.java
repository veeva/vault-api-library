package com.veeva.vault.vapil.extension;

import java.io.File;

public class UserRequestHelper {

    static final String CREATE_USERS_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "users" + File.separator + "create_multiple_users.csv";
    static final String UPDATE_USERS_CSV_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "users" + File.separator + "update_multiple_users.csv";

    public static String getPathCreateMultipleUsers() {
        FileHelper.createCsvFile(CREATE_USERS_CSV_PATH);
        return CREATE_USERS_CSV_PATH;
    }

    public static String getPathUpdateMultipleUsers() {
        FileHelper.createCsvFile(UPDATE_USERS_CSV_PATH);
        return UPDATE_USERS_CSV_PATH;
    }

}
