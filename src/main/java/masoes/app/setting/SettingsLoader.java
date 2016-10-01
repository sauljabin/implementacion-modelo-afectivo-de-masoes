/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.setting;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SettingsLoader {

    public static final String PROPERTIES_FILE = "settings.properties";
    private static final Setting[] initSettings = {Setting.OS_NAME, Setting.OS_ARCH, Setting.OS_VERSION, Setting.JAVA_VERSION, Setting.JAVA_VENDOR};
    private static SettingsLoader INSTANCE;
    private Properties properties;

    private SettingsLoader() {
        init();
    }

    public synchronized static SettingsLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SettingsLoader();
        }
        return INSTANCE;
    }

    public synchronized void init() {
        properties = new Properties();
        Arrays.stream(initSettings)
                .forEach(setting -> setSetting(setting.getKey(), System.getProperty(setting.getKey())));
        load();
    }

    public synchronized void setSetting(String key, String value) {
        properties.put(key, value);
    }

    public synchronized void load() {
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public synchronized String getSetting(String key, String defaultValue) {
        String value = getSetting(key);
        if (value == null)
            return defaultValue;
        return value;
    }

    public synchronized String getSetting(String key) {
        return properties.getProperty(key);
    }

    public synchronized void clear() {
        properties.clear();
    }

    public synchronized String toString() {
        return toMap().toString();
    }

    public synchronized Map<String, String> toMap() {
        Map<String, String> values = new HashMap<>();
        properties.stringPropertyNames()
                .forEach(key -> values.put(key, getSetting(key)));
        return values;
    }

}
