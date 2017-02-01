/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package experimental;

import agent.AgentLogger;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.lang.acl.ACLMessage;
import org.slf4j.LoggerFactory;
import util.StringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class FunctionalTest extends Behaviour {

    private static final int AGENT_NAME_LENGTH = 10;
    private static final int TIMEOUT = 4000;
    private List<AID> agentsToKill = new ArrayList<>();
    private StringGenerator stringGenerator = new StringGenerator();
    private AgentLogger logger = new AgentLogger(LoggerFactory.getLogger(FunctionalTest.class));
    private ContentManager contentManager = new ContentManager();

    @Override
    public void onStart() {
        contentManager.registerOntology(JADEManagementOntology.getInstance());
        contentManager.registerLanguage(new SLCodec(0));
        setUp();
    }

    @Override
    public int onEnd() {
        agentsToKill.forEach(aid -> {
            try {
                killAgent(aid);
            } catch (Exception e) {
                logger.exception(myAgent, e);
            }
        });
        return 0;
    }

    public abstract void setUp();

    @Override
    public boolean done() {
        return true;
    }

    private void sendMessageAMS(Concept concept) throws Exception {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setSender(myAgent.getAID());
        message.addReceiver(myAgent.getAMS());
        message.setOntology(JADEManagementOntology.NAME);
        message.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);

        contentManager.fillContent(message, new Action(myAgent.getAMS(), concept));
        ACLMessage response = FIPAService.doFipaRequestClient(myAgent, message, TIMEOUT);
        if (response != null) {
            ContentElement contentElement = contentManager.extractContent(response);
            if (!(contentElement instanceof Done)) {
                throw new FunctionalTestException("Unknown notification received from " + myAgent.getAMS());
            }
        } else {
            throw new FunctionalTestException("Timeout expired");
        }
    }

    public void killAgent(AID aid) {
        KillAgent killAgent = new KillAgent();
        killAgent.setAgent(aid);

        try {
            sendMessageAMS(killAgent);
        } catch (FunctionalTestException e) {
            throw e;
        } catch (Exception e) {
            logger.exception(myAgent, e);
            throw new FunctionalTestException(String.format("Error killing agent \"%s\"", aid), e);
        }
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass, List<String> arguments) {
        CreateAgent createAgent = new CreateAgent();
        createAgent.setAgentName(agentName);
        createAgent.setClassName(agentClass.getCanonicalName());
        createAgent.setContainer((ContainerID) myAgent.here());

        if (Optional.ofNullable(arguments).isPresent()) {
            arguments.forEach(arg -> createAgent.addArguments(arg));
        }

        try {
            AID aid = myAgent.getAID(agentName);
            agentsToKill.add(aid);
            sendMessageAMS(createAgent);
            return aid;
        } catch (FunctionalTestException e) {
            throw e;
        } catch (Exception e) {
            logger.exception(myAgent, e);
            throw new FunctionalTestException(String.format("Error creating agent \"%s\"", agentName), e);
        }
    }

    public AID createAgent(Class<? extends Agent> agentClass) {
        return createAgent(stringGenerator.getString(AGENT_NAME_LENGTH), agentClass);
    }

    public AID createAgent(Class<? extends Agent> agentClass, List<String> arguments) {
        return createAgent(stringGenerator.getString(AGENT_NAME_LENGTH), agentClass, arguments);
    }

    public AID createAgent(String agentName, Class<? extends Agent> agentClass) {
        return createAgent(agentName, agentClass, null);
    }

}
