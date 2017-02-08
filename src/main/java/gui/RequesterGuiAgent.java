/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import agent.AgentLogger;
import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ontology.OntologyAssistant;
import ontology.settings.GetAllSettings;
import ontology.settings.SettingsOntology;
import org.slf4j.LoggerFactory;

public class RequesterGuiAgent extends GuiAgent {

    private AgentLogger logger;
    private RequesterGui requesterGui;
    private RequesterGuiListener requesterGuiListener;

    public RequesterGuiAgent() {
        requesterGui = new RequesterGui();
        requesterGuiListener = new RequesterGuiListener(requesterGui, this);
        logger = new AgentLogger(LoggerFactory.getLogger(RequesterGuiAgent.class));
    }

    @Override
    protected void setup() {
        addBehaviour(new GuiResponseHandlerBehaviour(this, requesterGui));
        requesterGui.setSenderAgentName(getLocalName());
        requesterGui.showGui();
    }

    @Override
    protected void takeDown() {
        requesterGui.closeGui();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (RequesterGuiEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    doDelete();
                    break;
                case SEND_MESSAGE:
                    sendMessage();
                    break;
                case CHANGE_ACTION:
                    break;
                case SAVE_MESSAGE_LOGS:
                    break;
                case CLEAR_MESSAGE_LOGS:
                    clearMessageLogs();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.exception(this, e);
        }
    }

    private void clearMessageLogs() {
        requesterGui.clearMessageLogs();
    }

    private void sendMessage() {
        AID receiver = getAID(requesterGui.getReceiverAgentName());
        GetAllSettings agentAction = new GetAllSettings();
        SettingsOntology ontology = SettingsOntology.getInstance();

        OntologyAssistant ontologyAssistant = new OntologyAssistant(this, ontology);
        ACLMessage requestAction = ontologyAssistant.createRequestAction(receiver, agentAction);

        requesterGui.logMessage(requestAction);
        send(requestAction);
    }

}
