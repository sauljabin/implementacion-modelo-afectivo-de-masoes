/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import alice.tuprolog.SolveInfo;
import knowledge.KnowledgeException;
import util.ToStringBuilder;

public class BehaviourManager {

    private static final String ANSWER_VAR_NAME = "X";
    private static final String KNOWLEDGE_QUESTION = "behaviourPriority('%s'," + ANSWER_VAR_NAME + ").";
    private EmotionalConfigurator emotionalConfigurator;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;
    private BehaviourType behaviourType;

    public BehaviourManager(EmotionalConfigurator emotionalConfigurator, BehaviouralKnowledgeBase behaviouralKnowledgeBase) {
        this.emotionalConfigurator = emotionalConfigurator;
        this.behaviouralKnowledgeBase = behaviouralKnowledgeBase;
        updateBehaviour();
    }

    public BehaviourType getBehaviourType() {
        return behaviourType;
    }

    public void updateBehaviour() {
        behaviourType = getBehaviourTypeAssociated(emotionalConfigurator.getEmotion());
    }

    private BehaviourType getBehaviourTypeAssociated(Emotion emotion) {
        try {
            String emotionName = emotion.getName();
            SolveInfo solve = behaviouralKnowledgeBase.solve(String.format(KNOWLEDGE_QUESTION, emotionName));

            if (solve.isSuccess()) {
                String priority = solve.getTerm(ANSWER_VAR_NAME).toString().toUpperCase();
                return BehaviourType.valueOf(priority);
            }

            if (behaviourType != null) {
                return behaviourType;
            }

            return BehaviourType.IMITATIVE;
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("behaviourType", behaviourType)
                .append("behaviouralKnowledgeBase", behaviouralKnowledgeBase)
                .toString();
    }

}
