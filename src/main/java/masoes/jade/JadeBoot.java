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

    private ProfileImpl iae;
    private Runtime jadeRuntime;

    public JadeBoot(ProfileImpl iae, Runtime jadeRuntime) {
        this.iae = iae;
        this.jadeRuntime = jadeRuntime;
    }

    public JadeBoot() {
        this(new ProfileImpl(), Runtime.instance());
    }

    public void boot(String args) {
        iae.setParameter("gui", "true");
        iae.setParameter("agents", args);
        iae.setParameter("port", Setting.JADE_PORT.getValue());
        jadeRuntime.setCloseVM(true);
        jadeRuntime.createMainContainer(iae);
    }
}
