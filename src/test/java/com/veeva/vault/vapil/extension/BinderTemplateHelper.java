package com.veeva.vault.vapil.extension;

import java.io.File;

public class BinderTemplateHelper {

    static final String CREATE_BINDER_TEMPLATES_JSON_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "binder_templates" + File.separator + "create_binder_templates.json";
    static final String UPDATE_BINDER_TEMPLATES_JSON_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "binder_templates" + File.separator + "update_binder_templates.json";

    public static String getJsonPathCreateMultipleBinderTemplates() {
        return CREATE_BINDER_TEMPLATES_JSON_PATH;
    }

    public static String getJsonPathUpdateMultipleBinderTemplates() {
        return UPDATE_BINDER_TEMPLATES_JSON_PATH;
    }

}
