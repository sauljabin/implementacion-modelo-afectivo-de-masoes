/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import masoes.app.logger.ApplicationLogger;
import masoes.core.behaviour.ReplayAgentInformationBehaviour;
import masoes.core.behaviour.StimulusReceiverBehaviour;
import org.slf4j.LoggerFactory;

public class EmotionalAgent extends Agent {

    private BehaviourManager behaviourManager;
    private EmotionalConfigurator emotionalConfigurator;
    private ApplicationLogger logger;
    private BehaviourFactory behaviourFactory;

    public EmotionalAgent() {
        logger = new ApplicationLogger(LoggerFactory.getLogger(EmotionalAgent.class));
        behaviourManager = new BehaviourManager();
        emotionalConfigurator = new EmotionalConfigurator();
    }

    @Override
    protected final void setup() {
        setUp();
        addBehaviour(new ReplayAgentInformationBehaviour(this));
        addBehaviour(new StimulusReceiverBehaviour(this));
        behaviourManager.updateBehaviour(this);
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
        logger.agentEmotionalState(this);
        emotionalConfigurator.updateEmotion(stimulus);
        behaviourManager.updateBehaviour(this);
        logger.agentEmotionalStateChanged(this, stimulus);
    }

    public BehaviourFactory getBehaviourFactory() {
        return behaviourFactory;
    }

    public void setBehaviourFactory(BehaviourFactory behaviourFactory) {
        this.behaviourFactory = behaviourFactory;
    }

    protected void setUp() {

    }

}
