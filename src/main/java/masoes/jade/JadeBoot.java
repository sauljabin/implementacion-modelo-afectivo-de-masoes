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

    private ProfileImpl profile;
    private Runtime jadeRuntime;

    public JadeBoot(ProfileImpl profile, Runtime jadeRuntime) {
        this.profile = profile;
        this.jadeRuntime = jadeRuntime;
    }

    public JadeBoot() {
        this(new ProfileImpl(), Runtime.instance());
    }

    public void boot(String args) {
        profile.setParameter("gui", "true");
        profile.setParameter("agents", args);
        profile.setParameter("port", Setting.JADE_PORT.getValue());
        jadeRuntime.setCloseVM(true);
        jadeRuntime.createMainContainer(profile);
    }
}
