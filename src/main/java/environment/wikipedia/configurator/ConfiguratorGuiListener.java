/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import gui.WindowsEventsAdapter;
import jade.gui.GuiEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;

public class ConfiguratorGuiListener extends WindowsEventsAdapter {

    private ConfiguratorGui view;
    private ConfiguratorGuiAgent controller;

    public ConfiguratorGuiListener(ConfiguratorGui view, ConfiguratorGuiAgent controller) {
        this.view = view;
        this.controller = controller;

        view.addWindowListener(this);

        view.getAddStimulusButton().setActionCommand(ConfiguratorGuiEvent.ADD_STIMULUS.toString());
        view.getAddStimulusButton().addActionListener(this);

        view.getDeleteStimulusButton().setActionCommand(ConfiguratorGuiEvent.DELETE_STIMULUS.toString());
        view.getDeleteStimulusButton().addActionListener(this);

        view.getEditStimulusButton().setActionCommand(ConfiguratorGuiEvent.EDIT_STIMULUS.toString());
        view.getEditStimulusButton().addActionListener(this);

        view.getAddAgentButton().setActionCommand(ConfiguratorGuiEvent.ADD_AGENT.toString());
        view.getAddAgentButton().addActionListener(this);

        view.getDeleteAgentButton().setActionCommand(ConfiguratorGuiEvent.DELETE_AGENT.toString());
        view.getDeleteAgentButton().addActionListener(this);

        view.getEditAgentButton().setActionCommand(ConfiguratorGuiEvent.EDIT_AGENT.toString());
        view.getEditAgentButton().addActionListener(this);

        view.getPlayButton().setActionCommand(ConfiguratorGuiEvent.PLAY.toString());
        view.getPlayButton().addActionListener(this);

        view.getPauseButton().setActionCommand(ConfiguratorGuiEvent.PAUSE.toString());
        view.getPauseButton().addActionListener(this);

        view.getRefreshButton().setActionCommand(ConfiguratorGuiEvent.REFRESH.toString());
        view.getRefreshButton().addActionListener(this);

        view.getShowAgentStateButton().setActionCommand(ConfiguratorGuiEvent.SHOW_AGENT_STATE.toString());
        view.getShowAgentStateButton().addActionListener(this);

        view.getCentralEmotionCheckBox().addItemListener(this);
        view.getMaximumDistanceCheckBox().addItemListener(this);
        view.getEmotionalDispersionCheckBox().addItemListener(this);
        view.getBehavioursCheckBox().addItemListener(this);
        view.getEmotionsCheckBox().addItemListener(this);
        view.getEmotionalStatesCheckBox().addItemListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(controller, ConfiguratorGuiEvent.CLOSE_WINDOW.getInt());
        controller.postGuiEvent(guiEvent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiEvent guiEvent = new GuiEvent(controller, ConfiguratorGuiEvent.valueOf(e.getActionCommand()).getInt());
        controller.postGuiEvent(guiEvent);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        ConfiguratorGuiEvent event = null;

        if (e.getSource().equals(view.getCentralEmotionCheckBox())) {
            if (view.getCentralEmotionCheckBox().isSelected()) {
                event = ConfiguratorGuiEvent.SHOW_CENTRAL_EMOTION_CHART;
            } else {
                event = ConfiguratorGuiEvent.HIDE_CENTRAL_EMOTION_CHART;
            }
        } else if (e.getSource().equals(view.getMaximumDistanceCheckBox())) {
            if (view.getMaximumDistanceCheckBox().isSelected()) {
                event = ConfiguratorGuiEvent.SHOW_MAXIMUM_DISTANCE_CHART;
            } else {
                event = ConfiguratorGuiEvent.HIDE_MAXIMUM_DISTANCE_CHART;
            }
        } else if (e.getSource().equals(view.getEmotionalDispersionCheckBox())) {
            if (view.getEmotionalDispersionCheckBox().isSelected()) {
                event = ConfiguratorGuiEvent.SHOW_EMOTIONAL_DISPERSION_CHART;
            } else {
                event = ConfiguratorGuiEvent.HIDE_EMOTIONAL_DISPERSION_CHART;
            }
        } else if (e.getSource().equals(view.getBehavioursCheckBox())) {
            if (view.getBehavioursCheckBox().isSelected()) {
                event = ConfiguratorGuiEvent.SHOW_BEHAVIOUR_MODIFICATION_CHART;
            } else {
                event = ConfiguratorGuiEvent.HIDE_BEHAVIOUR_MODIFICATION_CHART;
            }
        } else if (e.getSource().equals(view.getEmotionsCheckBox())) {
            if (view.getEmotionsCheckBox().isSelected()) {
                event = ConfiguratorGuiEvent.SHOW_EMOTION_MODIFICATION_CHART;
            } else {
                event = ConfiguratorGuiEvent.HIDE_EMOTION_MODIFICATION_CHART;
            }
        }

        if (event != null) {
            GuiEvent guiEvent = new GuiEvent(controller, event.getInt());
            controller.postGuiEvent(guiEvent);
        }
    }

}
