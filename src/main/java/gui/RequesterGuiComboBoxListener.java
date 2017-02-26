/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

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
            case EVALUATE_STIMULUS:
                requesterGui.setEvaluateStimulusActionComponents();
                break;
            case SEND_SIMPLE_CONTENT:
                requesterGui.setSimpleContentActionComponents();
                break;
            case NOTIFY_ACTION:
                requesterGui.setNotifyActionComponents();
                break;
        }

    }

}