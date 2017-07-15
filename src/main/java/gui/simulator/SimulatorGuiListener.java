/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator;

import gui.WindowsEventsAdapter;
import jade.gui.GuiEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;

public class SimulatorGuiListener extends WindowsEventsAdapter {

    private SimulatorGui view;
    private SimulatorGuiAgent controller;

    public SimulatorGuiListener(SimulatorGui view, SimulatorGuiAgent controller) {
        this.view = view;
        this.controller = controller;

        view.addWindowListener(this);

        view.getAddAgentButton().setActionCommand(SimulatorGuiEvent.ADD_AGENT.toString());
        view.getAddAgentButton().addActionListener(this);

        view.getDeleteAgentButton().setActionCommand(SimulatorGuiEvent.DELETE_AGENT.toString());
        view.getDeleteAgentButton().addActionListener(this);

        view.getEditAgentButton().setActionCommand(SimulatorGuiEvent.EDIT_AGENT.toString());
        view.getEditAgentButton().addActionListener(this);

        view.getPlayButton().setActionCommand(SimulatorGuiEvent.PLAY.toString());
        view.getPlayButton().addActionListener(this);

        view.getPauseButton().setActionCommand(SimulatorGuiEvent.PAUSE.toString());
        view.getPauseButton().addActionListener(this);

        view.getRefreshButton().setActionCommand(SimulatorGuiEvent.REFRESH.toString());
        view.getRefreshButton().addActionListener(this);

        view.getShowAgentStateButton().setActionCommand(SimulatorGuiEvent.SHOW_AGENT_STATE.toString());
        view.getShowAgentStateButton().addActionListener(this);

        view.getEditAgentTypesDefinitionMenu().setActionCommand(SimulatorGuiEvent.SHOW_AGENT_TYPE_DEFINITION_GUI.toString());
        view.getEditAgentTypesDefinitionMenu().addActionListener(this);

        view.getEditStimuliDefinitionMenu().setActionCommand(SimulatorGuiEvent.SHOW_STIMULUS_DEFINITION_GUI.toString());
        view.getEditStimuliDefinitionMenu().addActionListener(this);

        view.getImportConfigurationMenu().setActionCommand(SimulatorGuiEvent.IMPORT_CONFIGURATION.toString());
        view.getImportConfigurationMenu().addActionListener(this);

        view.getExportConfigurationMenu().setActionCommand(SimulatorGuiEvent.EXPORT_CONFIGURATION.toString());
        view.getExportConfigurationMenu().addActionListener(this);

        view.getCentralEmotionCheckBox().addItemListener(this);
        view.getMaximumDistanceCheckBox().addItemListener(this);
        view.getEmotionalDispersionCheckBox().addItemListener(this);
        view.getBehavioursCheckBox().addItemListener(this);
        view.getEmotionsCheckBox().addItemListener(this);
        view.getEmotionalStatesCheckBox().addItemListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(controller, SimulatorGuiEvent.CLOSE_WINDOW.getInt());
        controller.postGuiEvent(guiEvent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GuiEvent guiEvent = new GuiEvent(controller, SimulatorGuiEvent.valueOf(e.getActionCommand()).getInt());
        controller.postGuiEvent(guiEvent);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        SimulatorGuiEvent event = null;

        if (e.getSource().equals(view.getCentralEmotionCheckBox())) {
            if (view.getCentralEmotionCheckBox().isSelected()) {
                event = SimulatorGuiEvent.SHOW_CENTRAL_EMOTION_CHART;
            } else {
                event = SimulatorGuiEvent.HIDE_CENTRAL_EMOTION_CHART;
            }
        } else if (e.getSource().equals(view.getMaximumDistanceCheckBox())) {
            if (view.getMaximumDistanceCheckBox().isSelected()) {
                event = SimulatorGuiEvent.SHOW_MAXIMUM_DISTANCE_CHART;
            } else {
                event = SimulatorGuiEvent.HIDE_MAXIMUM_DISTANCE_CHART;
            }
        } else if (e.getSource().equals(view.getEmotionalDispersionCheckBox())) {
            if (view.getEmotionalDispersionCheckBox().isSelected()) {
                event = SimulatorGuiEvent.SHOW_EMOTIONAL_DISPERSION_CHART;
            } else {
                event = SimulatorGuiEvent.HIDE_EMOTIONAL_DISPERSION_CHART;
            }
        } else if (e.getSource().equals(view.getBehavioursCheckBox())) {
            if (view.getBehavioursCheckBox().isSelected()) {
                event = SimulatorGuiEvent.SHOW_BEHAVIOUR_MODIFICATION_CHART;
            } else {
                event = SimulatorGuiEvent.HIDE_BEHAVIOUR_MODIFICATION_CHART;
            }
        } else if (e.getSource().equals(view.getEmotionsCheckBox())) {
            if (view.getEmotionsCheckBox().isSelected()) {
                event = SimulatorGuiEvent.SHOW_EMOTION_MODIFICATION_CHART;
            } else {
                event = SimulatorGuiEvent.HIDE_EMOTION_MODIFICATION_CHART;
            }
        } else if (e.getSource().equals(view.getEmotionalStatesCheckBox())) {
            if (view.getEmotionalStatesCheckBox().isSelected()) {
                event = SimulatorGuiEvent.SHOW_EMOTIONAL_STATE_CHART;
            } else {
                event = SimulatorGuiEvent.HIDE_EMOTIONAL_STATE_CHART;
            }
        }

        if (event != null) {
            GuiEvent guiEvent = new GuiEvent(controller, event.getInt());
            controller.postGuiEvent(guiEvent);
        }
    }

}
