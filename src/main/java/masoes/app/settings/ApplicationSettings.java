/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.settings;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class ApplicationSettings {

    public static final String OS_NAME = "os.name";
    public static final String OS_ARCH = "os.arch";
    public static final String OS_VERSION = "os.version";
    public static final String JAVA_VERSION = "java.version";
    public static final String JAVA_VENDOR = "java.vendor";
    public static final String JADE_VERSION = "jade.version";
    public static final String JADE_REVISION = "jade.revision";
    public static final String APP_NAME = "app.name";
    public static final String APP_REVISION = "app.revision";
    public static final String APP_VERSION = "app.version";
    public static final String MASOES_ENV = "masoes.env";

    private static final String SETTINGS_PROPERTIES_FILE = "application.properties";
    private static ApplicationSettings INSTANCE;
    private Properties properties;

    private ApplicationSettings() {
    }

    public synchronized static ApplicationSettings getInstance() {
        if (!Optional.ofNullable(INSTANCE).isPresent()) {
            INSTANCE = new ApplicationSettings();
        }
        return INSTANCE;
    }

    public synchronized void load() {
        properties = new Properties();
        set(OS_NAME, System.getProperty(OS_NAME));
        set(OS_ARCH, System.getProperty(OS_ARCH));
        set(OS_VERSION, System.getProperty(OS_VERSION));
        set(JAVA_VERSION, System.getProperty(JAVA_VERSION));
        set(JAVA_VENDOR, System.getProperty(JAVA_VENDOR));
        set(JADE_VERSION, jade.core.Runtime.getVersion());
        set(JADE_REVISION, jade.core.Runtime.getRevision());
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(SETTINGS_PROPERTIES_FILE));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public synchronized void set(String key, String value) {
        validatePropertiesLoad();
        if (!Optional.ofNullable(value).isPresent()) {
            properties.remove(key);
        } else {
            properties.put(key, value);
        }
    }

    public synchronized String get(String key, String defaultValue) {
        validatePropertiesLoad();
        String value = get(key);
        if (!Optional.ofNullable(value).isPresent()) {
            return defaultValue;
        }
        return value;
    }

    public synchronized String get(String key) {
        validatePropertiesLoad();
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
        validatePropertiesLoad();
        return properties.stringPropertyNames()
                .stream()
                .sorted()
                .collect(Collectors.toMap(key -> key, key -> get(key)));
    }

    private void validatePropertiesLoad() {
        if (!Optional.ofNullable(properties).isPresent()) {
            throw new ApplicationSettingsException("Application settings not loaded, first invokes load()");
        }
    }

}
