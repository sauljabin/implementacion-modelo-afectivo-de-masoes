/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.settings;

import common.settings.Settings;

import java.util.Optional;

public class ApplicationSettings extends Settings {

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

    private ApplicationSettings() {
        load();
    }

    public synchronized static ApplicationSettings getInstance() {
        if (!Optional.ofNullable(INSTANCE).isPresent()) {
            INSTANCE = new ApplicationSettings();
        }
        return INSTANCE;
    }

    public synchronized void load() {
        load(SETTINGS_PROPERTIES_FILE);
        set(OS_NAME, System.getProperty(OS_NAME));
        set(OS_ARCH, System.getProperty(OS_ARCH));
        set(OS_VERSION, System.getProperty(OS_VERSION));
        set(JAVA_VERSION, System.getProperty(JAVA_VERSION));
        set(JAVA_VENDOR, System.getProperty(JAVA_VENDOR));
        set(JADE_VERSION, jade.core.Runtime.getVersion());
        set(JADE_REVISION, jade.core.Runtime.getRevision());
    }

}
