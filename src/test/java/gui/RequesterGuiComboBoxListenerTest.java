/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

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
    public void shouldInvokeEvaluateStimulus() {
        prepareTest(RequesterGuiAction.EVALUATE_STIMULUS);
        verify(requesterGuiMock).setEvaluateStimulusActionComponents();
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

    private void prepareTest(RequesterGuiAction action) {
        doReturn(action).when(requesterGuiMock).getSelectedAction();
        ActionEvent actionEvent = new ActionEvent(requesterGuiMock, RequesterGuiEvent.CHANGE_ACTION.getInt(), RequesterGuiEvent.CHANGE_ACTION.toString());
        actionsComboBoxListener.actionPerformed(actionEvent);
    }

}