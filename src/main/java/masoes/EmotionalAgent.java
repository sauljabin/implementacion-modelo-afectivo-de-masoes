/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import agent.AgentLogger;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import masoes.conductual.BehaviourManager;
import masoes.conductual.Emotion;
import masoes.conductual.EmotionalConfigurator;
import masoes.conductual.EmotionalState;
import ontology.masoes.Stimulus;
import org.slf4j.LoggerFactory;
import util.ToStringBuilder;

public class EmotionalAgent extends Agent {

    private BehaviourManager behaviourManager;
    private EmotionalConfigurator emotionalConfigurator;
    private AgentLogger logger;

    private Behaviour cognitiveBehaviour;
    private Behaviour imitativeBehaviour;
    private Behaviour reactiveBehaviour;

    public EmotionalAgent() {
        logger = new AgentLogger(LoggerFactory.getLogger(EmotionalAgent.class));
        behaviourManager = new BehaviourManager();
        emotionalConfigurator = new EmotionalConfigurator();
    }

    @Override
    protected final void setup() {
        setUp();
        addBehaviour(new ResponseAgentStatusBehaviour(this));
        addBehaviour(new StimulusReceiverBehaviour(this));
        behaviourManager.updateBehaviour(this);
    }

    public Emotion getCurrentEmotion() {
        return emotionalConfigurator.getEmotion();
    }

    public Behaviour getCurrentEmotionalBehaviour() {
        return behaviourManager.getBehaviour();
    }

    public EmotionalState getEmotionalState() {
        return emotionalConfigurator.getEmotionalState();
    }

    public void evaluateStimulus(Stimulus stimulus) {
        logger.agent(this);
        emotionalConfigurator.updateEmotion(stimulus);
        behaviourManager.updateBehaviour(this);
        logger.agent(this);
    }

    public Behaviour getCognitiveBehaviour() {
        return cognitiveBehaviour;
    }

    public void setCognitiveBehaviour(Behaviour cognitiveBehaviour) {
        this.cognitiveBehaviour = cognitiveBehaviour;
    }

    public Behaviour getImitativeBehaviour() {
        return imitativeBehaviour;
    }

    public void setImitativeBehaviour(Behaviour imitativeBehaviour) {
        this.imitativeBehaviour = imitativeBehaviour;
    }

    public Behaviour getReactiveBehaviour() {
        return reactiveBehaviour;
    }

    public void setReactiveBehaviour(Behaviour reactiveBehaviour) {
        this.reactiveBehaviour = reactiveBehaviour;
    }

    protected void setUp() {

    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("aid", getAID())
                .append("behaviour", getCurrentEmotionalBehaviour())
                .append("emotion", getCurrentEmotion())
                .append("emotionalState", getEmotionalState())
                .toString();
    }

}
