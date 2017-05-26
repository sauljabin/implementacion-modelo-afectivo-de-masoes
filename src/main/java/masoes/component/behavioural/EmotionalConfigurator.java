/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.component.behavioural;

import alice.tuprolog.NoSolutionException;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.UnknownVarException;
import knowledge.KnowledgeException;
import masoes.MasoesSettings;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EventStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import masoes.ontology.stimulus.Stimulus;
import util.StringValidator;
import util.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmotionalConfigurator {

    private static final String ANSWER_ACTIVATION_VAR_NAME = "X";
    private static final String ANSWER_SATISFACTION_VAR_NAME = "Y";
    private static final String POSITIVE = "positive";
    private static final String NEGATIVE = "negative";
    private static final String DEFAULT_INCREASE_PARAMETER = "0.1";

    private EmotionalState emotionalState;
    private AffectiveModel affectiveModel;
    private BehaviouralKnowledgeBase behaviouralKnowledgeBase;

    public EmotionalConfigurator(BehaviouralKnowledgeBase behaviouralKnowledgeBase) {
        this.behaviouralKnowledgeBase = behaviouralKnowledgeBase;
        emotionalState = new EmotionalState();
        affectiveModel = AffectiveModel.getInstance();
    }

    public Emotion getEmotion() {
        return affectiveModel.searchEmotion(emotionalState);
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(EmotionalState emotionalState) {
        this.emotionalState = emotionalState;
    }

    public void updateEmotion(Stimulus stimulus) {
        emotionalState = calculateEmotionalState(stimulus);
    }

    private EmotionalState calculateEmotionalState(Stimulus stimulus) {
        try {
            SolveInfo solveEmotion = getSolutionQuestion(stimulus);

            if (solveEmotion.isSuccess()) {
                String activationValence = getActivationValence(solveEmotion);
                String satisfactionValence = getSatisfactionValence(solveEmotion);

                double activation = emotionalState.getActivation();
                double satisfaction = emotionalState.getSatisfaction();

                if (activationValence.equalsIgnoreCase(POSITIVE)) {
                    activation += getActivationParameter();
                } else if (activationValence.equalsIgnoreCase(NEGATIVE)) {
                    activation -= getActivationParameter();
                }

                if (satisfactionValence.equalsIgnoreCase(POSITIVE)) {
                    satisfaction += getSatisfactionParameter();
                } else if (satisfactionValence.equalsIgnoreCase(NEGATIVE)) {
                    satisfaction -= getSatisfactionParameter();
                }

                return new EmotionalState(activation, satisfaction);
            }
            return emotionalState;
        } catch (Exception e) {
            throw new KnowledgeException(e);
        }
    }

    private SolveInfo getSolutionQuestion(Stimulus stimulus) throws Exception {
        String question = getQuestion(stimulus);

        List<SolveInfo> solutions = new ArrayList<>();

        solutions.add(behaviouralKnowledgeBase.solve(question));

        while (behaviouralKnowledgeBase.hasOpenAlternatives()) {
            SolveInfo solveNext = behaviouralKnowledgeBase.solveNext();
            if (solveNext.isSuccess()) {
                solutions.add(solveNext);
            }
        }

        int random = new Random().nextInt(solutions.size());

        return solutions.get(random);
    }

    private double getActivationParameter() {
        String activationParameterString = MasoesSettings.getInstance().get(MasoesSettings.MASOES_ACTIVATION_PARAMETER, DEFAULT_INCREASE_PARAMETER);
        if (!StringValidator.isReal(activationParameterString)) {
            activationParameterString = DEFAULT_INCREASE_PARAMETER;
        }
        return Double.parseDouble(activationParameterString);
    }

    private double getSatisfactionParameter() {
        String satisfactionParameterString = MasoesSettings.getInstance().get(MasoesSettings.MASOES_SATISFACTION_PARAMETER, DEFAULT_INCREASE_PARAMETER);
        if (!StringValidator.isReal(satisfactionParameterString)) {
            satisfactionParameterString = DEFAULT_INCREASE_PARAMETER;
        }
        return Double.parseDouble(satisfactionParameterString);
    }

    private String getSatisfactionValence(SolveInfo solveEmotion) throws NoSolutionException, UnknownVarException {
        return solveEmotion.getTerm(ANSWER_SATISFACTION_VAR_NAME).toString().replace("'", "").toLowerCase();
    }

    private String getActivationValence(SolveInfo solveEmotion) throws NoSolutionException, UnknownVarException {
        return solveEmotion.getTerm(ANSWER_ACTIVATION_VAR_NAME).toString().replace("'", "").toLowerCase();
    }

    private String getQuestion(Stimulus stimulus) {
        return String.format("valence(%s, %s, " + ANSWER_ACTIVATION_VAR_NAME + ", " + ANSWER_SATISFACTION_VAR_NAME + ").", getAgent(stimulus), getItemName(stimulus));
    }

    private String getItemName(Stimulus stimulus) {
        String name;
        if (stimulus instanceof ActionStimulus) {
            ActionStimulus actionStimulus = (ActionStimulus) stimulus;
            name = actionStimulus.getActionName();
        } else if (stimulus instanceof ObjectStimulus) {
            ObjectStimulus objectStimulus = (ObjectStimulus) stimulus;
            name = objectStimulus.getObjectName();
        } else {
            EventStimulus eventStimulus = (EventStimulus) stimulus;
            name = eventStimulus.getEventName();
        }
        return String.format("'%s'", name);
    }

    private String getAgent(Stimulus stimulus) {
        String name;
        if (stimulus instanceof ActionStimulus) {
            ActionStimulus actionStimulus = (ActionStimulus) stimulus;
            name = actionStimulus.getActor().getLocalName();
        } else if (stimulus instanceof ObjectStimulus) {
            ObjectStimulus objectStimulus = (ObjectStimulus) stimulus;
            name = objectStimulus.getCreator().getLocalName();
        } else {
            EventStimulus eventStimulus = (EventStimulus) stimulus;
            name = eventStimulus.getAffected().getLocalName();
        }
        return String.format("'%s'", name);
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("emotion", getEmotion())
                .append("emotionalState", emotionalState)
                .append("affectiveModel", affectiveModel)
                .append("behaviouralKnowledgeBase", behaviouralKnowledgeBase)
                .toString();
    }

}
