/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.agenttypedefinition;

import masoes.agent.EmotionalAgent;

public class EmotionalAgentClassWrapper implements Comparable {

    private Class<? extends EmotionalAgent> agentClass;

    public EmotionalAgentClassWrapper(Class<? extends EmotionalAgent> agentClass) {
        this.agentClass = agentClass;
    }

    public Class<? extends EmotionalAgent> getAgentClass() {
        return agentClass;
    }

    @Override
    public String toString() {
        return agentClass.getName();
    }

    @Override
    public int compareTo(Object o) {
        return agentClass.getName().compareTo(((EmotionalAgentClassWrapper) o).getAgentClass().getName());
    }

}
