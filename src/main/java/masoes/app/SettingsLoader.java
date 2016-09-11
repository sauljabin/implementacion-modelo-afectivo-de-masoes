/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SettingsLoader {

    public static final String PROPERTIES_FILE = "settings.properties";
    private static SettingsLoader INSTANCE;
    private Properties properties;

    public synchronized static SettingsLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SettingsLoader();
        }
        return INSTANCE;
    }

    private SettingsLoader() {
        init();
    }

    public synchronized void set(String key, String value) {
        properties.put(key, value);
    }

    public synchronized String get(String key) {
        return properties.getProperty(key);
    }

    public synchronized String get(String key, String defaultValue) {
        String value = get(key);
        if (value == null)
            return defaultValue;
        return value;
    }

    public synchronized void init() {
        properties = new Properties();
        String[] initValues = {Settings.OS_NAME.getKey(), Settings.OS_ARCH.getKey(), Settings.OS_VERSION.getKey(), Settings.JAVA_VERSION.getKey(), Settings.JAVA_VENDOR.getKey()};
        for (String value : initValues) {
            set(value, System.getProperty(value));
        }
        load();
    }

    public synchronized Map<String, String> toMap() {
        Map<String, String> values = new HashMap<String, String>();
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            values.put(key, get(key));
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
