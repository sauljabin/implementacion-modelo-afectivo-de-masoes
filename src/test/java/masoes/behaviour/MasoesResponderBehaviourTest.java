/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.behaviour;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import masoes.ontology.MasoesOntology;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class MasoesResponderBehaviourTest {

    private ArgumentCaptor<MessageTemplate> messageTemplateArgumentCaptor;
    private Agent agentMock;
    private MasoesResponderBehaviour masoesResponderBehaviourSpy;

    @Before
    public void setUp() throws Exception {
        messageTemplateArgumentCaptor = ArgumentCaptor.forClass(MessageTemplate.class);
        agentMock = mock(Agent.class);
        masoesResponderBehaviourSpy = spy(new MasoesResponderBehaviour(agentMock));
    }

    @Test
    public void shouldSetCorrectMessageTemplate() {
        MessageTemplate expectedTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchLanguage(FIPANames.ContentLanguage.FIPA_SL));
        expectedTemplate = MessageTemplate.and(expectedTemplate, MessageTemplate.MatchOntology(MasoesOntology.ONTOLOGY_NAME));
        masoesResponderBehaviourSpy.onStart();
        verify(masoesResponderBehaviourSpy).setMessageTemplate(messageTemplateArgumentCaptor.capture());
        assertReflectionEquals(expectedTemplate, messageTemplateArgumentCaptor.getValue());
    }

}