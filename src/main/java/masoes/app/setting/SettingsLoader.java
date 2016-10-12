/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.setting;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class SettingsLoader {

    public static final String SETTINGS_PROPERTIES_FILE = "settings.properties";
    public static final String OS_NAME_KEY = "os.name";
    public static final String OS_ARCH_KEY = "os.arch";
    public static final String OS_VERSION_KEY = "os.version";
    public static final String JAVA_VERSION_KEY = "java.version";
    public static final String JAVA_VENDOR_KEY = "java.vendor";
    public static final String JADE_VERSION_KEY = "jade.version";
    public static final String JADE_REVISION_KEY = "jade.revision";
    public static final String APP_NAME_KEY = "app.name";
    public static final String APP_REVISION_KEY = "app.revision";
    public static final String APP_VERSION_KEY = "app.version";
    public static final String MASOES_ENV_KEY = "masoes.env";

    private static SettingsLoader INSTANCE;
    private Properties properties;

    private SettingsLoader() {
    }

    public synchronized static SettingsLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SettingsLoader();
        }
        return INSTANCE;
    }

    public synchronized void load() {
        properties = new Properties();
        setSetting(OS_NAME_KEY, System.getProperty(OS_NAME_KEY));
        setSetting(OS_ARCH_KEY, System.getProperty(OS_ARCH_KEY));
        setSetting(OS_VERSION_KEY, System.getProperty(OS_VERSION_KEY));
        setSetting(JAVA_VERSION_KEY, System.getProperty(JAVA_VERSION_KEY));
        setSetting(JAVA_VENDOR_KEY, System.getProperty(JAVA_VENDOR_KEY));
        setSetting(JADE_VERSION_KEY, jade.core.Runtime.getVersion());
        setSetting(JADE_REVISION_KEY, jade.core.Runtime.getRevision());
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(SETTINGS_PROPERTIES_FILE));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public synchronized void setSetting(String key, String value) {
        if (value == null)
            properties.remove(key);
        else
            properties.put(key, value);
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

    public synchronized String toString() {
        return toMap().toString();
    }

    public synchronized Map<String, String> toMap() {
        return properties.stringPropertyNames()
                .stream()
                .sorted()
                .collect(Collectors.toMap(key -> key, key -> getSetting(key)));
    }

    public synchronized List<String> getKeys() {
        return properties.stringPropertyNames().stream().sorted().collect(Collectors.toList());
    }

}
