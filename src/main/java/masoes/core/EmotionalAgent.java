/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.logger.JadeLogger;
import masoes.behaviour.ResponseAgentStatusBehaviour;
import masoes.behaviour.StimulusReceiverBehaviour;
import masoes.ontology.MasoesOntology;
import masoes.ontology.Stimulus;
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
        getContentManager().registerOntology(new MasoesOntology());
        getContentManager().registerLanguage(new SLCodec());
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
        logger.agentEmotionalState(this);
        emotionalConfigurator.updateEmotion(stimulus);
        behaviourManager.updateBehaviour(this);
        logger.agentEmotionalStateChanged(this, stimulus);
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

}
