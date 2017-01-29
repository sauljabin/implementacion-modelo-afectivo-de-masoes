/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import logger.writer.JadeLogger;
import masoes.model.BehaviourManager;
import masoes.model.Emotion;
import masoes.model.EmotionalConfigurator;
import masoes.model.EmotionalState;
import masoes.ontology.Stimulus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.LoggerFactory;

public class EmotionalAgent extends Agent {

    private BehaviourManager behaviourManager;
    private EmotionalConfigurator emotionalConfigurator;
    private JadeLogger logger;

    private Behaviour cognitiveBehaviour;
    private Behaviour imitativeBehaviour;
    private Behaviour reactiveBehaviour;

    public EmotionalAgent() {
        logger = new JadeLogger(LoggerFactory.getLogger(EmotionalAgent.class));
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("aid", getAID())
                .append("behaviour", getCurrentEmotionalBehaviour())
                .append("emotion", getCurrentEmotion())
                .append("emotionalState", getEmotionalState())
                .toString();
    }

}
