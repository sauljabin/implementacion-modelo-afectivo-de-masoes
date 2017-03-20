/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes;

import agent.configurable.ConfigurableAgent;
import environment.Environment;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import masoes.colective.NotifierAgent;
import masoes.ontology.MasoesOntology;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
import ontology.OntologyAssistant;
import org.junit.Before;
import org.junit.Test;
import test.FunctionalTest;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmotionalBehaviourFunctionalTest extends FunctionalTest {

    @Before
    public void setUp() {
        createAgent(Environment.NOTIFIER_AGENT, NotifierAgent.class, null);
    }

    @Test
    public void shouldNotifyAction() {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(MasoesOntology.ACTION_EVALUATE_STIMULUS);
        serviceDescription.setType(MasoesOntology.ACTION_EVALUATE_STIMULUS);
        register(serviceDescription);

        AID emotionalAgent = createAgent(ConfigurableAgent.class, null);
        addBehaviour(emotionalAgent, EmotionalBehaviourNotifier.class);

        OntologyAssistant ontologyAssistant = createOntologyAssistant(MasoesOntology.getInstance());

        ACLMessage stimulus = blockingReceive();
        deregister();

        Action action = (Action) ontologyAssistant.extractMessageContent(stimulus);

        ContentElement contentElementStimulus = (ContentElement) action.getAction();
        assertThat(contentElementStimulus, is(instanceOf(EvaluateStimulus.class)));
        EvaluateStimulus evaluateStimulus = (EvaluateStimulus) contentElementStimulus;

        ActionStimulus actionStimulus = (ActionStimulus) evaluateStimulus.getStimulus();

        assertThat(actionStimulus.getActionName(), is("expectedActionForTest"));
        assertThat(actionStimulus.getActor(), is(emotionalAgent));
    }

}