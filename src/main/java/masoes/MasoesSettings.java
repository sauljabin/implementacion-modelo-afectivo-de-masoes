/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import settings.SettingsLoader;

public class MasoesSettings extends SettingsLoader {

    public static final String MASOES_ENV = "masoes.env";
    public static final String GUI_FPS = "gui.fps";
    public static final String BEHAVIOUR_IPS = "behaviour.ips";

    private static final String SETTINGS_PROPERTIES_FILE = "masoes.properties";
    private static MasoesSettings INSTANCE;

    private MasoesSettings() {
        load();
    }

    public synchronized static MasoesSettings getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MasoesSettings();
        }
        return INSTANCE;
    }

    public synchronized void load() {
        load(SETTINGS_PROPERTIES_FILE);
    }

}
