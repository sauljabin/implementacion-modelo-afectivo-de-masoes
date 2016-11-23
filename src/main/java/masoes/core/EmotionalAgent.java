/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import masoes.core.behaviour.ReplayAgentInformationBehaviour;
import masoes.core.behaviour.StimulusReceiverBehaviour;

import java.util.Optional;

public abstract class EmotionalAgent extends Agent {

    private BehaviourManager behaviourManager;
    private EmotionalConfigurator emotionalConfigurator;
    private Emotion currentEmotion;
    private Behaviour emotionalBehaviour;
    private EmotionalModel emotionalModel;

    @Override
    protected void setup() {
        emotionalModel = createEmotionalModel();
        behaviourManager = emotionalModel.getBehaviourManager();
        emotionalConfigurator = emotionalModel.getEmotionalConfigurator();
        addBehaviour(new ReplayAgentInformationBehaviour(this));
        addBehaviour(new StimulusReceiverBehaviour(this));
        setUp();
    }

    public Emotion getCurrentEmotion() {
        return currentEmotion;
    }

    public Behaviour getEmotionalBehaviour() {
        return emotionalBehaviour;
    }

    public void evaluateStimulus(Stimulus stimulus) {
        emotionalConfigurator.updateEmotionalState(stimulus);
        currentEmotion = emotionalConfigurator.getEmotion();

        if (Optional.ofNullable(emotionalBehaviour).isPresent()) {
            removeBehaviour(emotionalBehaviour);
        }

        emotionalBehaviour = behaviourManager.selectBehaviour(currentEmotion);
        addBehaviour(emotionalBehaviour);
    }

    protected abstract EmotionalModel createEmotionalModel();

    protected abstract void setUp();

}
