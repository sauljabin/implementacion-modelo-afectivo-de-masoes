/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator.agent;

import environment.wikipedia.configurator.stimulus.StimulusModel;
import environment.wikipedia.configurator.stimulus.table.SelectableStimulusTableModel;
import gui.WindowsEventsAdapter;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import translate.Translation;
import util.RandomGenerator;

import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AgentViewController extends WindowsEventsAdapter {

    private Translation translation = Translation.getInstance();

    private AgentView agentView;
    private AgentModel agentModel;
    private List<AgentModel> agents;
    private AgentViewControllerCallback callback;
    private SelectableStimulusTableModel stimulusTableModel;
    private List<StimulusModel> stimuli;

    public AgentViewController(List<AgentModel> agents, List<StimulusModel> stimuli, AgentViewControllerCallback callback) {
        this(null, agents, stimuli, callback);
    }

    public AgentViewController(AgentModel agentModel, List<AgentModel> agents, List<StimulusModel> stimuli, AgentViewControllerCallback callback) {
        this.agentModel = agentModel;
        this.agents = agents;
        this.callback = callback;
        this.stimuli = stimuli;

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
            agentModel.setName(generateAgentName());
            agentModel.setStimuli(stimuli);
        }

        agentView.getNameField().setText(agentModel.getName());
        agentView.getActivationSpinner().setValue(agentModel.getActivation());
        agentView.getSatisfactionSpinner().setValue(agentModel.getSatisfaction());
        agentView.getAgentTypesCombo().setSelectedItem(agentModel.getAgentType());

        stimulusTableModel = new SelectableStimulusTableModel(agentView.getStimulusTable(), stimuli);
        stimulusTableModel.selectStimuli(agentModel.getStimuli());

        updateEmotion();
    }

    private String generateAgentName() {
        List<String> agentsName = agents.stream()
                .map(agentModel -> agentModel.getName())
                .collect(Collectors.toList());

        int sequence = 1;
        AgentType agentType = (AgentType) agentView.getAgentTypesCombo().getSelectedItem();
        String tempName = agentType.toString() + sequence;

        while (agentsName.contains(tempName)) {
            tempName = agentType.toString() + ++sequence;
        }

        return tempName;
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

        agentView.getSelectAllButton().setActionCommand(AgentViewEvent.SELECT_ALL.toString());
        agentView.getSelectAllButton().addActionListener(this);

        agentView.getDeselectAllButton().setActionCommand(AgentViewEvent.DESELECT_ALL.toString());
        agentView.getDeselectAllButton().addActionListener(this);

        if (agentModel != null) {
            agentView.getSaveAndNewButton().setVisible(false);
        }

        agentView.getNameField().setEditable(false);
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
        agentModel.setStimuli(stimulusTableModel.getSelectedStimuli());
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
                agents.add(agentModel);
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
            case SELECT_ALL:
                stimulusTableModel.selectStimuli();
                break;
            case DESELECT_ALL:
                stimulusTableModel.deselectStimuli();
                break;
        }
    }

    private void setRandomSatisfaction() {
        agentView.getSatisfactionSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
    }

    private void setRandomActivation() {
        agentView.getActivationSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
    }

}
