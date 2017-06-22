/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator.agent;

import environment.dummy.DummyEmotionalAgent;
import jade.core.Agent;
import translate.Translation;

public enum AgentType {

    CONTRIBUTOR("gui.contributor", DummyEmotionalAgent.class);

    private String translationKey;
    private Class<? extends Agent> agentClass;

    AgentType(String translationKey, Class<? extends Agent> agentClass) {
        this.translationKey = translationKey;
        this.agentClass = agentClass;
    }

    public Class<? extends Agent> getAgentCLass() {
        return agentClass;
    }

    @Override
    public String toString() {
        return Translation.getInstance().get(translationKey);
    }

}
