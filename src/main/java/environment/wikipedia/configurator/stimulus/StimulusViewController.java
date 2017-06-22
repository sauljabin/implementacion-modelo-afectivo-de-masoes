/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator.stimulus;

import gui.WindowsEventsAdapter;
import util.StringFormatter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class StimulusViewController extends WindowsEventsAdapter {

    private StimulusModel stimulusModel;
    private StimulusView stimulusView;
    private StimulusViewControllerCallback callback;

    public StimulusViewController(StimulusModel stimulusModel) {
        this(stimulusModel, null);
    }

    public StimulusViewController(StimulusModel stimulusModel, StimulusViewControllerCallback callback) {
        this.stimulusModel = stimulusModel;
        this.stimulusView = new StimulusView();
        configView();
        this.stimulusView.setVisible(true);
        this.callback = callback;
    }

    private void configView() {
        stimulusView.addWindowListener(this);

        stimulusView.getSaveButton().setActionCommand(StimulusViewEvent.SAVE.toString());
        stimulusView.getSaveButton().addActionListener(this);

        stimulusView.getCancelButton().setActionCommand(StimulusViewEvent.CANCEL.toString());
        stimulusView.getCancelButton().addActionListener(this);

        stimulusView.getNameField().addKeyListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        stimulusView.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(StimulusViewEvent.valueOf(e.getActionCommand()));
    }

    private void eventHandler(StimulusViewEvent event) {
        switch (event) {
            case SAVE:
                updateModel();
                break;
            case CANCEL:
                stimulusView.dispose();
                break;
            case NAME_CHANGED:
                changeValue();
                break;
        }
    }

    private void changeValue() {
        String name = stimulusView.getNameField().getText();
        stimulusView.getValueField().setText(StringFormatter.toCamelCase(name));
    }

    private void updateModel() {
        stimulusModel.setName(stimulusView.getNameField().getText());
        stimulusModel.setValue(stimulusView.getValueField().getText());
        stimulusModel.setActivation((Double) stimulusView.getActivationSpinner().getValue());
        stimulusModel.setSatisfaction((Double) stimulusView.getSatisfactionSpinner().getValue());
        stimulusModel.setSelf(stimulusView.getSelfButton().isSelected());
        stimulusView.dispose();
        if(callback!=null){
            callback.afterSave();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(stimulusView.getNameField())) {
            eventHandler(StimulusViewEvent.NAME_CHANGED);
        }
    }

    public static void main(String[] args) {
        new StimulusViewController(new StimulusModel());
    }

}
