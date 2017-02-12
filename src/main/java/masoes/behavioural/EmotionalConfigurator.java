/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import alice.tuprolog.SolveInfo;
import knowledge.KnowledgeBaseException;
import ontology.masoes.Stimulus;
import util.ToStringBuilder;

// TODO: UNIT-TEST

public class EmotionalConfigurator {

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
            String actorName = stimulus.getActor().getLocalName().toLowerCase();
            String actionName = stimulus.getActionName().toLowerCase();

            SolveInfo solveActivation = behaviouralKnowledgeBase.solve(String.format("activation(%s,%s,X).", actorName, actionName));
            SolveInfo solveSatisfaction = behaviouralKnowledgeBase.solve(String.format("satisfaction(%s,%s,X).", actorName, actionName));

            if (solveActivation.isSuccess() && solveSatisfaction.isSuccess()) {
                double activation = Double.parseDouble(solveActivation.getTerm("X").toString());
                double satisfaction = Double.parseDouble(solveSatisfaction.getTerm("X").toString());
                return new EmotionalState(activation, satisfaction);
            }
            return new EmotionalState();
        } catch (Exception e) {
            throw new KnowledgeBaseException(e);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("emotionalState", emotionalState)
                .toString();
    }

}
