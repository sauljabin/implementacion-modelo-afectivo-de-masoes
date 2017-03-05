/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import alice.tuprolog.SolveInfo;
import knowledge.KnowledgeException;
import masoes.EmotionalAgent;
import masoes.EmotionalBehaviour;
import util.ToStringBuilder;

public class BehaviourManager {

    private static final String ANSWER_VAR_NAME = "X";
    private static final String KNOWLEDGE_QUESTION = "behaviourPriority('%s'," + ANSWER_VAR_NAME + ").";
    private final EmotionalAgent emotionalAgent;
    private final EmotionalConfigurator emotionalConfigurator;
    private EmotionalBehaviour behaviour;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;

    public BehaviourManager(EmotionalAgent emotionalAgent, EmotionalConfigurator emotionalConfigurator, BehaviouralKnowledgeBase behaviouralKnowledgeBase) {
        this.emotionalAgent = emotionalAgent;
        this.emotionalConfigurator = emotionalConfigurator;
        this.behaviouralKnowledgeBase = behaviouralKnowledgeBase;
        updateBehaviour();
    }

    public EmotionalBehaviour getBehaviour() {
        return behaviour;
    }

    public void updateBehaviour() {
        BehaviourType newType = getBehaviourTypeAssociated(emotionalConfigurator.getEmotion());

        if (behaviour != null) {
            BehaviourType type = behaviour.getType();
            if (newType == type) {
                return;
            }
            emotionalAgent.log(String.format("Actualizando comportamiento %s a %s", type, newType));
            emotionalAgent.removeBehaviour(behaviour);
        } else {
            emotionalAgent.log(String.format("Inicializando comportamiento %s", newType));
        }

        behaviour = calculateBehaviour(newType, emotionalAgent);

        if (behaviour != null) {
            emotionalAgent.addBehaviour(behaviour);
        }

    }

    private EmotionalBehaviour calculateBehaviour(BehaviourType type, EmotionalAgent emotionalAgent) {
        switch (type) {
            case COGNITIVE:
                return emotionalAgent.getCognitiveBehaviour();
            case IMITATIVE:
                return emotionalAgent.getImitativeBehaviour();
            case REACTIVE:
            default:
                return emotionalAgent.getReactiveBehaviour();
        }
    }

    private BehaviourType getBehaviourTypeAssociated(Emotion emotion) {
        try {
            String emotionName = emotion.getName();
            SolveInfo solve = behaviouralKnowledgeBase.solve(String.format(KNOWLEDGE_QUESTION, emotionName));

            if (solve.isSuccess()) {
                String priority = solve.getTerm(ANSWER_VAR_NAME).toString().toUpperCase();
                return BehaviourType.valueOf(priority);
            }

            if (behaviour != null) {
                return behaviour.getType();
            }

            return BehaviourType.IMITATIVE;
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("behaviour", behaviour)
                .append("behaviouralKnowledgeBase", behaviouralKnowledgeBase)
                .toString();
    }

}
