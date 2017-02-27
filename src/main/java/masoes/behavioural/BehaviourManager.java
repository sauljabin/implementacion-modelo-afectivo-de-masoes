/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import alice.tuprolog.SolveInfo;
import knowledge.KnowledgeBaseException;
import masoes.EmotionalAgent;
import masoes.EmotionalBehaviour;
import util.ToStringBuilder;

public class BehaviourManager {

    private static final String ANSWER_VAR_NAME = "X";
    private static final String KNOWLEDGE_QUESTION = "behaviourPriority('%s'," + ANSWER_VAR_NAME + ").";
    private EmotionalBehaviour behaviour;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;

    public BehaviourManager(BehaviouralKnowledgeBase behaviouralKnowledgeBase) {
        this.behaviouralKnowledgeBase = behaviouralKnowledgeBase;
    }

    public EmotionalBehaviour getBehaviour() {
        return behaviour;
    }

    public void updateBehaviour(EmotionalAgent emotionalAgent, Emotion emotion) {
        if (behaviour != null) {
            emotionalAgent.removeBehaviour(behaviour);
        }

        behaviour = calculateBehaviour(emotionalAgent, emotion);

        if (behaviour != null) {
            emotionalAgent.addBehaviour(behaviour);
        }
    }

    private EmotionalBehaviour calculateBehaviour(EmotionalAgent emotionalAgent, Emotion emotion) {
        switch (getBehaviourTypeAssociated(emotion)) {
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
            String emotionName = emotion.getName().toLowerCase();
            SolveInfo solve = behaviouralKnowledgeBase.solve(String.format(KNOWLEDGE_QUESTION, emotionName));
            if (solve.isSuccess()) {
                String priority = solve.getTerm(ANSWER_VAR_NAME).toString().toUpperCase();
                return BehaviourType.valueOf(priority);
            }
            return BehaviourType.IMITATIVE;
        } catch (Exception e) {
            throw new KnowledgeBaseException(e);
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
