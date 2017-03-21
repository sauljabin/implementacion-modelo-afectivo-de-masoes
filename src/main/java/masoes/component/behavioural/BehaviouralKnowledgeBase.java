/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import knowledge.KnowledgeBase;
import util.ToStringBuilder;

import java.nio.file.Paths;

public class BehaviouralKnowledgeBase extends KnowledgeBase {

    private static final String PATH_THEORY_BEHAVIOUR_MANAGER = "theories/behavioural/behaviourManager.prolog";

    public BehaviouralKnowledgeBase(String agentName) {
        addAgentKnowledge(agentName);
        addTheory(Paths.get(PATH_THEORY_BEHAVIOUR_MANAGER));
    }

    private void addAgentKnowledge(String agentName) {
        addTheory(String.format("self('%s').", agentName));
        addTheory("other(AGENT) :- not self(AGENT).");
        addTheory("anyone(AGENT) :- self(AGENT).");
        addTheory("anyone(AGENT) :- other(AGENT).");
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("behaviourManagerKnowledgePath", PATH_THEORY_BEHAVIOUR_MANAGER)
                .toString();
    }

}
