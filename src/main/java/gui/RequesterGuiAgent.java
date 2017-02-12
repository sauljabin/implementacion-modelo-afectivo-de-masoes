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
import ontology.configurable.AddBehaviour;
import ontology.configurable.ConfigurableOntology;
import ontology.configurable.RemoveBehaviour;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetEmotionalState;
import ontology.masoes.MasoesOntology;
import ontology.masoes.Stimulus;
import ontology.settings.GetAllSettings;
import ontology.settings.GetSetting;
import ontology.settings.SettingsOntology;
import org.slf4j.LoggerFactory;
import util.MessageBuilder;

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
                case SAVE_MESSAGE_LOGS:
                    saveMessageLogs();
                    break;
                case CLEAR_MESSAGE_LOGS:
                    clearMessagesLog();
                    break;
            }
        } catch (Exception e) {
            logger.exception(this, e);
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
        AID aid = getAID(requesterGui.getReceiverAgentName());

        switch (requesterGui.getSelectedAction()) {
            case GET_ALL_SETTINGS:
                sendOntologyMessage(aid, SettingsOntology.getInstance(), new GetAllSettings());
                break;
            case GET_SETTING:
                sendOntologyMessage(aid, SettingsOntology.getInstance(), new GetSetting(requesterGui.getKeySetting()));
                break;
            case ADD_BEHAVIOUR:
                AddBehaviour addBehaviour = new AddBehaviour(requesterGui.getBehaviourName(), requesterGui.getBehaviourClassName());
                sendOntologyMessage(aid, ConfigurableOntology.getInstance(), addBehaviour);
                break;
            case REMOVE_BEHAVIOUR:
                RemoveBehaviour removeBehaviour = new RemoveBehaviour(requesterGui.getBehaviourName());
                sendOntologyMessage(aid, ConfigurableOntology.getInstance(), removeBehaviour);
                break;
            case GET_EMOTIONAL_STATE:
                sendOntologyMessage(aid, MasoesOntology.getInstance(), new GetEmotionalState());
                break;
            case EVALUATE_STIMULUS:
                Stimulus stimulus = new Stimulus();
                stimulus.setActor(getAID(requesterGui.getActorName()));
                stimulus.setActionName(requesterGui.getActionName());
                sendOntologyMessage(aid, MasoesOntology.getInstance(), new EvaluateStimulus(stimulus));
                break;
            case SEND_SIMPLE_CONTENT:
                ACLMessage message = new MessageBuilder()
                        .request()
                        .conversationId()
                        .replyWith()
                        .sender(getAID())
                        .receiver(aid)
                        .content(requesterGui.getSimpleContent())
                        .build();
                sendMessage(message);
                break;
        }

    }

    private void sendOntologyMessage(AID receiver, Ontology ontology, AgentAction agentAction) {
        OntologyAssistant ontologyAssistant = new OntologyAssistant(this, ontology);
        ACLMessage requestAction = ontologyAssistant.createRequestAction(receiver, agentAction);
        sendMessage(requestAction);
    }

    private void sendMessage(ACLMessage requestAction) {
        requesterGui.logMessage(requestAction);
        send(requestAction);
    }

}
