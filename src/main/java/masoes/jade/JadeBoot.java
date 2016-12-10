/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import masoes.app.setting.Setting;

public class JadeBoot {

    public static final String GUI_PARAMETER = "gui";
    public static final String AGENTS_PARAMETER = "agents";
    public static final String PORT_PARAMETER = "port";
    public static final String JADE_MTP_PORT_PARAMETER = "jade_mtp_http_port";
    public static final String JADE_DF_AUTOCLEANUP = "jade_domain_df_autocleanup";

    private ProfileImpl jadeProfile;
    private Runtime jadeRuntime;

    public JadeBoot() {
        jadeProfile = new ProfileImpl();
        jadeRuntime = Runtime.instance();
    }

    public void boot(String agents) {
        jadeProfile.setParameter(GUI_PARAMETER, Setting.JADE_GUI.getValue());
        jadeProfile.setParameter(PORT_PARAMETER, Setting.JADE_PORT.getValue());
        jadeProfile.setParameter(JADE_MTP_PORT_PARAMETER, Setting.JADE_MTP_PORT.getValue());
        jadeProfile.setParameter(JADE_DF_AUTOCLEANUP, Setting.JADE_DF_AUTOCLEANUP.getValue());
        jadeProfile.setParameter(AGENTS_PARAMETER, agents);
        jadeRuntime.setCloseVM(true);
        jadeRuntime.createMainContainer(jadeProfile);
    }

}
