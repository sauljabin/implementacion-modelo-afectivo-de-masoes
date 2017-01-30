/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package functional.test.environment.dummy;

import environment.dummy.DummyEmotionalAgent;
import functional.test.FunctionalTest;
import jade.content.Predicate;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.protocol.OntologyRequesterBehaviour;
import masoes.ontology.AgentStatus;
import masoes.ontology.EvaluateStimulus;
import masoes.ontology.GetAgentStatus;
import masoes.ontology.MasoesOntology;
import masoes.ontology.Stimulus;
import test.common.TestException;

public class ShouldChangeEmotionAndBehaviourTest extends FunctionalTest {

    private AgentStatus firstEmotionalResponse;

    @Override
    public Behaviour load(Agent tester) throws TestException {
        setTimeout(TIMEOUT_DEFAULT);

        AID dummyAgentAID = createAgent(tester, DummyEmotionalAgent.class.getName());

        OntologyRequesterBehaviour firstRequest = new OntologyRequesterBehaviour(
                null,
                dummyAgentAID,
                new GetAgentStatus(),
                new MasoesOntology()) {

            @Override
            protected void handleInform(Predicate predicate) {
                firstEmotionalResponse = (AgentStatus) predicate;
            }
        };

        OntologyRequesterBehaviour stimulateRequest = new OntologyRequesterBehaviour(
                null,
                dummyAgentAID,
                new EvaluateStimulus(new Stimulus()),
                new MasoesOntology()) {

        };

        OntologyRequesterBehaviour secondRequest = new OntologyRequesterBehaviour(
                null,
                dummyAgentAID,
                new GetAgentStatus(),
                new MasoesOntology()) {

            @Override
            protected void handleInform(Predicate predicate) {
                AgentStatus agentStatus = (AgentStatus) predicate;
                assertReflectionNotEquals("Agent Status", firstEmotionalResponse, agentStatus);
            }
        };

        SequentialBehaviour testerBehaviour = new SequentialBehaviour();
        testerBehaviour.addSubBehaviour(firstRequest);
        testerBehaviour.addSubBehaviour(stimulateRequest);
        testerBehaviour.addSubBehaviour(secondRequest);

        return testerBehaviour;
    }

}
