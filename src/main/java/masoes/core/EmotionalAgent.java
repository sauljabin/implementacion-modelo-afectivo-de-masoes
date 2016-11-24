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

public abstract class EmotionalAgent extends Agent {

    private BehaviourManager behaviourManager;
    private EmotionalConfigurator emotionalConfigurator;
    private EmotionalModel emotionalModel;

    @Override
    protected void setup() {
        emotionalModel = createEmotionalModel();
        behaviourManager = emotionalModel.getBehaviourManager();
        emotionalConfigurator = emotionalModel.getEmotionalConfigurator();
        behaviourManager.updateBehaviour(emotionalConfigurator.getEmotion());

        addBehaviour(new ReplayAgentInformationBehaviour(this));
        addBehaviour(new StimulusReceiverBehaviour(this));
        addBehaviour(behaviourManager.getBehaviour());
        setUp();
    }

    public Emotion getCurrentEmotion() {
        return emotionalConfigurator.getEmotion();
    }

    public Behaviour getEmotionalBehaviour() {
        return behaviourManager.getBehaviour();
    }

    public EmotionalState getEmotionalState() {
        return emotionalConfigurator.getEmotionalState();
    }

    public void evaluateStimulus(Stimulus stimulus) {
        removeBehaviour(behaviourManager.getBehaviour());
        emotionalConfigurator.updateEmotionalState(stimulus);
        behaviourManager.updateBehaviour(emotionalConfigurator.getEmotion());
        addBehaviour(behaviourManager.getBehaviour());
    }

    protected abstract EmotionalModel createEmotionalModel();

    protected abstract void setUp();

}
