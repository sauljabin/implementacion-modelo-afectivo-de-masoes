/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.stimulus;

import gui.WindowsEventsAdapter;
import util.StringFormatter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class StimulusGuiListener extends WindowsEventsAdapter {

    private StimulusModel stimulusModel;
    private StimulusGui stimulusGui;
    private StimulusGuiCallback callback;

    public StimulusGuiListener(StimulusGuiCallback callback) {
        this(null, callback);
    }

    public StimulusGuiListener(StimulusModel stimulusModel, StimulusGuiCallback callback) {
        this.stimulusModel = stimulusModel;
        this.stimulusGui = new StimulusGui();
        configView();
        initView();
        this.stimulusGui.setVisible(true);
        this.callback = callback;
    }

    private void initView() {
        if (stimulusModel == null) {
            stimulusModel = new StimulusModel();
        }

        stimulusGui.getNameField().setText(stimulusModel.getName());
        stimulusGui.getValueField().setText(stimulusModel.getValue());
        stimulusGui.getActivationSpinner().setValue(stimulusModel.getActivation());
        stimulusGui.getSatisfactionSpinner().setValue(stimulusModel.getSatisfaction());
        stimulusGui.getSelfButton().setSelected(stimulusModel.isSelf());
        stimulusGui.getOthersButton().setSelected(!stimulusModel.isSelf());
    }

    private void configView() {
        stimulusGui.addWindowListener(this);

        stimulusGui.getSaveButton().setActionCommand(StimulusGuiEvent.SAVE.toString());
        stimulusGui.getSaveButton().addActionListener(this);

        stimulusGui.getSaveAndNewButton().setActionCommand(StimulusGuiEvent.SAVE_AND_NEW.toString());
        stimulusGui.getSaveAndNewButton().addActionListener(this);

        stimulusGui.getCancelButton().setActionCommand(StimulusGuiEvent.CANCEL.toString());
        stimulusGui.getCancelButton().addActionListener(this);

        stimulusGui.getNameField().addKeyListener(this);

        if (stimulusModel != null) {
            stimulusGui.getSaveAndNewButton().setVisible(false);
        }

        stimulusGui.getValueField().setEditable(false);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(StimulusGuiEvent.valueOf(e.getActionCommand()));
    }

    private void eventHandler(StimulusGuiEvent event) {
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
        stimulusGui.dispose();
    }

    private void saveAndNew() {
        updateModel();
        stimulusModel = null;
        initView();
    }

    private void save() {
        updateModel();
        close();
    }

    private void changeValue() {
        String name = stimulusGui.getNameField().getText();
        stimulusGui.getValueField().setText(StringFormatter.toCamelCase(name));
    }

    private void updateModel() {
        stimulusModel.setName(stimulusGui.getNameField().getText());
        stimulusModel.setValue(stimulusGui.getValueField().getText());
        stimulusModel.setActivation((Double) stimulusGui.getActivationSpinner().getValue());
        stimulusModel.setSatisfaction((Double) stimulusGui.getSatisfactionSpinner().getValue());
        stimulusModel.setSelf(stimulusGui.getSelfButton().isSelected());
        if (callback != null) {
            callback.afterSave(stimulusModel);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(stimulusGui.getNameField())) {
            eventHandler(StimulusGuiEvent.NAME_CHANGED);
        }
    }

}
