/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RequesterGuiComboBoxListener implements ActionListener {

    private RequesterGui requesterGui;

    public RequesterGuiComboBoxListener(RequesterGui requesterGui) {
        this.requesterGui = requesterGui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!RequesterGuiEvent.valueOf(e.getActionCommand()).equals(RequesterGuiEvent.CHANGE_ACTION)) {
            return;
        }

        switch (requesterGui.getSelectedAction()) {
            case GET_ALL_SETTINGS:
                requesterGui.setGetAllSettingsActionComponents();
                break;
            case GET_SETTING:
                requesterGui.setGetSettingActionComponents();
                break;
            case ADD_BEHAVIOUR:
                requesterGui.setAddBehaviourActionComponents();
                break;
            case REMOVE_BEHAVIOUR:
                requesterGui.setRemoveBehaviourActionComponents();
                break;
            case GET_EMOTIONAL_STATE:
                requesterGui.setGetEmotionalStateActionComponents();
                break;
            case EVALUATE_ACTION_STIMULUS:
                requesterGui.setEvaluateActionStimulusComponents();
                break;
            case EVALUATE_OBJECT_STIMULUS:
                requesterGui.setEvaluateObjectStimulusComponents();
                break;
            case EVALUATE_EVENT_STIMULUS:
                requesterGui.setEvaluateEventStimulusComponents();
                break;
            case SEND_SIMPLE_CONTENT:
                requesterGui.setSimpleContentActionComponents();
                break;
            case NOTIFY_ACTION:
                requesterGui.setNotifyActionComponents();
                break;
            case NOTIFY_EVENT:
                requesterGui.setNotifyEventComponents();
                break;
            case NOTIFY_OBJECT:
                requesterGui.setNotifyObjectComponents();
                break;
            case GET_SERVICES:
                requesterGui.setGetServicesActionComponents();
                break;
            case GET_AGENTS:
                requesterGui.setGetAgentsActionComponents();
                break;
            case SEARCH_AGENT:
                requesterGui.setSearchAgentActionComponents();
                break;
            case DEREGISTER_AGENT:
                requesterGui.setDeregisterAgentActionComponents();
                break;
            case REGISTER_AGENT:
                requesterGui.setRegisterAgentActionComponents();
                break;
            case CREATE_OBJECT:
                requesterGui.setCreateObjectComponents();
                break;
            case UPDATE_OBJECT:
                requesterGui.setUpdateObjectComponents();
                break;
            case DELETE_OBJECT:
                requesterGui.setDeleteObjectComponents();
                break;
            case GET_OBJECT:
                requesterGui.setGetObjectComponents();
                break;
            case GET_SOCIAL_EMOTION:
                requesterGui.setGetSocialEmotionActionComponents();
                break;
        }

    }

}
