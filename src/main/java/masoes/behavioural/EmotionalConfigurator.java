/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import alice.tuprolog.SolveInfo;
import knowledge.KnowledgeException;
import masoes.EmotionalAgent;
import ontology.masoes.stimulus.ActionStimulus;
import ontology.masoes.stimulus.ObjectStimulus;
import ontology.masoes.stimulus.Stimulus;
import util.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmotionalConfigurator {

    private static final String ANSWER_VAR_NAME = "X";

    private EmotionalState emotionalState;
    private EmotionalSpace emotionalSpace;
    private EmotionalAgent emotionalAgent;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;

    public EmotionalConfigurator(EmotionalAgent emotionalAgent, BehaviouralKnowledgeBase behaviouralKnowledgeBase) {
        this.emotionalAgent = emotionalAgent;
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
            String question = getQuestion(stimulus);

            List<SolveInfo> solveInfos = new ArrayList<>();

            solveInfos.add(behaviouralKnowledgeBase.solve(question));

            while (behaviouralKnowledgeBase.hasOpenAlternatives()) {
                SolveInfo solveNext = behaviouralKnowledgeBase.solveNext();
                if (solveNext.isSuccess()) {
                    solveInfos.add(solveNext);
                }
            }

            int random = new Random().nextInt(solveInfos.size());

            SolveInfo solveEmotion = solveInfos.get(random);

            if (solveEmotion.isSuccess()) {
                String emotionName = solveEmotion.getTerm(ANSWER_VAR_NAME).toString().replace("'", "").toLowerCase();
                Emotion newEmotion = emotionalSpace.searchEmotion(emotionName);

                if (newEmotion != null) {
                    if (!getEmotion().getName().equals(newEmotion.getName())) {
                        emotionalAgent.getLogger().updatingEmotion(getEmotion(), newEmotion);
                    }

                    return newEmotion.getRandomEmotionalState();
                }
            }
            return emotionalState;
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

    private String getQuestion(Stimulus stimulus) {
        if (stimulus instanceof ActionStimulus) {
            return String.format("emotionByAction(%s,%s," + ANSWER_VAR_NAME + ").", getActor(stimulus), getQuestionParameter(stimulus));
        } else {
            return String.format("emotionByObject(%s,%s," + ANSWER_VAR_NAME + ").", getActor(stimulus), getQuestionParameter(stimulus));
        }
    }

    private String getQuestionParameter(Stimulus stimulus) {
        if (stimulus instanceof ActionStimulus) {
            ActionStimulus actionStimulus = (ActionStimulus) stimulus;
            return String.format("'%s'", actionStimulus.getActionName());
        } else {
            ObjectStimulus objectStimulus = (ObjectStimulus) stimulus;

            return String.format("'%s'", objectStimulus.getObjectName());
        }
    }

    private String getActor(Stimulus stimulus) {
        if (stimulus instanceof ActionStimulus) {
            ActionStimulus actionStimulus = (ActionStimulus) stimulus;
            return String.format("'%s'", actionStimulus.getActor().getLocalName());
        } else {
            ObjectStimulus objectStimulus = (ObjectStimulus) stimulus;
            return String.format("'%s'", objectStimulus.getCreator().getLocalName());
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
