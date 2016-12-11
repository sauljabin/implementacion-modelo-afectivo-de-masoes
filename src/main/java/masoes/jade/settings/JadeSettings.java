/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.settings;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class JadeSettings {

    public static final String GUI = "gui";
    public static final String AGENTS = "agents";
    public static final String PORT = "port";
    public static final String JADE_MTP_HTTP_PORT = "jade_mtp_http_port";
    public static final String JADE_DOMAIN_DF_AUTOCLEANUP = "jade_domain_df_autocleanup";

    private static final String SETTINGS_PROPERTIES_FILE = "jade.properties";
    private static JadeSettings INSTANCE;
    private Properties properties;

    private JadeSettings() {
        properties = new Properties();
    }

    public synchronized static JadeSettings getInstance() {
        if (!Optional.ofNullable(INSTANCE).isPresent()) {
            INSTANCE = new JadeSettings();
        }
        return INSTANCE;
    }

    public synchronized void load() {
        try {
            properties.clear();
            properties.load(ClassLoader.getSystemResourceAsStream(SETTINGS_PROPERTIES_FILE));
        } catch (Exception e) {
            throw new JadeSettingsException(e.getMessage(), e);
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
