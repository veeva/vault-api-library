package com.veeva.vault.vapil.extension;

import java.io.File;

public class MdlHelper {

    static final String MDL_RECREATE_SCRIPT_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "mdl_scripts" + File.separator + "recreate_mdl_script.txt";
    static final String MDL_ALTER_SCRIPT_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "mdl_scripts" + File.separator + "alter_mdl_script.txt";
    static final String MDL_DROP_SCRIPT_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "mdl_scripts" + File.separator + "drop_mdl_script.txt";

    public static String getMdlRecreateScript() {
        return FileHelper.readTextFile(MDL_RECREATE_SCRIPT_PATH);
    }

    public static String getMdlAlterScript() {
        return FileHelper.readTextFile(MDL_ALTER_SCRIPT_PATH);
    }

    public static String getMdlDropScript() {
        return FileHelper.readTextFile(MDL_DROP_SCRIPT_PATH);
    }

}
