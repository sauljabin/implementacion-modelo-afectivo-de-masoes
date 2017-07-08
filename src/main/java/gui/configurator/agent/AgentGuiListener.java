/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.agent;

import gui.configurator.stimulus.StimulusModel;
import gui.configurator.stimulus.table.SelectableStimulusTableModel;
import gui.WindowsEventsAdapter;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import translate.Translation;
import util.RandomGenerator;

import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Collectors;

public class AgentGuiListener extends WindowsEventsAdapter {

    private Translation translation = Translation.getInstance();

    private AgentGui agentGui;
    private AgentModel agentModel;
    private List<AgentModel> agents;
    private AgentGuiListenerCallback callback;
    private SelectableStimulusTableModel stimulusTableModel;
    private List<StimulusModel> stimuli;

    public AgentGuiListener(List<AgentModel> agents, List<StimulusModel> stimuli, AgentGuiListenerCallback callback) {
        this(null, agents, stimuli, callback);
    }

    public AgentGuiListener(AgentModel agentModel, List<AgentModel> agents, List<StimulusModel> stimuli, AgentGuiListenerCallback callback) {
        this.agentModel = agentModel;
        this.agents = agents;
        this.callback = callback;
        this.stimuli = stimuli;

        this.agentGui = new AgentGui();
        configView();
        initView();
        this.agentGui.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    private void initView() {
        if (agentModel == null) {
            agentModel = new AgentModel();
            agentModel.setName(generateAgentName());
            agentModel.setStimuli(stimuli);
        }

        agentGui.getNameField().setText(agentModel.getName());
        agentGui.getActivationSpinner().setValue(agentModel.getActivation());
        agentGui.getSatisfactionSpinner().setValue(agentModel.getSatisfaction());
        agentGui.getAgentTypesCombo().setSelectedItem(agentModel.getAgentType());

        stimulusTableModel = new SelectableStimulusTableModel(agentGui.getStimuliTable(), stimuli);
        stimulusTableModel.selectStimuli(agentModel.getStimuli());

        updateEmotion();
    }

    private String generateAgentName() {
        List<String> agentsName = agents.stream()
                .map(agentModel -> agentModel.getName())
                .collect(Collectors.toList());

        int sequence = 1;
        AgentType agentType = (AgentType) agentGui.getAgentTypesCombo().getSelectedItem();
        String tempName = agentType.toString() + sequence;

        while (agentsName.contains(tempName)) {
            tempName = agentType.toString() + ++sequence;
        }

        return tempName;
    }

    public void updateEmotion() {
        EmotionalState emotionalState = new EmotionalState((Double) agentGui.getActivationSpinner().getValue(), (Double) agentGui.getSatisfactionSpinner().getValue());
        Emotion emotion = AffectiveModel.getInstance().searchEmotion(emotionalState);
        agentGui.getEmotionLabel().setText(String.format("%s - %s", translation.get(emotion.getName().toLowerCase()), translation.get(emotion.getType().toString().toLowerCase())));
    }

    private void configView() {
        agentGui.addWindowListener(this);

        agentGui.getSaveButton().setActionCommand(AgentGuiEvent.SAVE.toString());
        agentGui.getSaveButton().addActionListener(this);

        agentGui.getSaveAndNewButton().setActionCommand(AgentGuiEvent.SAVE_AND_NEW.toString());
        agentGui.getSaveAndNewButton().addActionListener(this);

        agentGui.getCancelButton().setActionCommand(AgentGuiEvent.CANCEL.toString());
        agentGui.getCancelButton().addActionListener(this);

        agentGui.getActivationRandomButton().setActionCommand(AgentGuiEvent.SET_RANDOM_ACTIVATION.toString());
        agentGui.getActivationRandomButton().addActionListener(this);

        agentGui.getSatisfactionRandomButton().setActionCommand(AgentGuiEvent.SET_RANDOM_SATISFACTION.toString());
        agentGui.getSatisfactionRandomButton().addActionListener(this);

        agentGui.getSatisfactionSpinner().addChangeListener(this);
        agentGui.getActivationSpinner().addChangeListener(this);

        agentGui.getSelectAllButton().setActionCommand(AgentGuiEvent.SELECT_ALL.toString());
        agentGui.getSelectAllButton().addActionListener(this);

        agentGui.getDeselectAllButton().setActionCommand(AgentGuiEvent.DESELECT_ALL.toString());
        agentGui.getDeselectAllButton().addActionListener(this);

        agentGui.getAgentTypesCombo().addItemListener(this);

        if (agentModel != null) {
            agentGui.getSaveAndNewButton().setVisible(false);
        }

        agentGui.getNameField().setEditable(false);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(agentGui.getSatisfactionSpinner())
                || e.getSource().equals(agentGui.getActivationSpinner())) {
            eventHandler(AgentGuiEvent.UPDATE_EMOTION);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(AgentGuiEvent.valueOf(e.getActionCommand()));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(agentGui.getAgentTypesCombo())) {
            eventHandler(AgentGuiEvent.UPDATE_AGENT_TYPE);
        }
    }

    private void updateModel() {
        agentModel.setAgentType((AgentType) agentGui.getAgentTypesCombo().getSelectedItem());
        agentModel.setName(agentGui.getNameField().getText());
        agentModel.setActivation((Double) agentGui.getActivationSpinner().getValue());
        agentModel.setSatisfaction((Double) agentGui.getSatisfactionSpinner().getValue());
        agentModel.setStimuli(stimulusTableModel.getSelectedStimuli());
        if (callback != null) {
            callback.afterSave(agentModel);
        }
    }

    private void eventHandler(AgentGuiEvent event) {
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
            case SET_RANDOM_ACTIVATION:
                setRandomActivation();
                break;
            case SET_RANDOM_SATISFACTION:
                setRandomSatisfaction();
                break;
            case UPDATE_EMOTION:
                updateEmotion();
                break;
            case SELECT_ALL:
                selectAll();
                break;
            case DESELECT_ALL:
                deselectAll();
                break;
            case UPDATE_AGENT_TYPE:
                updateAgentType();
                break;
        }
    }

    private void updateAgentType() {
        agentGui.getNameField().setText(generateAgentName());
    }

    private void deselectAll() {
        stimulusTableModel.deselectStimuli();
    }

    private void selectAll() {
        stimulusTableModel.selectStimuli();
    }

    private void close() {
        agentGui.dispose();
    }

    private void saveAndNew() {
        updateModel();
        agentModel = null;
        initView();
    }

    private void save() {
        updateModel();
        close();
    }

    private void setRandomSatisfaction() {
        agentGui.getSatisfactionSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
        updateEmotion();
    }

    private void setRandomActivation() {
        agentGui.getActivationSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
        updateEmotion();
    }

}
