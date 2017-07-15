/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.stimulusconfiguration;

import gui.WindowsEventsAdapter;
import gui.simulator.stimulusdefinition.StimulusDefinitionModel;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class StimulusConfigurationGuiListener extends WindowsEventsAdapter {

    private StimulusConfigurationModel stimulusConfigurationModel;
    private StimulusConfigurationGui gui;
    private StimulusConfigurationGuiCallback callback;

    public StimulusConfigurationGuiListener(StimulusConfigurationModel stimulusConfigurationModel, StimulusConfigurationGuiCallback callback) {
        this.callback = callback;
        this.stimulusConfigurationModel = stimulusConfigurationModel;
        gui = new StimulusConfigurationGui();
        configView();
        initView();
        gui.setVisible(true);
        gui.getNameField().requestFocus();
    }

    public static void main(String[] args) {
        new StimulusConfigurationGuiListener(new StimulusConfigurationModel(new StimulusDefinitionModel("test", "test", 0.2, 0.2, true)), model -> System.out.println(model));
    }

    private void initView() {
        gui.getNameField().setText(stimulusConfigurationModel.getStimulusDefinition().getName());
        gui.getValueField().setText(stimulusConfigurationModel.getStimulusDefinition().getValue());
        gui.getActivationSpinner().setValue(stimulusConfigurationModel.getActivation());
        gui.getSatisfactionSpinner().setValue(stimulusConfigurationModel.getSatisfaction());
        gui.getSelfButton().setSelected(stimulusConfigurationModel.isSelf());
        gui.getOthersButton().setSelected(!stimulusConfigurationModel.isSelf());
        gui.getNameField().requestFocus();
    }

    private void configView() {
        gui.addWindowListener(this);

        gui.getSaveButton().setActionCommand(StimulusConfigurationGuiEvent.SAVE.toString());
        gui.getSaveButton().addActionListener(this);

        gui.getCancelButton().setActionCommand(StimulusConfigurationGuiEvent.CANCEL.toString());
        gui.getCancelButton().addActionListener(this);

        gui.getNameField().setEditable(false);
        gui.getValueField().setEditable(false);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(StimulusConfigurationGuiEvent.valueOf(e.getActionCommand()));
    }

    private void eventHandler(StimulusConfigurationGuiEvent event) {
        switch (event) {
            case SAVE:
                save();
                break;
            case CANCEL:
                close();
                break;
        }
    }

    private void close() {
        gui.dispose();
    }

    private void save() {
        updateModel();
        close();
    }

    private void updateModel() {
        stimulusConfigurationModel.setActivation((Double) gui.getActivationSpinner().getValue());
        stimulusConfigurationModel.setSatisfaction((Double) gui.getSatisfactionSpinner().getValue());
        stimulusConfigurationModel.setSelf(gui.getSelfButton().isSelected());
        if (callback != null) {
            callback.afterSave(stimulusConfigurationModel);
        }
    }

}
