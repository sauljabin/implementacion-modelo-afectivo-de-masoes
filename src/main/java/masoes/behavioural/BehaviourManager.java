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

// TODO: UNIT-TEST

public class BehaviourManager {

    private EmotionalBehaviour behaviour;
    private EmotionalAgent emotionalAgent;
    private EmotionalConfigurator emotionalConfigurator;
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
        if (behaviour != null) {
            emotionalAgent.removeBehaviour(behaviour);
        }

        behaviour = calculateBehaviour();

        if (behaviour != null) {
            emotionalAgent.addBehaviour(behaviour);
        }
    }

    public BehaviourType getBehaviourTypeAssociated(Emotion emotion) {
        try {
            String emotionName = emotion.getName().toLowerCase();
            SolveInfo solve = behaviouralKnowledgeBase.solve(String.format("behaviourPriority(%s,X).", emotionName));
            if (solve.isSuccess()) {
                String priority = solve.getTerm("X").toString().toUpperCase();
                return BehaviourType.valueOf(priority);
            }
            return BehaviourType.IMITATIVE;
        } catch (Exception e) {
            throw new KnowledgeBaseException(e);
        }
    }

    private EmotionalBehaviour calculateBehaviour() {
        switch (getBehaviourTypeAssociated(emotionalConfigurator.getEmotion())) {
            case COGNITIVE:
                return emotionalAgent.getCognitiveBehaviour();
            case IMITATIVE:
                return emotionalAgent.getImitativeBehaviour();
            case REACTIVE:
            default:
                return emotionalAgent.getReactiveBehaviour();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("behaviour", behaviour)
                .toString();
    }

}
