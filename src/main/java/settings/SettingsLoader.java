/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings;

import util.ToStringBuilder;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class SettingsLoader {

    private Properties properties;

    public SettingsLoader() {
        properties = new Properties();
    }

    public synchronized void load(String path) {
        try {
            properties.clear();
            properties.load(new InputStreamReader(ClassLoader.getSystemResourceAsStream(path), "UTF-8"));
        } catch (Exception e) {
            throw new SettingsException(e);
        }
    }

    public synchronized void set(String key, String value) {
        if (value == null) {
            properties.remove(key);
        } else {
            properties.put(key, value);
        }
    }

    public synchronized String get(String key, String defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public synchronized String get(String key) {
        if (key == null) {
            return null;
        }
        return properties.getProperty(key);
    }

    @Override
    public synchronized String toString() {
        return new ToStringBuilder()
                .append("settings", toMap())
                .toString();
    }

    public synchronized Map<String, String> toMap() {
        return properties.stringPropertyNames()
                .stream()
                .sorted()
                .collect(Collectors.toMap(key -> key, key -> get(key)));
    }

}
