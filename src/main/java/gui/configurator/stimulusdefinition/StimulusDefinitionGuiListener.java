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
    private StimulusDefinitionGui gui;
    private StimulusDefinitionGuiCallback callback;

    public StimulusDefinitionGuiListener(StimulusDefinitionGuiCallback callback) {
        this(null, callback);
    }

    public StimulusDefinitionGuiListener(StimulusDefinitionModel stimulusDefinitionModel, StimulusDefinitionGuiCallback callback) {
        this.callback = callback;
        this.stimulusDefinitionModel = stimulusDefinitionModel;
        gui = new StimulusDefinitionGui();
        configView();
        initView();
        gui.setVisible(true);
        gui.getNameField().requestFocus();
    }

    public static void main(String[] args) {
        new StimulusDefinitionGuiListener(model -> System.out.println(model));
    }

    private void initView() {
        if (stimulusDefinitionModel == null) {
            stimulusDefinitionModel = new StimulusDefinitionModel();
        }

        gui.getNameField().setText(stimulusDefinitionModel.getName());
        gui.getValueField().setText(stimulusDefinitionModel.getValue());
        gui.getActivationSpinner().setValue(stimulusDefinitionModel.getActivation());
        gui.getSatisfactionSpinner().setValue(stimulusDefinitionModel.getSatisfaction());
        gui.getSelfButton().setSelected(stimulusDefinitionModel.isSelf());
        gui.getOthersButton().setSelected(!stimulusDefinitionModel.isSelf());
        gui.getNameField().requestFocus();
    }

    private void configView() {
        gui.addWindowListener(this);

        gui.getSaveButton().setActionCommand(StimulusDefinitionGuiEvent.SAVE.toString());
        gui.getSaveButton().addActionListener(this);

        gui.getSaveAndNewButton().setActionCommand(StimulusDefinitionGuiEvent.SAVE_AND_NEW.toString());
        gui.getSaveAndNewButton().addActionListener(this);

        gui.getCancelButton().setActionCommand(StimulusDefinitionGuiEvent.CANCEL.toString());
        gui.getCancelButton().addActionListener(this);

        gui.getNameField().addKeyListener(this);

        if (stimulusDefinitionModel != null) {
            gui.getSaveAndNewButton().setVisible(false);
        }

        gui.getValueField().setEditable(false);
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
        gui.dispose();
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
        String name = gui.getNameField().getText();
        gui.getValueField().setText(StringFormatter.toCamelCase(name));
    }

    private void updateModel() {
        stimulusDefinitionModel.setName(gui.getNameField().getText());
        stimulusDefinitionModel.setValue(gui.getValueField().getText());
        stimulusDefinitionModel.setActivation((Double) gui.getActivationSpinner().getValue());
        stimulusDefinitionModel.setSatisfaction((Double) gui.getSatisfactionSpinner().getValue());
        stimulusDefinitionModel.setSelf(gui.getSelfButton().isSelected());
        if (callback != null) {
            callback.afterSave(stimulusDefinitionModel);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(gui.getNameField())) {
            eventHandler(StimulusDefinitionGuiEvent.NAME_CHANGED);
        }
    }

}
