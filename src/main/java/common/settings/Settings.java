/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package common.settings;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class Settings {

    private Properties properties;

    public Settings() {
        properties = new Properties();
    }

    public synchronized void load(String path) {
        try {
            properties.clear();
            properties.load(ClassLoader.getSystemResourceAsStream(path));
        } catch (Exception e) {
            throw new SettingsException(e.getMessage(), e);
        }
    }

    public synchronized void set(String key, String value) {
        if (!Optional.ofNullable(value).isPresent()) {
            properties.remove(key);
        } else {
            properties.put(key, value);
        }
    }

    public synchronized String get(String key, String defaultValue) {
        String value = get(key);
        if (!Optional.ofNullable(value).isPresent()) {
            return defaultValue;
        }
        return value;
    }

    public synchronized String get(String key) {
        if (!Optional.ofNullable(key).isPresent()) {
            return null;
        }
        return properties.getProperty(key);
    }

    @Override
    public synchronized String toString() {
        return toMap().toString();
    }

    public synchronized Map<String, String> toMap() {
        return properties.stringPropertyNames()
                .stream()
                .sorted()
                .collect(Collectors.toMap(key -> key, key -> get(key)));
    }

}
