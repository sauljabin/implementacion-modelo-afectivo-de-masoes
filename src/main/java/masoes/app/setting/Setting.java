/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.setting;

import java.util.Map;

public enum Setting {

    APP_NAME(SettingsLoader.APP_NAME_KEY),
    APP_VERSION(SettingsLoader.APP_VERSION_KEY),
    APP_REVISION(SettingsLoader.APP_REVISION_KEY),
    OS_NAME(SettingsLoader.OS_NAME_KEY),
    OS_ARCH(SettingsLoader.OS_ARCH_KEY),
    OS_VERSION(SettingsLoader.OS_VERSION_KEY),
    JAVA_VERSION(SettingsLoader.JAVA_VERSION_KEY),
    JAVA_VENDOR(SettingsLoader.JAVA_VENDOR_KEY),
    MASOES_ENV(SettingsLoader.MASOES_ENV_KEY),
    JADE_REVISION(SettingsLoader.JADE_REVISION_KEY),
    JADE_VERSION(SettingsLoader.JADE_VERSION_KEY),
    JADE_PORT(SettingsLoader.JADE_PORT_KEY),
    JADE_GUI(SettingsLoader.JADE_GUI_KEY);

    private String key;

    Setting(String key) {
        this.key = key;
    }

    public static void set(String key, String value) {
        SettingsLoader.getInstance().setSetting(key, value);
    }

    public static String get(String key, String defaultValue) {
        return SettingsLoader.getInstance().getSetting(key, defaultValue);
    }

    public static String get(String key) {
        return SettingsLoader.getInstance().getSetting(key, "");
    }

    public static Map<String, String> toMap() {
        return SettingsLoader.getInstance().toMap();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return get(key);
    }

    public void setValue(String value) {
        set(key, value);
    }

    public String getValue(String defaultValue) {
        return get(key, defaultValue);
    }

    @Override
    public String toString() {
        return key;
    }

}
