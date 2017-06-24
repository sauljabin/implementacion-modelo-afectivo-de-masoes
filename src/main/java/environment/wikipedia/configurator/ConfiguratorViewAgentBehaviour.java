/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import behaviour.CounterBehaviour;
import masoes.MasoesSettings;

public class ConfiguratorViewAgentBehaviour extends CounterBehaviour {

    private ConfiguratorViewAgent agent;

    public ConfiguratorViewAgentBehaviour(ConfiguratorViewAgent agent, int maxCount) {
        super(agent, maxCount);
        this.agent = agent;
    }

    @Override
    public void count(int i) {
        agent.getConfiguratorView().getIterationLabel().setText(String.valueOf(i));
        sleep();
    }

    private void sleep() {
        myAgent.doWait(1000 / Long.parseLong(MasoesSettings.getInstance().get(MasoesSettings.BEHAVIOUR_IPS)));
    }

}
