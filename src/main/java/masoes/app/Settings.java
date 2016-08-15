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

public class Settings {

    public static final String APP_NAME_KEY = "app.name";
    private static Settings INSTANCE;
    private Properties properties;

    public synchronized static Settings getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }

    private Settings() {
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
        String[] initValues = {"os.name", "os.arch", "os.version", "java.version", "java.vendor"};
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
            properties.load(ClassLoader.getSystemResourceAsStream("settings.properties"));
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
