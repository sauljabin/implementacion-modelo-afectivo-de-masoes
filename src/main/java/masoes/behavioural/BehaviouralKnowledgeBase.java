/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import knowledge.KnowledgeBase;
import knowledge.KnowledgeException;
import masoes.EmotionalAgent;
import util.ToStringBuilder;

import java.nio.file.Paths;

public class BehaviouralKnowledgeBase extends KnowledgeBase {

    private static final String PATH_THEORY_BEHAVIOUR_MANAGER = "theories/behavioural/behaviourManager.prolog";
    private EmotionalAgent emotionalAgent;

    public BehaviouralKnowledgeBase(EmotionalAgent emotionalAgent) {
        this.emotionalAgent = emotionalAgent;
        addAgentKnowledge();
        addTheory(Paths.get(PATH_THEORY_BEHAVIOUR_MANAGER));
    }

    private void addAgentKnowledge() {
        if (emotionalAgent != null) {
            if (emotionalAgent.getLocalName() == null) {
                throw new KnowledgeException("No agent name, create in setup agent method");
            }
            addTheory(String.format("self('%s').", emotionalAgent.getLocalName()));
            addTheory("other(AGENT) :- not self(AGENT).");
            addTheory("anyone(AGENT) :- self(AGENT).");
            addTheory("anyone(AGENT) :- other(AGENT).");
            addKnowledge(emotionalAgent.getKnowledge());
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("emotionalAgent", emotionalAgent)
                .append("behaviourManagerKnowledgePath", PATH_THEORY_BEHAVIOUR_MANAGER)
                .toString();
    }

}
