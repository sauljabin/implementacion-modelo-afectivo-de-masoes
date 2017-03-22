/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import masoes.MasoesSettings;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import ontology.OntologyAssistant;
import protocol.ProtocolAssistant;

import java.util.Arrays;

public class ConfiguratorAgent extends GuiAgent {

    private AgentLogger logger;
    private ConfiguratorAgentGui configuratorAgentGui;
    private ConfiguratorAgentListener configuratorAgentListener;
    private AgentManagementAssistant agentManagementAssistant;
    private OntologyAssistant masoesOntologyAssistant;
    private ProtocolAssistant protocolAssistant;

    public ConfiguratorAgent() {
        configuratorAgentGui = new ConfiguratorAgentGui();
        configuratorAgentListener = new ConfiguratorAgentListener(this, configuratorAgentGui);
        logger = new AgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
        masoesOntologyAssistant = new OntologyAssistant(this, MasoesOntology.getInstance());
        protocolAssistant = new ProtocolAssistant(this);
    }

    @Override
    protected void setup() {
        configuratorAgentGui.showGui();

        agentManagementAssistant.createAgent("testAgent", ContributorAgent.class, null);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage requestAction = masoesOntologyAssistant.createRequestAction(getAID("testAgent"), new GetEmotionalState());
                ACLMessage response = protocolAssistant.sendRequest(requestAction, ACLMessage.INFORM);
                AgentState agentState = (AgentState) masoesOntologyAssistant.extractMessageContent(response);
                configuratorAgentGui.setAgentStates(Arrays.asList(agentState));
                block(1000 / 10);
            }
        });
    }

    @Override
    protected void takeDown() {
        configuratorAgentGui.closeGui();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (ConfiguratorAgentEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    doDelete();
                    break;
                case UPDATE_SATISFACTION_INCREASE:
                    updateSatisfactionIncrease();
                    break;
                case UPDATE_ACTIVATION_INCREASE:
                    updateActivationIncrease();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
            configuratorAgentGui.showError(e.getMessage());
        }
    }

    private void updateActivationIncrease() {
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_ACTIVATION_INCREASE, String.valueOf(configuratorAgentGui.getActivationIncrease()));
    }

    private void updateSatisfactionIncrease() {
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_SATISFACTION_INCREASE, String.valueOf(configuratorAgentGui.getSatisfactionIncrease()));
    }

}
