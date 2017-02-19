/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

// TODO: UNIT-TEST

import knowledge.KnowledgeBase;
import util.ToStringBuilder;

public class BehaviouralKnowledgeBase extends KnowledgeBase {

    public BehaviouralKnowledgeBase(String agentName, String agentKnowledgePath) {
        if (agentName != null) {
            addTheory(String.format("self(%s).", agentName));
            addTheory("other(X) :- not self(X).");
            if (agentKnowledgePath != null) {
                addTheoryFromPath(agentKnowledgePath);
            }
        }
        addTheoryFromPath("theories/behaviourManager.prolog");
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("version", getVersion())
                .toString();
    }

}
