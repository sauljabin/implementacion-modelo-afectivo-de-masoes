/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.test.setting;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import masoes.jade.setting.SettingsBehaviour;
import test.common.Test;
import test.common.TestException;

public class SettingsBehaviourTest extends Test {

    @Override
    public Behaviour load(Agent agent) throws TestException {

        return new SettingsBehaviour(){
            @Override
            public void action() {
                passed("holaaa");
            }
        };
    }

}
