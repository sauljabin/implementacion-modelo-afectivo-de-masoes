/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import agent.AgentLogger;
import jade.content.AgentAction;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ontology.OntologyAssistant;
import ontology.settings.GetAllSettings;
import ontology.settings.GetSetting;
import ontology.settings.SettingsOntology;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                    changeAction();
                    break;
                case SAVE_MESSAGE_LOGS:
                    saveMessageLogs();
                    break;
                case CLEAR_MESSAGE_LOGS:
                    clearMessagesLog();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.exception(this, e);
        }
    }

    private void changeAction() {
        if (requesterGui.getSelectedAction().equals(RequesterGuiAction.GET_ALL_SETTINGS)) {
            requesterGui.setGetAllSettingsActionComponents();
        } else if (requesterGui.getSelectedAction().equals(RequesterGuiAction.GET_SETTING)) {
            requesterGui.setGetSettingActionComponents();
        }
    }

    private void saveMessageLogs() throws IOException {
        String fileNameFormat = "messages%s.log";
        File file = new File(String.format(fileNameFormat, ""));

        int sequence = 1;
        while (file.exists()) {
            file = new File(String.format(fileNameFormat, sequence));
            sequence++;
        }

        Files.write(Paths.get(file.getPath()), requesterGui.getMessagesLog().getBytes());
    }

    private void clearMessagesLog() {
        requesterGui.clearMessagesLog();
    }

    private void sendMessage() {
        AgentAction agentAction = null;
        Ontology ontology = null;

        if (requesterGui.getSelectedAction().equals(RequesterGuiAction.GET_ALL_SETTINGS)) {
            agentAction = new GetAllSettings();
            ontology = SettingsOntology.getInstance();
        } else if (requesterGui.getSelectedAction().equals(RequesterGuiAction.GET_SETTING)) {
            agentAction = new GetSetting(requesterGui.getKeySetting());
            ontology = SettingsOntology.getInstance();
        }

        AID receiver = getAID(requesterGui.getReceiverAgentName());
        OntologyAssistant ontologyAssistant = new OntologyAssistant(this, ontology);
        ACLMessage requestAction = ontologyAssistant.createRequestAction(receiver, agentAction);
        requesterGui.logMessage(requestAction);
        send(requestAction);
    }

}
