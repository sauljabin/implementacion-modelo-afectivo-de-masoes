/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RequesterGuiComboBoxListenerTest {

    private RequesterGui requesterGuiMock;
    private RequesterGuiComboBoxListener actionsComboBoxListener;

    @Before
    public void setUp() {
        requesterGuiMock = mock(RequesterGui.class);
        actionsComboBoxListener = new RequesterGuiComboBoxListener(requesterGuiMock);
    }

    @Test
    public void shouldInvokeGelAllSettings() {
        prepareTest(RequesterGuiAction.GET_ALL_SETTINGS);
        verify(requesterGuiMock).setGetAllSettingsActionComponents();
    }

    @Test
    public void shouldInvokeGetSetting() {
        prepareTest(RequesterGuiAction.GET_SETTING);
        verify(requesterGuiMock).setGetSettingActionComponents();
    }

    @Test
    public void shouldInvokeAddBehaviour() {
        prepareTest(RequesterGuiAction.ADD_BEHAVIOUR);
        verify(requesterGuiMock).setAddBehaviourActionComponents();
    }

    @Test
    public void shouldInvokeRemoveBehaviour() {
        prepareTest(RequesterGuiAction.REMOVE_BEHAVIOUR);
        verify(requesterGuiMock).setRemoveBehaviourActionComponents();
    }

    @Test
    public void shouldInvokeGetState() {
        prepareTest(RequesterGuiAction.GET_EMOTIONAL_STATE);
        verify(requesterGuiMock).setGetEmotionalStateActionComponents();
    }

    @Test
    public void shouldInvokeEvaluateActionStimulus() {
        prepareTest(RequesterGuiAction.EVALUATE_ACTION_STIMULUS);
        verify(requesterGuiMock).setEvaluateActionStimulusComponents();
    }

    @Test
    public void shouldInvokeEvaluateObjectStimulus() {
        prepareTest(RequesterGuiAction.EVALUATE_OBJECT_STIMULUS);
        verify(requesterGuiMock).setEvaluateObjectStimulusComponents();
    }

    @Test
    public void shouldInvokeEvaluateEventStimulus() {
        prepareTest(RequesterGuiAction.EVALUATE_EVENT_STIMULUS);
        verify(requesterGuiMock).setEvaluateEventStimulusComponents();
    }

    @Test
    public void shouldInvokeSimpleContent() {
        prepareTest(RequesterGuiAction.SEND_SIMPLE_CONTENT);
        verify(requesterGuiMock).setSimpleContentActionComponents();
    }

    @Test
    public void shouldInvokeNotifyAction() {
        prepareTest(RequesterGuiAction.NOTIFY_ACTION);
        verify(requesterGuiMock).setNotifyActionComponents();
    }

    @Test
    public void shouldInvokeNotifyEvent() {
        prepareTest(RequesterGuiAction.NOTIFY_EVENT);
        verify(requesterGuiMock).setNotifyEventComponents();
    }

    @Test
    public void shouldInvokeNotifyObject() {
        prepareTest(RequesterGuiAction.NOTIFY_OBJECT);
        verify(requesterGuiMock).setNotifyObjectComponents();
    }

    @Test
    public void shouldInvokeGetServices() {
        prepareTest(RequesterGuiAction.GET_SERVICES);
        verify(requesterGuiMock).setGetServicesActionComponents();
    }

    @Test
    public void shouldInvokeGetAgents() {
        prepareTest(RequesterGuiAction.GET_AGENTS);
        verify(requesterGuiMock).setGetAgentsActionComponents();
    }

    @Test
    public void shouldInvokeSearchAgent() {
        prepareTest(RequesterGuiAction.SEARCH_AGENT);
        verify(requesterGuiMock).setSearchAgentActionComponents();
    }

    @Test
    public void shouldInvokeDeregisterAgent() {
        prepareTest(RequesterGuiAction.DEREGISTER_AGENT);
        verify(requesterGuiMock).setDeregisterAgentActionComponents();
    }

    @Test
    public void shouldInvokeRegisterAgent() {
        prepareTest(RequesterGuiAction.REGISTER_AGENT);
        verify(requesterGuiMock).setRegisterAgentActionComponents();
    }

    @Test
    public void shouldInvokeCreateObject() {
        prepareTest(RequesterGuiAction.CREATE_OBJECT);
        verify(requesterGuiMock).setCreateObjectComponents();
    }

    @Test
    public void shouldInvokeUpdateObject() {
        prepareTest(RequesterGuiAction.UPDATE_OBJECT);
        verify(requesterGuiMock).setUpdateObjectComponents();
    }

    @Test
    public void shouldInvokeDeleteObject() {
        prepareTest(RequesterGuiAction.DELETE_OBJECT);
        verify(requesterGuiMock).setDeleteObjectComponents();
    }

    @Test
    public void shouldInvokeGetObject() {
        prepareTest(RequesterGuiAction.GET_OBJECT);
        verify(requesterGuiMock).setGetObjectComponents();
    }

    @Test
    public void shouldInvokeGetSocialEmotion() {
        prepareTest(RequesterGuiAction.GET_SOCIAL_EMOTION);
        verify(requesterGuiMock).setGetSocialEmotionActionComponents();
    }

    private void prepareTest(RequesterGuiAction action) {
        doReturn(action).when(requesterGuiMock).getSelectedAction();
        ActionEvent actionEvent = new ActionEvent(requesterGuiMock, RequesterGuiEvent.CHANGE_ACTION.getInt(), RequesterGuiEvent.CHANGE_ACTION.toString());
        actionsComboBoxListener.actionPerformed(actionEvent);
    }

}