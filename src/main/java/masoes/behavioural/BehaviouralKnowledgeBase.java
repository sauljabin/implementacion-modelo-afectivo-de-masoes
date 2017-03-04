/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
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

    private static final String PATH_THEORY_BEHAVIOUR_MANAGER = "theories/behaviourManager.prolog";
    private static final String PATH_THEORY_EMOTIONAL_CONFIGURATOR = "theories/emotionalConfigurator.prolog";
    private EmotionalAgent emotionalAgent;

    public BehaviouralKnowledgeBase(EmotionalAgent emotionalAgent) {
        this.emotionalAgent = emotionalAgent;
        addAgentKnowledge();
        addTheory(Paths.get(PATH_THEORY_BEHAVIOUR_MANAGER));
        addTheory(Paths.get(PATH_THEORY_EMOTIONAL_CONFIGURATOR));
    }

    private void addAgentKnowledge() {
        if (emotionalAgent != null) {
            if (emotionalAgent.getLocalName() == null) {
                throw new KnowledgeException("No agent name, create in setup agent method");
            }
            addTheory(String.format("self('%s').", emotionalAgent.getLocalName()));
            addTheory("other(X) :- not self(X).");
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
                .append("emotionalConfiguratorKnowledgePath", PATH_THEORY_EMOTIONAL_CONFIGURATOR)
                .toString();
    }

}
