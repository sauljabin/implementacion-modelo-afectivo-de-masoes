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

    private ProfileImpl jadeProfile;
    private Runtime jadeRuntime;

    public JadeBoot(ProfileImpl jadeProfile, Runtime jadeRuntime) {
        this.jadeProfile = jadeProfile;
        this.jadeRuntime = jadeRuntime;
    }

    public JadeBoot() {
        this(new ProfileImpl(), Runtime.instance());
    }

    public void boot(String agents) {
        jadeProfile.setParameter(AGENTS_PARAMETER, agents);
        jadeProfile.setParameter(GUI_PARAMETER, Setting.JADE_GUI.getValue());
        jadeProfile.setParameter(PORT_PARAMETER, Setting.JADE_PORT.getValue());
        jadeRuntime.setCloseVM(true);
        jadeRuntime.createMainContainer(jadeProfile);
    }

}
