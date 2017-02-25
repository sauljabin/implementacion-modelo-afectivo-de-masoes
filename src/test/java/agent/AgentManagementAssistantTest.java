/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.Result;
import jade.content.onto.basic.TrueProposition;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.Register;
import jade.domain.FIPAAgentManagement.Search;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import language.SemanticLanguage;
import ontology.configurable.AddBehaviour;
import ontology.configurable.ConfigurableOntology;
import ontology.configurable.RemoveBehaviour;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import protocol.InvalidResponseException;
import util.MessageBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Agent.class)
public class AgentManagementAssistantTest {

    private static final String AMS_NAME = "ams";
    private static final String AGENT_NAME = "agentName";
    private static final String DF_NAME = "df";
    private static final String CONTAINER_NAME = "containerName";
    private static final String SERVICE_NAME = "serviceName";
    private static final String SERVICE_TYPE = "serviceType";
    private static final String ONTOLOGY_NAME = "ontologyName";
    private static final String OTHER_NAME = "otherName";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private AID amsAID = new AID(AMS_NAME, AID.ISGUID);
    private AID dfAID = new AID(DF_NAME, AID.ISGUID);
    private AID agentAID = new AID(AGENT_NAME, AID.ISGUID);
    private AID otherAID = new AID(OTHER_NAME, AID.ISGUID);
    private AgentManagementAssistant agentManagementAssistant;
    private Agent agentMock;
    private ACLMessage response;
    private ArgumentCaptor<ACLMessage> messageArgumentCaptor;
    private ArgumentCaptor<DFAgentDescription> dfDescriptorArgumentCaptor;
    private ServiceDescription serviceDescription;

    @Before
    public void setUp() {
        messageArgumentCaptor = ArgumentCaptor.forClass(ACLMessage.class);
        dfDescriptorArgumentCaptor = ArgumentCaptor.forClass(DFAgentDescription.class);

        agentMock = mock(Agent.class);
        doReturn(agentAID).when(agentMock).getAID();
        doReturn(amsAID).when(agentMock).getAMS();
        doReturn(dfAID).when(agentMock).getDefaultDF();
        doReturn(new ContainerID(CONTAINER_NAME, null)).when(agentMock).here();

        agentManagementAssistant = new AgentManagementAssistant(agentMock);

        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(JADEManagementOntology.getInstance())
                .content(new Done(new Action(agentAID, new ShutdownPlatform())))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());

        serviceDescription = new ServiceDescription();
        serviceDescription.setName(SERVICE_NAME);
        serviceDescription.setType(SERVICE_TYPE);
        serviceDescription.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
        serviceDescription.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
        serviceDescription.addOntologies(ONTOLOGY_NAME);
    }

    @Test
    public void shouldSendShutdownToAMSAgent() throws Exception {
        agentManagementAssistant.shutdownPlatform();
        ContentElement contentElement = testSendBasicMessage(AMS_NAME, JADEManagementOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(ShutdownPlatform.class)));
    }

    @Test
    public void shouldSendKillContainerToAMSAgent() throws Exception {
        agentManagementAssistant.killContainer();
        ContentElement contentElement = testSendBasicMessage(AMS_NAME, JADEManagementOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(KillContainer.class)));
        KillContainer killContainer = (KillContainer) action.getAction();
        assertThat(killContainer.getContainer().getName(), is(CONTAINER_NAME));
    }

    @Test
    public void shouldSendKillAgentToAMSAgent() throws Exception {
        agentManagementAssistant.killAgent(agentAID);
        ContentElement contentElement = testSendAndResponseBasicMessage(AMS_NAME, JADEManagementOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(KillAgent.class)));
        KillAgent killAgent = (KillAgent) action.getAction();
        assertThat(killAgent.getAgent().getName(), is(AGENT_NAME));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInKillAgent() throws Exception {
        prepareTestException(JADEManagementOntology.getInstance());
        agentManagementAssistant.killAgent(agentAID);
    }

    @Test
    public void shouldSendCreateAgent() throws Exception {
        String arg1 = "arg1";
        agentManagementAssistant.createAgent(AGENT_NAME, Agent.class, Arrays.asList(arg1));
        ContentElement contentElement = testSendAndResponseBasicMessage(AMS_NAME, JADEManagementOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(CreateAgent.class)));
        CreateAgent createAgent = (CreateAgent) action.getAction();
        assertThat(createAgent.getAgentName(), is(AGENT_NAME));
        assertThat(createAgent.getClassName(), is(Agent.class.getCanonicalName()));
        assertThat(createAgent.getAllArguments().next(), is(arg1));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInCreateAgent() throws Exception {
        prepareTestException(JADEManagementOntology.getInstance());
        agentManagementAssistant.createAgent("name", Agent.class, Arrays.asList("arg1"));
    }

    @Test
    public void shouldSendCreateAgentWithRandomName() throws Exception {
        String arg1 = "arg1";
        agentManagementAssistant.createAgent(Agent.class, Arrays.asList(arg1));
        ContentElement contentElement = testSendAndResponseBasicMessage(AMS_NAME, JADEManagementOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(CreateAgent.class)));
        CreateAgent createAgent = (CreateAgent) action.getAction();
        assertThat(createAgent.getAgentName().length(), is(30));
        assertThat(createAgent.getClassName(), is(Agent.class.getCanonicalName()));
        assertThat(createAgent.getAllArguments().next(), is(arg1));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInCreateAgentWithRandomName() throws Exception {
        prepareTestException(JADEManagementOntology.getInstance());
        agentManagementAssistant.createAgent(Agent.class, Arrays.asList("arg1"));
    }

    @Test
    public void shouldSendRemoveBehaviour() throws Exception {
        String behaviourName = "behaviourName";
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(ConfigurableOntology.getInstance())
                .content(new Done(new Action(amsAID, new RemoveBehaviour(behaviourName))))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        agentManagementAssistant.removeBehaviour(amsAID, behaviourName);
        ContentElement contentElement = testSendAndResponseBasicMessage(AMS_NAME, ConfigurableOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(RemoveBehaviour.class)));

        RemoveBehaviour removeBehaviour = (RemoveBehaviour) action.getAction();
        assertThat(removeBehaviour.getName(), is(behaviourName));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInRemoveBehaviour() throws Exception {
        prepareTestException(ConfigurableOntology.getInstance());
        agentManagementAssistant.removeBehaviour(amsAID, "");
    }

    @Test
    public void shouldSendAddBehaviour() throws Exception {
        String behaviourName = "behaviourName";
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(ConfigurableOntology.getInstance())
                .content(new Done(new Action(amsAID, new AddBehaviour(behaviourName, SimpleBehaviour.class.getCanonicalName()))))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        agentManagementAssistant.addBehaviour(amsAID, behaviourName, SimpleBehaviour.class);
        ContentElement contentElement = testSendAndResponseBasicMessage(AMS_NAME, ConfigurableOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(AddBehaviour.class)));

        AddBehaviour addBehaviour = (AddBehaviour) action.getAction();
        assertThat(addBehaviour.getName(), is(behaviourName));
        assertThat(addBehaviour.getClassName(), is(SimpleBehaviour.class.getCanonicalName()));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInAddBehavior() throws Exception {
        prepareTestException(ConfigurableOntology.getInstance());
        agentManagementAssistant.addBehaviour(amsAID, "name", SimpleBehaviour.class);
    }

    @Test
    public void shouldSendAddBehaviourWithRandomName() throws Exception {
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(ConfigurableOntology.getInstance())
                .content(new Done(new Action(amsAID, new AddBehaviour("", SimpleBehaviour.class.getCanonicalName()))))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        agentManagementAssistant.addBehaviour(amsAID, SimpleBehaviour.class);
        ContentElement contentElement = testSendAndResponseBasicMessage(AMS_NAME, ConfigurableOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(AddBehaviour.class)));

        AddBehaviour addBehaviour = (AddBehaviour) action.getAction();
        assertThat(addBehaviour.getName().length(), is(30));
        assertThat(addBehaviour.getClassName(), is(SimpleBehaviour.class.getCanonicalName()));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInAddBehaviorWithRandomName() throws Exception {
        prepareTestException(ConfigurableOntology.getInstance());
        agentManagementAssistant.addBehaviour(amsAID, SimpleBehaviour.class);
    }

    @Test
    public void shouldSendRegisterAgent() throws Exception {
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(FIPAManagementOntology.getInstance())
                .content(new Done(new Action(agentAID, new ShutdownPlatform())))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());

        agentManagementAssistant.register(new ServiceDescription());
        ContentElement contentElement = testSendAndResponseBasicMessage(DF_NAME, FIPAManagementOntology.getInstance());

        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(Register.class)));

        Register register = (Register) action.getAction();
        DFAgentDescription description = (DFAgentDescription) register.getDescription();
        assertThat(description.getName(), is(agentAID));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInRegisterAgent() throws Exception {
        prepareTestException(FIPAManagementOntology.getInstance());
        agentManagementAssistant.register(new ServiceDescription());
    }

    @Test
    public void shouldSendRegisterAgentWithName() throws Exception {
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(FIPAManagementOntology.getInstance())
                .content(new Done(new Action(agentAID, new ShutdownPlatform())))
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());

        agentManagementAssistant.register(otherAID, new ServiceDescription());
        ContentElement contentElement = testSendAndResponseBasicMessage(DF_NAME, FIPAManagementOntology.getInstance());

        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(Register.class)));

        Register register = (Register) action.getAction();
        DFAgentDescription description = (DFAgentDescription) register.getDescription();
        assertThat(description.getName(), is(otherAID));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInRegisterAgentWithName() throws Exception {
        prepareTestException(FIPAManagementOntology.getInstance());
        agentManagementAssistant.register(otherAID, new ServiceDescription());
    }

    @Test
    public void shouldSendSearchServicesAgent() throws Exception {
        SearchConstraints constraints = new SearchConstraints();
        constraints.setMaxResults(-1L);

        Search search = new Search();
        search.setDescription("description");
        search.setConstraints(constraints);

        Action actionResult = new Action(agentAID, search);

        ArrayList items = new ArrayList();
        DFAgentDescription description = new DFAgentDescription();
        description.setName(agentAID);

        ServiceDescription expectedServiceDescription = new ServiceDescription();
        description.addServices(expectedServiceDescription);
        String expectedServiceName = "expectedServiceName";
        expectedServiceDescription.setName(expectedServiceName);

        items.add(description);

        Result result = new Result();
        result.setAction(actionResult);
        result.setItems(items);

        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(FIPAManagementOntology.getInstance())
                .content(result)
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());

        List<ServiceDescription> services = agentManagementAssistant.services(agentAID);

        ContentElement contentElement = testSendAndResponseBasicMessage(DF_NAME, FIPAManagementOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(Search.class)));

        Search addBehaviour = (Search) action.getAction();
        assertThat(addBehaviour.getConstraints().getMaxResults(), is(-1L));

        assertThat(services, hasSize(1));
        assertThat(services.get(0).getName(), is(expectedServiceName));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInSearchServiceAgent() throws Exception {
        prepareTestException(FIPAManagementOntology.getInstance());
        agentManagementAssistant.services(agentAID);
    }

    @Test
    public void shouldSendSearchAgent() throws Exception {
        SearchConstraints constraints = new SearchConstraints();
        constraints.setMaxResults(-1L);

        Search search = new Search();
        search.setDescription("description");
        search.setConstraints(constraints);

        Action actionResult = new Action(agentAID, search);

        ArrayList items = new ArrayList();
        DFAgentDescription description = new DFAgentDescription();
        description.setName(agentAID);
        items.add(description);

        Result result = new Result();
        result.setAction(actionResult);
        result.setItems(items);

        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(FIPAManagementOntology.getInstance())
                .content(result)
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());

        List<AID> agents = agentManagementAssistant.search(new ServiceDescription());

        ContentElement contentElement = testSendAndResponseBasicMessage(DF_NAME, FIPAManagementOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(Search.class)));

        Search addBehaviour = (Search) action.getAction();
        assertThat(addBehaviour.getConstraints().getMaxResults(), is(-1L));

        assertThat(agents, hasSize(1));
        assertThat(agents.get(0), is(agentAID));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInSearchAgent() throws Exception {
        prepareTestException(FIPAManagementOntology.getInstance());
        agentManagementAssistant.search(new ServiceDescription());
    }

    @Test
    public void shouldSendSearchAgents() throws Exception {
        SearchConstraints constraints = new SearchConstraints();
        constraints.setMaxResults(-1L);

        Search search = new Search();
        search.setDescription("description");
        search.setConstraints(constraints);

        Action actionResult = new Action(agentAID, search);

        ArrayList items = new ArrayList();
        AMSAgentDescription amsAgentDescription1 = new AMSAgentDescription();
        amsAgentDescription1.setName(agentAID);
        items.add(amsAgentDescription1);

        AMSAgentDescription amsAgentDescription2 = new AMSAgentDescription();
        amsAgentDescription2.setName(amsAID);
        items.add(amsAgentDescription2);

        AMSAgentDescription amsAgentDescription3 = new AMSAgentDescription();
        amsAgentDescription3.setName(dfAID);
        items.add(amsAgentDescription3);

        Result result = new Result();
        result.setAction(actionResult);
        result.setItems(items);

        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(FIPAManagementOntology.getInstance())
                .content(result)
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());

        List<AID> agents = agentManagementAssistant.agents();

        ContentElement contentElement = testSendAndResponseBasicMessage(AMS_NAME, FIPAManagementOntology.getInstance());
        Action action = (Action) contentElement;
        assertThat(action.getAction(), is(instanceOf(Search.class)));

        Search addBehaviour = (Search) action.getAction();
        assertThat(addBehaviour.getConstraints().getMaxResults(), is(-1L));

        assertThat(agents, hasSize(1));
        assertThat(agents.get(0), is(agentAID));
    }

    @Test
    public void shouldThrowExceptionWhenIsNotDoneInSearchAgents() throws Exception {
        prepareTestException(FIPAManagementOntology.getInstance());
        agentManagementAssistant.agents();
    }

    private ContentElement testSendBasicMessage(String receiver, Ontology ontology) throws Exception {
        verify(agentMock).send(messageArgumentCaptor.capture());

        ACLMessage message = messageArgumentCaptor.getValue();

        assertThat(message.getPerformative(), is(ACLMessage.REQUEST));
        assertThat(message.getOntology(), is(ontology.getName()));
        assertThat(message.getLanguage(), is(SemanticLanguage.getInstance().getName()));
        assertThat(message.getProtocol(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
        assertThat(((AID) message.getAllReceiver().next()).getName(), is(receiver));
        assertThat(message.getSender().getName(), is(AGENT_NAME));

        ContentManager contentManager = new ContentManager();
        contentManager.registerLanguage(SemanticLanguage.getInstance());
        contentManager.registerOntology(ontology);

        ContentElement contentElement = contentManager.extractContent(message);
        assertThat(contentElement, is(instanceOf(Action.class)));
        return contentElement;
    }

    private ContentElement testSendAndResponseBasicMessage(String receiver, Ontology ontology) throws Exception {
        verify(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        return testSendBasicMessage(receiver, ontology);
    }

    private void prepareTestException(Ontology ontology) {
        response = new MessageBuilder()
                .performative(ACLMessage.INFORM)
                .fipaSL()
                .ontology(ontology)
                .content(new TrueProposition())
                .build();
        doReturn(response).when(agentMock).blockingReceive(any(MessageTemplate.class), anyLong());
        expectedException.expectMessage(containsString("Unknown notification: "));
        expectedException.expect(InvalidResponseException.class);
    }

}