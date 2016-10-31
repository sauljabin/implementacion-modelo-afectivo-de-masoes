/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.jade.agent;

import jade.core.Agent;
import masoes.jade.behaviour.SettingsBehaviour;

public class SettingsAgent extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new SettingsBehaviour());
    }

}
