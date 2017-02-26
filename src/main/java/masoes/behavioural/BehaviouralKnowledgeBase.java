/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import knowledge.KnowledgeBase;
import util.ToStringBuilder;

public class BehaviouralKnowledgeBase extends KnowledgeBase {

    private static final String PATH_THEORY_BEHAVIOUR_MANAGER = "theories/behaviourManager.prolog";
    private static final String PATH_THEORY_EMOTIONAL_CONFIGURATOR = "theories/emotionalConfigurator.prolog";
    private String agentName;
    private String agentKnowledgePath;

    public BehaviouralKnowledgeBase(String agentName, String agentKnowledgePath) {
        this.agentName = agentName;
        this.agentKnowledgePath = agentKnowledgePath;
        addAgentKnowledge(agentName, agentKnowledgePath);
        addTheoryFromPath(PATH_THEORY_BEHAVIOUR_MANAGER);
        addTheoryFromPath(PATH_THEORY_EMOTIONAL_CONFIGURATOR);
    }

    private void addAgentKnowledge(String agentName, String agentKnowledgePath) {
        if (agentName != null) {
            addTheory(String.format("self(%s).", agentName));
            addTheory("other(X) :- not self(X).");
            if (agentKnowledgePath != null) {
                addTheoryFromPath(agentKnowledgePath);
            }
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("agentName", agentName)
                .append("agentKnowledgePath", agentKnowledgePath)
                .append("behaviourManagerKnowledgePath", PATH_THEORY_BEHAVIOUR_MANAGER)
                .append("emotionalConfiguratorKnowledgePath", PATH_THEORY_EMOTIONAL_CONFIGURATOR)
                .toString();
    }

}
