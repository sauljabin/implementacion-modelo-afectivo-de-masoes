/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

import settings.SettingsLoader;

public class JadeSettings extends SettingsLoader {

    public static final String GUI = "gui";
    public static final String AGENTS = "agents";
    public static final String PORT = "port";
    public static final String JADE_MTP_HTTP_PORT = "jade_mtp_http_port";
    public static final String JADE_DOMAIN_DF_AUTOCLEANUP = "jade_domain_df_autocleanup";
    public static final String PLATFORM_ID = "platform-id";

    private static final String SETTINGS_PROPERTIES_FILE = "jade.properties";
    private static JadeSettings INSTANCE;

    private JadeSettings() {
        load();
    }

    public synchronized static JadeSettings getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JadeSettings();
        }
        return INSTANCE;
    }

    public synchronized void load() {
        load(SETTINGS_PROPERTIES_FILE);
    }

}
