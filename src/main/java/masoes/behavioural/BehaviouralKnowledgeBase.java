/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

// TODO: UNIT-TEST

import knowledge.KnowledgeBase;
import masoes.EmotionalAgent;
import util.ToStringBuilder;

public class BehaviouralKnowledgeBase extends KnowledgeBase {

    public BehaviouralKnowledgeBase(EmotionalAgent emotionalAgent) {
        String agentName = emotionalAgent.getLocalName().toLowerCase();

        addTheory(String.format("self(%s).", agentName));
        addTheory("other(X) :- not self(X).");
        addTheoryFromPath("theories/behaviourManager.pl");

        if (emotionalAgent.getKnowledgePath() != null) {
            addTheoryFromPath(emotionalAgent.getKnowledgePath());
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("version", getVersion())
                .toString();
    }

}
