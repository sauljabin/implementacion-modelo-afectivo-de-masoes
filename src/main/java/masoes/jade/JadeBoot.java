/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import masoes.jade.settings.JadeSettings;

public class JadeBoot {

    private JadeSettings jadeSettings;
    private ProfileImpl jadeProfile;
    private Runtime jadeRuntime;

    public JadeBoot() {
        jadeProfile = new ProfileImpl();
        jadeRuntime = Runtime.instance();
        jadeSettings = JadeSettings.getInstance();
    }

    public void boot() {
        jadeSettings.toMap().forEach((key, value) -> jadeProfile.setParameter(key, value));
        jadeRuntime.setCloseVM(true);
        jadeRuntime.startUp(jadeProfile);
    }

}
