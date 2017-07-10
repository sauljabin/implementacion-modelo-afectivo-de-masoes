/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.stimulusdefinition;

import gui.WindowsEventsAdapter;
import util.StringFormatter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class StimulusDefinitionGuiListener extends WindowsEventsAdapter {

    private StimulusDefinitionModel stimulusDefinitionModel;
    private StimulusDefinitionGui stimulusDefinitionGui;
    private StimulusDefinitionGuiCallback callback;

    public StimulusDefinitionGuiListener(StimulusDefinitionGuiCallback callback) {
        this(null, callback);
    }

    public StimulusDefinitionGuiListener(StimulusDefinitionModel stimulusDefinitionModel, StimulusDefinitionGuiCallback callback) {
        this.stimulusDefinitionModel = stimulusDefinitionModel;
        this.stimulusDefinitionGui = new StimulusDefinitionGui();
        configView();
        initView();
        this.stimulusDefinitionGui.setVisible(true);
        this.callback = callback;
    }

    public static void main(String[] args) {
        new StimulusDefinitionGuiListener(model -> System.out.println(model));
    }

    private void initView() {
        if (stimulusDefinitionModel == null) {
            stimulusDefinitionModel = new StimulusDefinitionModel();
        }

        stimulusDefinitionGui.getNameField().setText(stimulusDefinitionModel.getName());
        stimulusDefinitionGui.getValueField().setText(stimulusDefinitionModel.getValue());
        stimulusDefinitionGui.getActivationSpinner().setValue(stimulusDefinitionModel.getActivation());
        stimulusDefinitionGui.getSatisfactionSpinner().setValue(stimulusDefinitionModel.getSatisfaction());
        stimulusDefinitionGui.getSelfButton().setSelected(stimulusDefinitionModel.isSelf());
        stimulusDefinitionGui.getOthersButton().setSelected(!stimulusDefinitionModel.isSelf());
    }

    private void configView() {
        stimulusDefinitionGui.addWindowListener(this);

        stimulusDefinitionGui.getSaveButton().setActionCommand(StimulusDefinitionGuiEvent.SAVE.toString());
        stimulusDefinitionGui.getSaveButton().addActionListener(this);

        stimulusDefinitionGui.getSaveAndNewButton().setActionCommand(StimulusDefinitionGuiEvent.SAVE_AND_NEW.toString());
        stimulusDefinitionGui.getSaveAndNewButton().addActionListener(this);

        stimulusDefinitionGui.getCancelButton().setActionCommand(StimulusDefinitionGuiEvent.CANCEL.toString());
        stimulusDefinitionGui.getCancelButton().addActionListener(this);

        stimulusDefinitionGui.getNameField().addKeyListener(this);

        if (stimulusDefinitionModel != null) {
            stimulusDefinitionGui.getSaveAndNewButton().setVisible(false);
        }

        stimulusDefinitionGui.getValueField().setEditable(false);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(StimulusDefinitionGuiEvent.valueOf(e.getActionCommand()));
    }

    private void eventHandler(StimulusDefinitionGuiEvent event) {
        switch (event) {
            case SAVE:
                save();
                break;
            case SAVE_AND_NEW:
                saveAndNew();
                break;
            case CANCEL:
                close();
                break;
            case NAME_CHANGED:
                changeValue();
                break;
        }
    }

    private void close() {
        stimulusDefinitionGui.dispose();
    }

    private void saveAndNew() {
        updateModel();
        stimulusDefinitionModel = null;
        initView();
    }

    private void save() {
        updateModel();
        close();
    }

    private void changeValue() {
        String name = stimulusDefinitionGui.getNameField().getText();
        stimulusDefinitionGui.getValueField().setText(StringFormatter.toCamelCase(name));
    }

    private void updateModel() {
        stimulusDefinitionModel.setName(stimulusDefinitionGui.getNameField().getText());
        stimulusDefinitionModel.setValue(stimulusDefinitionGui.getValueField().getText());
        stimulusDefinitionModel.setActivation((Double) stimulusDefinitionGui.getActivationSpinner().getValue());
        stimulusDefinitionModel.setSatisfaction((Double) stimulusDefinitionGui.getSatisfactionSpinner().getValue());
        stimulusDefinitionModel.setSelf(stimulusDefinitionGui.getSelfButton().isSelected());
        if (callback != null) {
            callback.afterSave(stimulusDefinitionModel);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(stimulusDefinitionGui.getNameField())) {
            eventHandler(StimulusDefinitionGuiEvent.NAME_CHANGED);
        }
    }

}
