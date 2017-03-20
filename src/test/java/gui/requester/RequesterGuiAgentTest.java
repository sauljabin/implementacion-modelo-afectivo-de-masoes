/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import agent.AgentLogger;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Deregister;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Register;
import jade.domain.FIPAAgentManagement.Search;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import language.SemanticLanguage;
import agent.configurable.ontology.AddBehaviour;
import agent.configurable.ontology.ConfigurableOntology;
import agent.configurable.ontology.RemoveBehaviour;
import masoes.ontology.MasoesOntology;
import masoes.ontology.data.CreateObject;
import masoes.ontology.data.DeleteObject;
import masoes.ontology.data.GetObject;
import masoes.ontology.data.ObjectEnvironment;
import masoes.ontology.data.ObjectProperty;
import masoes.ontology.data.UpdateObject;
import masoes.ontology.notifier.NotifyAction;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.stimulus.ActionStimulus;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.ObjectStimulus;
import settings.ontology.GetAllSettings;
import settings.ontology.GetSetting;
import settings.ontology.SettingsOntology;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import test.PowerMockitoTest;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class RequesterGuiAgentTest extends PowerMockitoTest {

    private static final String RECEIVER_AGENT_NAME = "receiverAgentName";
    private static final String SENDER_AGENT_NAME = "senderAgentName";
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private RequesterGuiAgent requesterGuiAgent;
    private RequesterGuiAgent requesterGuiAgentSpy;
    private RequesterGui requesterGuiMock;
    private AID senderAgentAID;
    private AID receiverAgentAID;
    private AgentLogger logger;

    @Before
    public void setUp() throws Exception {
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);

        requesterGuiMock = mock(RequesterGui.class);
        logger = mock(AgentLogger.class);

        requesterGuiAgent = new RequesterGuiAgent();
        setFieldValue(requesterGuiAgent, "requesterGui", requesterGuiMock);
        setFieldValue(requesterGuiAgent, "logger", logger);

        requesterGuiAgentSpy = spy(requesterGuiAgent);

        senderAgentAID = new AID(SENDER_AGENT_NAME, AID.ISGUID);
        receiverAgentAID = new AID(RECEIVER_AGENT_NAME, AID.ISGUID);
        doReturn(senderAgentAID).when(requesterGuiAgentSpy).getAID();
        doReturn(receiverAgentAID).when(requesterGuiAgentSpy).getAID(RECEIVER_AGENT_NAME);

        doReturn(RECEIVER_AGENT_NAME).when(requesterGuiMock).getReceiverAgentName();
    }

    @Test
    public void shouldInvokeSetupAndShowGuiWhenStartAgent() {
        requesterGuiAgent.setup();
        verify(requesterGuiMock).showGui();
    }

    @Test
    public void shouldAddResponseHandlerBehaviour() {
        requesterGuiAgentSpy.setup();
        verify(requesterGuiAgentSpy).addBehaviour(isA(RequesterGuiResponseHandlerBehaviour.class));
    }

    @Test
    public void shouldInvokeCloseGuiWhenDeleteAgent() {
        requesterGuiAgent.takeDown();
        verify(requesterGuiMock).closeGui();
    }

    @Test
    public void shouldInvokeAgentDoDeleteOnEventClose() {
        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.CLOSE_WINDOW.getInt());
        doNothing().when(requesterGuiAgentSpy).doDelete();
        requesterGuiAgentSpy.onGuiEvent(guiEvent);
        verify(requesterGuiAgentSpy).doDelete();
    }

    @Test
    public void shouldSetSenderAgentName() {
        String expectedName = "expectedName";
        doReturn(expectedName).when(requesterGuiAgentSpy).getLocalName();
        requesterGuiAgentSpy.setup();
        verify(requesterGuiMock).setSenderAgentName(expectedName);
    }

    @Test
    public void shouldLogExceptionWhenErrorInEventMethod() {
        RuntimeException expectedException = new RuntimeException("message");
        doThrow(expectedException).when(requesterGuiMock).logMessage(any(ACLMessage.class));
        doReturn(RequesterGuiAction.GET_ALL_SETTINGS).when(requesterGuiMock).getSelectedAction();

        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.SEND_MESSAGE.getInt());
        requesterGuiAgentSpy.onGuiEvent(guiEvent);
        verify(logger).exception(expectedException);
    }

    @Test
    public void shouldClearMessageLogsInWindows() {
        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.CLEAR_MESSAGE_LOGS.getInt());
        requesterGuiAgentSpy.onGuiEvent(guiEvent);
        verify(requesterGuiMock).clearMessagesLog();
    }

    @Test
    public void shouldSendSimpleContentToAgent() {
        String expectedContent = "expectedContent";
        doReturn(expectedContent).when(requesterGuiMock).getSimpleContent();
        doReturn(RequesterGuiAction.SEND_SIMPLE_CONTENT).when(requesterGuiMock).getSelectedAction();

        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.SEND_MESSAGE.getInt());
        requesterGuiAgentSpy.onGuiEvent(guiEvent);

        verify(requesterGuiMock).logMessage(messageArgumentCaptor.capture());
        verify(requesterGuiAgentSpy).send(messageArgumentCaptor.getValue());

        ACLMessage message = messageArgumentCaptor.getValue();
        assertThat(message.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(message.getReplyWith(), is(notNullValue()));
        assertThat(message.getConversationId(), is(notNullValue()));
        assertThat(message.getSender(), is(senderAgentAID));
        assertThat(message.getAllReceiver().next(), is(receiverAgentAID));
        assertThat(message.getContent(), is(expectedContent));
    }

    @Test
    public void shouldSendGetAllSettingsToAgent() throws Exception {
        Action action = testRequestAction(SettingsOntology.getInstance(), RequesterGuiAction.GET_ALL_SETTINGS);
        assertThat(action.getAction(), is(instanceOf(GetAllSettings.class)));
    }

    @Test
    public void shouldSendGetASettingToAgent() throws Exception {
        String expectedKey = "expectedSetting";
        doReturn(expectedKey).when(requesterGuiMock).getKeySetting();

        Action action = testRequestAction(SettingsOntology.getInstance(), RequesterGuiAction.GET_SETTING);
        assertThat(action.getAction(), is(instanceOf(GetSetting.class)));

        GetSetting getSetting = (GetSetting) action.getAction();
        assertThat(getSetting.getKey(), is(expectedKey));
    }

    @Test
    public void shouldSendGetEmotionalStateToAgent() throws Exception {
        Action action = testRequestAction(MasoesOntology.getInstance(), RequesterGuiAction.GET_EMOTIONAL_STATE);
        assertThat(action.getAction(), is(instanceOf(GetEmotionalState.class)));
    }

    @Test
    public void shouldSendEvaluateActionStimulus() throws Exception {
        String expectedActorName = "expectedActorName";
        String expectedActionName = "expectedActionName";
        doReturn(expectedActorName).when(requesterGuiMock).getActorName();
        doReturn(expectedActionName).when(requesterGuiMock).getActionName();
        doReturn(new AID(expectedActorName, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedActorName);

        Action action = testRequestAction(MasoesOntology.getInstance(), RequesterGuiAction.EVALUATE_ACTION_STIMULUS);
        assertThat(action.getAction(), is(instanceOf(EvaluateStimulus.class)));

        EvaluateStimulus evaluateStimulus = (EvaluateStimulus) action.getAction();

        ActionStimulus actionStimulus = (ActionStimulus) evaluateStimulus.getStimulus();
        assertThat(actionStimulus.getActor().getName(), is(expectedActorName));
        assertThat(actionStimulus.getActionName(), is(expectedActionName));
    }

    @Test
    public void shouldSendEvaluateObjectStimulus() throws Exception {
        String expectedCreatorName = "expectedCreatorName";
        String expectedObjectName = "expectedObjectName";
        String expectedProperties = "color=blue\ntype=car";
        doReturn(expectedCreatorName).when(requesterGuiMock).getCreatorName();
        doReturn(expectedObjectName).when(requesterGuiMock).getObjectName();
        doReturn(expectedProperties).when(requesterGuiMock).getObjectProperties();
        doReturn(new AID(expectedCreatorName, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedCreatorName);

        Action action = testRequestAction(MasoesOntology.getInstance(), RequesterGuiAction.EVALUATE_OBJECT_STIMULUS);
        assertThat(action.getAction(), is(instanceOf(EvaluateStimulus.class)));

        EvaluateStimulus evaluateStimulus = (EvaluateStimulus) action.getAction();

        ObjectStimulus objectStimulus = (ObjectStimulus) evaluateStimulus.getStimulus();
        assertThat(objectStimulus.getCreator().getLocalName(), is(expectedCreatorName));
        assertThat(objectStimulus.getObjectName(), is(expectedObjectName));
    }

    @Test
    public void shouldSendCreateObject() throws Exception {
        String expectedCreatorName = "expectedCreatorName";
        String expectedObjectName = "expectedObjectName";
        String expectedProperties = "color=blue\ntype=car";
        doReturn(expectedCreatorName).when(requesterGuiMock).getCreatorName();
        doReturn(expectedObjectName).when(requesterGuiMock).getObjectName();
        doReturn(expectedProperties).when(requesterGuiMock).getObjectProperties();
        doReturn(new AID(expectedCreatorName, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedCreatorName);

        Action action = testRequestAction(MasoesOntology.getInstance(), RequesterGuiAction.CREATE_OBJECT);
        assertThat(action.getAction(), is(instanceOf(CreateObject.class)));

        CreateObject createObject = (CreateObject) action.getAction();

        ObjectEnvironment objectEnvironment = createObject.getObjectEnvironment();
        assertThat(objectEnvironment.getCreator().getLocalName(), is(expectedCreatorName));
        assertThat(objectEnvironment.getName(), is(expectedObjectName));
        ObjectProperty propertyColor = (ObjectProperty) objectEnvironment.getObjectProperties().get(0);
        assertThat(propertyColor.getName(), is("color"));
        assertThat(propertyColor.getValue(), is("blue"));

        ObjectProperty propertyType = (ObjectProperty) objectEnvironment.getObjectProperties().get(1);
        assertThat(propertyType.getName(), is("type"));
        assertThat(propertyType.getValue(), is("car"));
    }

    @Test
    public void shouldSendUpdateObject() throws Exception {
        String expectedCreatorName = "expectedCreatorName";
        String expectedObjectName = "expectedObjectName";
        String expectedProperties = "color=blue\ntype=car";
        doReturn(expectedCreatorName).when(requesterGuiMock).getCreatorName();
        doReturn(expectedObjectName).when(requesterGuiMock).getObjectName();
        doReturn(expectedProperties).when(requesterGuiMock).getObjectProperties();
        doReturn(new AID(expectedCreatorName, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedCreatorName);

        Action action = testRequestAction(MasoesOntology.getInstance(), RequesterGuiAction.UPDATE_OBJECT);
        assertThat(action.getAction(), is(instanceOf(UpdateObject.class)));

        UpdateObject updateObject = (UpdateObject) action.getAction();

        ObjectEnvironment objectEnvironment = updateObject.getObjectEnvironment();
        assertThat(objectEnvironment.getCreator().getLocalName(), is(expectedCreatorName));
        assertThat(objectEnvironment.getName(), is(expectedObjectName));
        ObjectProperty propertyColor = (ObjectProperty) objectEnvironment.getObjectProperties().get(0);
        assertThat(propertyColor.getName(), is("color"));
        assertThat(propertyColor.getValue(), is("blue"));

        ObjectProperty propertyType = (ObjectProperty) objectEnvironment.getObjectProperties().get(1);
        assertThat(propertyType.getName(), is("type"));
        assertThat(propertyType.getValue(), is("car"));
    }

    @Test
    public void shouldSendDeleteObject() throws Exception {
        String expectedCreatorName = "expectedCreatorName";
        String expectedObjectName = "expectedObjectName";
        doReturn(expectedCreatorName).when(requesterGuiMock).getCreatorName();
        doReturn(expectedObjectName).when(requesterGuiMock).getObjectName();
        doReturn(new AID(expectedCreatorName, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedCreatorName);

        Action action = testRequestAction(MasoesOntology.getInstance(), RequesterGuiAction.DELETE_OBJECT);
        assertThat(action.getAction(), is(instanceOf(DeleteObject.class)));

        DeleteObject deleteObject = (DeleteObject) action.getAction();

        ObjectEnvironment objectEnvironment = deleteObject.getObjectEnvironment();
        assertThat(objectEnvironment.getCreator().getLocalName(), is(expectedCreatorName));
        assertThat(objectEnvironment.getName(), is(expectedObjectName));
    }

    @Test
    public void shouldSendGetObject() throws Exception {
        String expectedCreatorName = "expectedCreatorName";
        String expectedObjectName = "expectedObjectName";
        doReturn(expectedCreatorName).when(requesterGuiMock).getCreatorName();
        doReturn(expectedObjectName).when(requesterGuiMock).getObjectName();
        doReturn(new AID(expectedCreatorName, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedCreatorName);

        Action action = testRequestAction(MasoesOntology.getInstance(), RequesterGuiAction.GET_OBJECT);
        assertThat(action.getAction(), is(instanceOf(GetObject.class)));

        GetObject getObject = (GetObject) action.getAction();

        ObjectEnvironment objectEnvironment = getObject.getObjectEnvironment();
        assertThat(objectEnvironment.getCreator().getLocalName(), is(expectedCreatorName));
        assertThat(objectEnvironment.getName(), is(expectedObjectName));
    }

    @Test
    public void shouldSendAddBehaviourToAgent() throws Exception {
        String expectedBehaviourClassName = "expectedBehaviourClassName";
        String expectedBehaviourName = "expectedBehaviourName";

        doReturn(expectedBehaviourName).when(requesterGuiMock).getBehaviourName();
        doReturn(expectedBehaviourClassName).when(requesterGuiMock).getBehaviourClassName();

        Action action = testRequestAction(ConfigurableOntology.getInstance(), RequesterGuiAction.ADD_BEHAVIOUR);
        assertThat(action.getAction(), is(instanceOf(AddBehaviour.class)));

        AddBehaviour addBehaviour = (AddBehaviour) action.getAction();
        assertThat(addBehaviour.getClassName(), is(expectedBehaviourClassName));
        assertThat(addBehaviour.getName(), is(expectedBehaviourName));
    }

    @Test
    public void shouldSendRemoveBehaviourToAgent() throws Exception {
        String expectedBehaviourName = "expectedBehaviourName";
        doReturn(expectedBehaviourName).when(requesterGuiMock).getBehaviourName();

        Action action = testRequestAction(ConfigurableOntology.getInstance(), RequesterGuiAction.REMOVE_BEHAVIOUR);
        assertThat(action.getAction(), is(instanceOf(RemoveBehaviour.class)));

        RemoveBehaviour removeBehaviour = (RemoveBehaviour) action.getAction();
        assertThat(removeBehaviour.getName(), is(expectedBehaviourName));
    }

    @Test
    public void shouldSendNotifyAction() throws Exception {
        String expectedActor = "expectedActor";
        String expectedActionName = "expectedActionName";
        doReturn(new AID(expectedActor, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedActor);
        doReturn(expectedActor).when(requesterGuiMock).getActorName();
        doReturn(expectedActionName).when(requesterGuiMock).getActionName();

        Action action = testRequestAction(MasoesOntology.getInstance(), RequesterGuiAction.NOTIFY_ACTION);
        assertThat(action.getAction(), is(instanceOf(NotifyAction.class)));

        NotifyAction notifyAction = (NotifyAction) action.getAction();
        assertThat(notifyAction.getActionStimulus().getActor().getName(), is(expectedActor));
        assertThat(notifyAction.getActionStimulus().getActionName(), is(expectedActionName));
    }

    @Test
    public void shouldSendGetServices() throws Exception {
        String expectedAgentName = "expectedAgentName";
        doReturn(new AID(expectedAgentName, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedAgentName);
        doReturn(expectedAgentName).when(requesterGuiMock).getAgentName();

        Action action = testRequestAction(FIPAManagementOntology.getInstance(), RequesterGuiAction.GET_SERVICES);
        assertThat(action.getAction(), is(instanceOf(Search.class)));

        Search notifyAction = (Search) action.getAction();
        assertThat(notifyAction.getDescription(), is(instanceOf(DFAgentDescription.class)));

        DFAgentDescription description = (DFAgentDescription) notifyAction.getDescription();
        assertThat(description.getName().getLocalName(), is(expectedAgentName));
    }

    @Test
    public void shouldSendGetAgents() throws Exception {
        Action action = testRequestAction(FIPAManagementOntology.getInstance(), RequesterGuiAction.GET_AGENTS);
        assertThat(action.getAction(), is(instanceOf(Search.class)));

        Search notifyAction = (Search) action.getAction();
        assertThat(notifyAction.getDescription(), is(instanceOf(AMSAgentDescription.class)));
    }

    @Test
    public void shouldSendSearchAgent() throws Exception {
        String expectedServiceName = "expectedServiceName";
        doReturn(expectedServiceName).when(requesterGuiMock).getServiceName();

        Action action = testRequestAction(FIPAManagementOntology.getInstance(), RequesterGuiAction.SEARCH_AGENT);
        assertThat(action.getAction(), is(instanceOf(Search.class)));

        Search notifyAction = (Search) action.getAction();
        assertThat(notifyAction.getDescription(), is(instanceOf(DFAgentDescription.class)));

        DFAgentDescription description = (DFAgentDescription) notifyAction.getDescription();
        ServiceDescription serviceDescriptor = (ServiceDescription) description.getAllServices().next();
        assertThat(serviceDescriptor.getName(), is(expectedServiceName));
    }

    @Test
    public void shouldSendDeregisterAgent() throws Exception {
        String expectedAgentName = "expectedAgentName";
        doReturn(new AID(expectedAgentName, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedAgentName);
        doReturn(expectedAgentName).when(requesterGuiMock).getAgentName();

        Action action = testRequestAction(FIPAManagementOntology.getInstance(), RequesterGuiAction.DEREGISTER_AGENT);
        assertThat(action.getAction(), is(instanceOf(Deregister.class)));

        Deregister deregister = (Deregister) action.getAction();
        assertThat(deregister.getDescription(), is(instanceOf(DFAgentDescription.class)));

        DFAgentDescription description = (DFAgentDescription) deregister.getDescription();
        assertThat(description.getName().getLocalName(), is(expectedAgentName));
    }

    @Test
    public void shouldSendRegisterAgent() throws Exception {
        String expectedServiceName = "expectedServiceName";
        String expectedAgentName = "expectedAgentName";
        doReturn(expectedServiceName).when(requesterGuiMock).getServiceName();
        doReturn(expectedAgentName).when(requesterGuiMock).getAgentName();
        doReturn(new AID(expectedAgentName, AID.ISGUID)).when(requesterGuiAgentSpy).getAID(expectedAgentName);

        Action action = testRequestAction(FIPAManagementOntology.getInstance(), RequesterGuiAction.REGISTER_AGENT);
        assertThat(action.getAction(), is(instanceOf(Register.class)));

        Register register = (Register) action.getAction();
        assertThat(register.getDescription(), is(instanceOf(DFAgentDescription.class)));

        DFAgentDescription description = (DFAgentDescription) register.getDescription();
        assertThat(description.getName(), is(expectedAgentName));

        ServiceDescription serviceDescriptor = (ServiceDescription) description.getAllServices().next();
        assertThat(serviceDescriptor.getName(), is(expectedServiceName));
        assertThat(serviceDescriptor.getType(), is(expectedServiceName));
    }

    private Action testRequestAction(Ontology ontology, RequesterGuiAction guiAction) throws Exception {
        doReturn(guiAction).when(requesterGuiMock).getSelectedAction();

        GuiEvent guiEvent = new GuiEvent(requesterGuiMock, RequesterGuiEvent.SEND_MESSAGE.getInt());
        requesterGuiAgentSpy.onGuiEvent(guiEvent);

        SemanticLanguage language = SemanticLanguage.getInstance();

        verify(requesterGuiMock).logMessage(messageArgumentCaptor.capture());
        verify(requesterGuiAgentSpy).send(messageArgumentCaptor.getValue());

        ACLMessage message = messageArgumentCaptor.getValue();

        ContentManager contentManager = new ContentManager();
        contentManager.registerOntology(ontology);
        contentManager.registerLanguage(language);

        assertThat(message.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(message.getOntology(), is(ontology.getName()));
        assertThat(message.getLanguage(), is(language.getName()));
        assertThat(message.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(message.getReplyWith(), is(notNullValue()));
        assertThat(message.getConversationId(), is(notNullValue()));
        assertThat(message.getSender(), is(senderAgentAID));
        assertThat(message.getAllReceiver().next(), is(receiverAgentAID));

        ContentElement contentElement = contentManager.extractContent(message);
        assertThat(contentElement, is(instanceOf(Action.class)));
        return (Action) contentElement;
    }

}