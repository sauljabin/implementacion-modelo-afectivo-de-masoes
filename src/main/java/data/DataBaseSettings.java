/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import settings.SettingsLoader;

public class DataBaseSettings extends SettingsLoader {

    public static final String URL = "url";
    public static final String DRIVER = "driver";
    private static final String SETTINGS_PROPERTIES_FILE = "jdbc.properties";
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
