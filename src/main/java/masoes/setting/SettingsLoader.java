/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.setting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SettingsLoader {

    public static final String PROPERTIES_FILE = "settings.properties";
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

    public synchronized void setSetting(String key, String value) {
        properties.put(key, value);
    }

    public synchronized String getSetting(String key) {
        return properties.getProperty(key);
    }

    public synchronized String getSetting(String key, String defaultValue) {
        String value = getSetting(key);
        if (value == null)
            return defaultValue;
        return value;
    }

    public synchronized void init() {
        properties = new Properties();
        String[] initValues = {Setting.OS_NAME.getKey(), Setting.OS_ARCH.getKey(), Setting.OS_VERSION.getKey(), Setting.JAVA_VERSION.getKey(), Setting.JAVA_VENDOR.getKey()};
        for (String value : initValues) {
            setSetting(value, System.getProperty(value));
        }
        load();
    }

    public synchronized Map<String, String> toMap() {
        Map<String, String> values = new HashMap<>();
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            values.put(key, getSetting(key));
        }
        return values;
    }

    public synchronized void load() {
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public void clear() {
        properties.clear();
    }

    public String toString() {
        return toMap().toString();
    }

}
