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
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Deregister;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Register;
import jade.domain.FIPAAgentManagement.Search;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ontology.OntologyAssistant;
import ontology.configurable.AddBehaviour;
import ontology.configurable.ConfigurableOntology;
import ontology.configurable.RemoveBehaviour;
import ontology.masoes.ActionStimulus;
import ontology.masoes.EvaluateStimulus;
import ontology.masoes.GetEmotionalState;
import ontology.masoes.MasoesOntology;
import ontology.masoes.NotifyAction;
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
                sendGetAllSettings(aid);
                break;
            case GET_SETTING:
                sendGetSetting(aid);
                break;
            case ADD_BEHAVIOUR:
                sendAddBehaviour(aid);
                break;
            case REMOVE_BEHAVIOUR:
                sendRemoveBehaviour(aid);
                break;
            case GET_EMOTIONAL_STATE:
                sendGetEmotionalState(aid);
                break;
            case EVALUATE_ACTION_STIMULUS:
                sendEvaluateStimulus(aid);
                break;
            case SEND_SIMPLE_CONTENT:
                sendSimpleContent(aid);
                break;
            case NOTIFY_ACTION:
                sendNotifyAction(aid);
                break;
            case GET_SERVICES:
                sendGetServices(aid);
                break;
            case GET_AGENTS:
                Search searchAgents = sendGetAgents();
                sendOntologyMessage(aid, FIPAManagementOntology.getInstance(), searchAgents);
                break;
            case SEARCH_AGENT:
                sendSearchAgent(aid);
                break;
            case DEREGISTER_AGENT:
                sendDeregisterAgent(aid);
                break;
            case REGISTER_AGENT:
                sendRegisterAgent(aid);
                break;
        }

    }

    private void sendRegisterAgent(AID aid) {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(requesterGui.getServiceName());
        serviceDescription.setType(requesterGui.getServiceName());

        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID(requesterGui.getAgentName()));
        dfAgentDescription.addServices(serviceDescription);

        Register register = new Register();
        register.setDescription(dfAgentDescription);

        sendOntologyMessage(aid, FIPAManagementOntology.getInstance(), register);
    }

    private void sendDeregisterAgent(AID aid) {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID(requesterGui.getAgentName()));
        Deregister deregister = new Deregister();
        deregister.setDescription(dfAgentDescription);
        sendOntologyMessage(aid, FIPAManagementOntology.getInstance(), deregister);
    }

    private void sendSearchAgent(AID aid) {
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(requesterGui.getServiceName());
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.addServices(serviceDescription);
        SearchConstraints constraints = new SearchConstraints();
        constraints.setMaxResults(-1L);
        Search search = new Search();
        search.setDescription(dfAgentDescription);
        search.setConstraints(constraints);
        sendOntologyMessage(aid, FIPAManagementOntology.getInstance(), search);
    }

    private Search sendGetAgents() {
        AMSAgentDescription amsAgentDescription = new AMSAgentDescription();
        SearchConstraints constraints = new SearchConstraints();
        constraints.setMaxResults(-1L);
        Search search = new Search();
        search.setDescription(amsAgentDescription);
        search.setConstraints(constraints);
        return search;
    }

    private void sendGetServices(AID aid) {
        DFAgentDescription dfAgentDescription = new DFAgentDescription();
        dfAgentDescription.setName(getAID(requesterGui.getAgentName()));
        SearchConstraints constraints = new SearchConstraints();
        constraints.setMaxResults(-1L);
        Search search = new Search();
        search.setDescription(dfAgentDescription);
        search.setConstraints(constraints);
        sendOntologyMessage(aid, FIPAManagementOntology.getInstance(), search);
    }

    private void sendNotifyAction(AID aid) {
        NotifyAction notifyAction = new NotifyAction();
        ActionStimulus actionStimulus = new ActionStimulus();
        actionStimulus.setActor(getAID(requesterGui.getActorName()));
        actionStimulus.setActionName(requesterGui.getActionName());
        notifyAction.setActionStimulus(actionStimulus);
        sendOntologyMessage(aid, MasoesOntology.getInstance(), notifyAction);
    }

    private void sendSimpleContent(AID aid) {
        ACLMessage message = new MessageBuilder()
                .request()
                .conversationId()
                .replyWith()
                .sender(getAID())
                .receiver(aid)
                .content(requesterGui.getSimpleContent())
                .build();
        sendMessage(message);
    }

    private void sendEvaluateStimulus(AID aid) {
        ActionStimulus stimulus = new ActionStimulus();
        stimulus.setActor(getAID(requesterGui.getActorName()));
        stimulus.setActionName(requesterGui.getActionName());
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new EvaluateStimulus(stimulus));
    }

    private void sendGetEmotionalState(AID aid) {
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new GetEmotionalState());
    }

    private void sendRemoveBehaviour(AID aid) {
        RemoveBehaviour removeBehaviour = new RemoveBehaviour(requesterGui.getBehaviourName());
        sendOntologyMessage(aid, ConfigurableOntology.getInstance(), removeBehaviour);
    }

    private void sendAddBehaviour(AID aid) {
        AddBehaviour addBehaviour = new AddBehaviour(requesterGui.getBehaviourName(), requesterGui.getBehaviourClassName());
        sendOntologyMessage(aid, ConfigurableOntology.getInstance(), addBehaviour);
    }

    private void sendGetSetting(AID aid) {
        sendOntologyMessage(aid, SettingsOntology.getInstance(), new GetSetting(requesterGui.getKeySetting()));
    }

    private void sendGetAllSettings(AID aid) {
        sendOntologyMessage(aid, SettingsOntology.getInstance(), new GetAllSettings());
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
