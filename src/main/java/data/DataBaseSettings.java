/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import settings.SettingsLoader;

public class DataBaseSettings extends SettingsLoader {

    private static final String SETTINGS_PROPERTIES_FILE = "jdbc.properties";
    public static final String URL = "url";
    public static final String DRIVER = "driver";
    private static DataBaseSettings INSTANCE;

    private DataBaseSettings() {
        load();
    }

    public synchronized static DataBaseSettings getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataBaseSettings();
        }
        return INSTANCE;
    }

    public synchronized void load() {
        load(SETTINGS_PROPERTIES_FILE);
    }

}
