/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator.agent;

import gui.WindowsEventsAdapter;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import translate.Translation;
import util.RandomGenerator;

import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class AgentViewController extends WindowsEventsAdapter {

    private Translation translation = Translation.getInstance();

    private AgentView agentView;
    private AgentModel agentModel;
    private AgentViewControllerCallback callback;

    public AgentViewController(AgentViewControllerCallback callback) {
        this(null, callback);
    }

    public AgentViewController(AgentModel agentModel, AgentViewControllerCallback callback) {
        this.agentModel = agentModel;
        this.callback = callback;
        this.agentView = new AgentView();
        configView();
        initView();
        this.agentView.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        agentView.dispose();
    }

    private void initView() {
        if (agentModel == null) {
            agentModel = new AgentModel();
        }

        agentView.getNameField().setText(agentModel.getName());
        agentView.getActivationSpinner().setValue(agentModel.getActivation());
        agentView.getSatisfactionSpinner().setValue(agentModel.getSatisfaction());
        agentView.getAgentTypesCombo().setSelectedItem(agentModel.getAgentType());

        updateEmotion();
    }

    public void updateEmotion() {
        EmotionalState emotionalState = new EmotionalState((Double) agentView.getActivationSpinner().getValue(), (Double) agentView.getSatisfactionSpinner().getValue());
        Emotion emotion = AffectiveModel.getInstance().searchEmotion(emotionalState);
        agentView.getEmotionLabel().setText(String.format("%s - %s", translation.get(emotion.getName().toLowerCase()), translation.get(emotion.getType().toString().toLowerCase())));
    }

    private void configView() {
        agentView.addWindowListener(this);

        agentView.getSaveButton().setActionCommand(AgentViewEvent.SAVE.toString());
        agentView.getSaveButton().addActionListener(this);

        agentView.getSaveAndNewButton().setActionCommand(AgentViewEvent.SAVE_AND_NEW.toString());
        agentView.getSaveAndNewButton().addActionListener(this);

        agentView.getCancelButton().setActionCommand(AgentViewEvent.CANCEL.toString());
        agentView.getCancelButton().addActionListener(this);

        agentView.getActivationRandomButton().setActionCommand(AgentViewEvent.SET_RANDOM_ACTIVATION.toString());
        agentView.getActivationRandomButton().addActionListener(this);

        agentView.getSatisfactionRandomButton().setActionCommand(AgentViewEvent.SET_RANDOM_SATISFACTION.toString());
        agentView.getSatisfactionRandomButton().addActionListener(this);

        agentView.getSatisfactionSpinner().addChangeListener(this);
        agentView.getActivationSpinner().addChangeListener(this);

        if (agentModel != null) {
            agentView.getSaveAndNewButton().setVisible(false);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        eventHandler(AgentViewEvent.UPDATE_EMOTION);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(AgentViewEvent.valueOf(e.getActionCommand()));
    }

    private void updateModel() {
        agentModel.setAgentType((AgentType) agentView.getAgentTypesCombo().getSelectedItem());
        agentModel.setName(agentView.getNameField().getText());
        agentModel.setActivation((Double) agentView.getActivationSpinner().getValue());
        agentModel.setSatisfaction((Double) agentView.getSatisfactionSpinner().getValue());
        if (callback != null) {
            callback.afterSave(agentModel);
        }
    }

    private void eventHandler(AgentViewEvent event) {
        switch (event) {
            case SAVE:
                updateModel();
                agentView.dispose();
                break;
            case SAVE_AND_NEW:
                updateModel();
                agentModel = null;
                initView();
                break;
            case CANCEL:
                agentView.dispose();
                break;
            case SET_RANDOM_ACTIVATION:
                setRandomActivation();
                updateEmotion();
                break;
            case SET_RANDOM_SATISFACTION:
                setRandomSatisfaction();
                updateEmotion();
                break;
            case UPDATE_EMOTION:
                updateEmotion();
                break;
        }
    }

    private void setRandomSatisfaction() {
        agentView.getSatisfactionSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
    }

    private void setRandomActivation() {
        agentView.getActivationSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
    }

    public static void main(String[] args) {
        new AgentViewController(agentModel -> System.out.println(agentModel));
    }

}
