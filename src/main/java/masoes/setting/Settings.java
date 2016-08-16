/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.setting;

public enum Settings {

    APP_NAME("app.name"),
    APP_VERSION("app.version"),
    APP_REVISION("app.revision"),
    OS_NAME("os.name"),
    OS_ARCH("os.arch"),
    OS_VERSION("os.version"),
    JAVA_VERSION("java.version"),
    JAVA_VENDOR("java.vendor");

    Settings(String key) {
        this.key = key;
    }

    private String key;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return SettingsLoader.getInstance().get(key, "");
    }
}
