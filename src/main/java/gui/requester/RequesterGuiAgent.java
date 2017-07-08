/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import agent.AgentException;
import agent.AgentLogger;
import agent.configurable.ontology.AddBehaviour;
import agent.configurable.ontology.ConfigurableOntology;
import agent.configurable.ontology.RemoveBehaviour;
import gui.GuiException;
import jade.JadeSettings;
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
import jade.util.leap.ArrayList;
import masoes.ontology.MasoesOntology;
import masoes.ontology.knowledge.CreateObject;
import masoes.ontology.knowledge.DeleteObject;
import masoes.ontology.knowledge.GetObject;
import masoes.ontology.knowledge.ObjectEnvironment;
import masoes.ontology.knowledge.ObjectProperty;
import masoes.ontology.knowledge.UpdateObject;
import masoes.ontology.notifier.NotifyAction;
import masoes.ontology.notifier.NotifyEvent;
import masoes.ontology.notifier.NotifyObject;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.state.collective.GetSocialEmotion;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.EventStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import ontology.OntologyAssistant;
import settings.ontology.GetAllSettings;
import settings.ontology.GetSetting;
import settings.ontology.SettingsOntology;
import util.MessageBuilder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class RequesterGuiAgent extends GuiAgent {

    private AgentLogger logger;
    private RequesterGui requesterGui;
    private RequesterGuiListener requesterGuiListener;

    public RequesterGuiAgent() {
        requesterGui = new RequesterGui();
        requesterGuiListener = new RequesterGuiListener(this, requesterGui);
        logger = new AgentLogger(this);
    }

    @Override
    protected void setup() {
        if (!isGUIEnabled()) {
            throw new AgentException(getLocalName() + ": gui option is disabled");
        }

        addBehaviour(new RequesterGuiResponseHandlerBehaviour(this, requesterGui));
        requesterGui.setSenderAgentName(getLocalName());
        requesterGui.showGui();
    }

    private boolean isGUIEnabled() {
        return Boolean.parseBoolean(JadeSettings.getInstance().get(JadeSettings.GUI));
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
            logger.exception(e);
            requesterGui.showError(e.getMessage());
        }
    }

    private void saveMessageLogs() throws IOException {
        File folder = new File("output");
        folder.mkdir();

        String fileNameFormat = "output/messages%s.log";
        File file = new File(String.format(fileNameFormat, ""));

        int sequence = 1;
        while (file.exists()) {
            file = new File(String.format(fileNameFormat, sequence));
            sequence++;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(folder);
        fileChooser.setSelectedFile(file);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("LOG", "log"));
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            Files.write(Paths.get(fileChooser.getSelectedFile().getPath()), requesterGui.getMessagesLog().getBytes());
        }
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
                sendEvaluateActionStimulus(aid);
                break;
            case EVALUATE_OBJECT_STIMULUS:
                sendEvaluateObjectStimulus(aid);
                break;
            case EVALUATE_EVENT_STIMULUS:
                sendEvaluateEventStimulus(aid);
                break;
            case SEND_SIMPLE_CONTENT:
                sendSimpleContent(aid);
                break;
            case NOTIFY_ACTION:
                sendNotifyAction(aid);
                break;
            case NOTIFY_EVENT:
                sendNotifyEvent(aid);
                break;
            case NOTIFY_OBJECT:
                sendNotifyObject(aid);
                break;
            case GET_SERVICES:
                sendGetServices(aid);
                break;
            case GET_AGENTS:
                sendGetAgents(aid);
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
            case CREATE_OBJECT:
                sendCreateObject(aid);
                break;
            case UPDATE_OBJECT:
                sendUpdateObject(aid);
                break;
            case DELETE_OBJECT:
                sendDeleteObject(aid);
                break;
            case GET_OBJECT:
                sendGetObject(aid);
                break;
            case GET_SOCIAL_EMOTION:
                sendGetSocialEmotion(aid);
                break;
        }

    }

    private void sendGetObject(AID aid) {
        ObjectEnvironment objectEnvironment = new ObjectEnvironment();
        objectEnvironment.setCreator(getAID(requesterGui.getCreatorName()));
        objectEnvironment.setName(requesterGui.getObjectName());
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new GetObject(objectEnvironment));
    }

    private void sendDeleteObject(AID aid) {
        ObjectEnvironment objectEnvironment = new ObjectEnvironment();
        objectEnvironment.setCreator(getAID(requesterGui.getCreatorName()));
        objectEnvironment.setName(requesterGui.getObjectName());
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new DeleteObject(objectEnvironment));
    }

    private void sendUpdateObject(AID aid) {
        ObjectEnvironment objectEnvironment = new ObjectEnvironment();
        objectEnvironment.setCreator(getAID(requesterGui.getCreatorName()));
        objectEnvironment.setName(requesterGui.getObjectName());
        objectEnvironment.setObjectProperties(createPropertiesList());
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new UpdateObject(objectEnvironment));
    }

    private void sendCreateObject(AID aid) {
        ObjectEnvironment objectEnvironment = new ObjectEnvironment();
        objectEnvironment.setCreator(getAID(requesterGui.getCreatorName()));
        objectEnvironment.setName(requesterGui.getObjectName());
        objectEnvironment.setObjectProperties(createPropertiesList());
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new CreateObject(objectEnvironment));
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

    private void sendGetAgents(AID aid) {
        AMSAgentDescription amsAgentDescription = new AMSAgentDescription();
        SearchConstraints constraints = new SearchConstraints();
        constraints.setMaxResults(-1L);
        Search search = new Search();
        search.setDescription(amsAgentDescription);
        search.setConstraints(constraints);
        sendOntologyMessage(aid, FIPAManagementOntology.getInstance(), search);
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

    private void sendNotifyEvent(AID aid) {
        NotifyEvent notifyEvent = new NotifyEvent();
        EventStimulus eventStimulus = new EventStimulus();
        eventStimulus.setAffected(getAID(requesterGui.getAffectedName()));
        eventStimulus.setEventName(requesterGui.getEventName());
        notifyEvent.setEventStimulus(eventStimulus);
        sendOntologyMessage(aid, MasoesOntology.getInstance(), notifyEvent);
    }

    private void sendNotifyObject(AID aid) {
        NotifyObject notifyEvent = new NotifyObject();
        ObjectStimulus objectStimulus = new ObjectStimulus();
        objectStimulus.setCreator(getAID(requesterGui.getCreatorName()));
        objectStimulus.setObjectName(requesterGui.getObjectName());
        notifyEvent.setObjectStimulus(objectStimulus);
        sendOntologyMessage(aid, MasoesOntology.getInstance(), notifyEvent);
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

    private void sendEvaluateActionStimulus(AID aid) {
        ActionStimulus stimulus = new ActionStimulus();
        stimulus.setActor(getAID(requesterGui.getActorName()));
        stimulus.setActionName(requesterGui.getActionName());
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new EvaluateStimulus(stimulus));
    }

    private void sendEvaluateObjectStimulus(AID aid) {
        ObjectStimulus objectStimulus = new ObjectStimulus();
        objectStimulus.setCreator(getAID(requesterGui.getCreatorName()));
        objectStimulus.setObjectName(requesterGui.getObjectName());
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new EvaluateStimulus(objectStimulus));
    }

    private void sendEvaluateEventStimulus(AID aid) {
        EventStimulus eventStimulus = new EventStimulus();
        eventStimulus.setAffected(getAID(requesterGui.getAffectedName()));
        eventStimulus.setEventName(requesterGui.getEventName());
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new EvaluateStimulus(eventStimulus));
    }

    private ArrayList createPropertiesList() {
        ArrayList listProperties = new ArrayList();
        if (!requesterGui.getObjectProperties().isEmpty()) {
            try {
                Properties properties = new Properties();
                properties.load(new StringReader(requesterGui.getObjectProperties()));

                List<ObjectProperty> propertiesToList = properties.entrySet()
                        .stream()
                        .map(objectEntry -> new ObjectProperty(objectEntry.getKey().toString(), objectEntry.getValue().toString()))
                        .collect(Collectors.toList());

                listProperties.fromList(propertiesToList);
            } catch (Exception e) {
                throw new GuiException(e);
            }
        }
        return listProperties;
    }

    private void sendGetSocialEmotion(AID aid) {
        sendOntologyMessage(aid, MasoesOntology.getInstance(), new GetSocialEmotion());
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
