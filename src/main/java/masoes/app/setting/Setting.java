/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.setting;

import java.util.Map;

public enum Setting {

    APP_NAME("app.name"),
    APP_VERSION("app.version"),
    APP_REVISION("app.revision"),
    OS_NAME("os.name"),
    OS_ARCH("os.arch"),
    OS_VERSION("os.version"),
    JAVA_VERSION("java.version"),
    JAVA_VENDOR("java.vendor"),
    MASOES_CASE("masoes.case");

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
        return SettingsLoader.getInstance().getSetting(key, "");
    }

    public void setValue(String value) {
        SettingsLoader.getInstance().setSetting(key, value);
    }

    public String getValue(String defaultValue) {
        return SettingsLoader.getInstance().getSetting(key, defaultValue);
    }

    @Override
    public String toString() {
        return key;
    }

}
