/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behavioural;

import alice.tuprolog.SolveInfo;
import knowledge.KnowledgeException;
import masoes.EmotionalAgent;
import ontology.masoes.ActionStimulus;
import ontology.masoes.ObjectProperty;
import ontology.masoes.ObjectStimulus;
import ontology.masoes.Stimulus;
import util.ToStringBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmotionalConfigurator {

    private static final String ANSWER_VAR_NAME = "X";
    private static final String KNOWLEDGE_QUESTION = "emotion(%s,%s," + ANSWER_VAR_NAME + ").";

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
            SolveInfo solveEmotion = behaviouralKnowledgeBase.solve(String.format(KNOWLEDGE_QUESTION, getActor(stimulus), getQuestionParameter(stimulus)));
            if (solveEmotion.isSuccess()) {
                String emotionName = solveEmotion.getTerm(ANSWER_VAR_NAME).toString().replace("'", "").toLowerCase();
                Emotion newEmotion = emotionalSpace.searchEmotion(emotionName);
                EmotionalState newEmotionalState = newEmotion.getRandomEmotionalState();
                if (!getEmotion().getName().equals(newEmotion.getName())) {
                    emotionalAgent.getLogger().updatingEmotion(getEmotion(), newEmotion);
                }
                return newEmotionalState;
            }
            return emotionalState;
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

    private String getQuestionParameter(Stimulus stimulus) {
        if (stimulus instanceof ActionStimulus) {
            ActionStimulus actionStimulus = (ActionStimulus) stimulus;
            return String.format("'%s'", actionStimulus.getActionName());
        } else {
            ObjectStimulus objectStimulus = (ObjectStimulus) stimulus;
            Object[] properties = objectStimulus.getObjectProperties().toArray();

            List<String> stringsProperties = Arrays.stream(properties).map(object -> {
                ObjectProperty objectProperty = (ObjectProperty) object;
                return String.format("'%s'='%s'", objectProperty.getName(), objectProperty.getValue());
            }).collect(Collectors.toList());

            return String.format("[%s]", String.join(",", stringsProperties));
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
