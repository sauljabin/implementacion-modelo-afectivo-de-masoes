/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import alice.tuprolog.SolveInfo;
import knowledge.KnowledgeBaseException;
import ontology.masoes.ActionStimulus;
import ontology.masoes.Stimulus;
import util.ToStringBuilder;

public class EmotionalConfigurator {

    private static final String ANSWER_VAR_NAME = "X";
    private static final String KNOWLEDGE_QUESTION = "emotion('%s','%s'," + ANSWER_VAR_NAME + ").";

    private EmotionalState emotionalState;
    private EmotionalSpace emotionalSpace;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;

    public EmotionalConfigurator(BehaviouralKnowledgeBase behaviouralKnowledgeBase) {
        this.behaviouralKnowledgeBase = behaviouralKnowledgeBase;
        emotionalState = new EmotionalState();
        emotionalSpace = new EmotionalSpace();
    }

    public Emotion getEmotion() {
        return emotionalSpace.searchEmotion(emotionalState);
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void updateEmotion(Stimulus stimulus) {
        emotionalState = calculateEmotionalState(stimulus);
    }

    private EmotionalState calculateEmotionalState(Stimulus stimulus) {
        try {
            ActionStimulus actionStimulus = (ActionStimulus) stimulus;
            String actorName = actionStimulus.getActor().getLocalName();
            String actionName = actionStimulus.getActionName();

            SolveInfo solveEmotion = behaviouralKnowledgeBase.solve(String.format(KNOWLEDGE_QUESTION, actorName, actionName));

            if (solveEmotion.isSuccess()) {
                String emotionName = solveEmotion.getTerm(ANSWER_VAR_NAME).toString().replace("'", "").toLowerCase();
                return emotionalSpace.searchEmotion(emotionName).getRandomEmotionalState();
            }
            return new EmotionalState();
        } catch (Exception e) {
            throw new KnowledgeBaseException(e);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("emotion", getEmotion())
                .append("emotionalState", emotionalState)
                .append("emotionalSpace", emotionalSpace)
                .append("behaviouralKnowledgeBase", behaviouralKnowledgeBase)
                .toString();
    }

}
