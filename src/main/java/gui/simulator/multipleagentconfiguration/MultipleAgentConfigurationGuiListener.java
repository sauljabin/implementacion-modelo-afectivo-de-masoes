/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.multipleagentconfiguration;

import gui.WindowsEventsAdapter;
import gui.simulator.agentconfiguration.AgentConfigurationGuiEvent;
import gui.simulator.agentconfiguration.AgentConfigurationModel;
import gui.simulator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.simulator.stimulusconfiguration.StimulusConfigurationGuiListener;
import gui.simulator.stimulusconfiguration.StimulusConfigurationModel;
import gui.simulator.stimulusconfiguration.StimulusConfigurationTableModel;
import gui.simulator.stimulusdefinition.StimulusDefinitionModel;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import translate.Translation;
import util.RandomGenerator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleAgentConfigurationGuiListener extends WindowsEventsAdapter {

    private Translation translation = Translation.getInstance();

    private MultipleAgentConfigurationGui multipleAgentConfigurationGui;
    private List<AgentConfigurationModel> agents;
    private List<AgentTypeDefinitionModel> agentTypes;
    private MultipleAgentConfigurationGuiCallback callback;
    private StimulusConfigurationTableModel stimulusTableModel;
    private List<StimulusDefinitionModel> stimuli;
    private List<StimulusConfigurationModel> stimulusConfigurationModelsBase;

    public MultipleAgentConfigurationGuiListener(List<AgentConfigurationModel> agents, List<AgentTypeDefinitionModel> agentTypes, List<StimulusDefinitionModel> stimuli, MultipleAgentConfigurationGuiCallback callback) {
        this.agents = agents;
        this.agentTypes = agentTypes;
        this.callback = callback;
        this.stimuli = stimuli;

        this.multipleAgentConfigurationGui = new MultipleAgentConfigurationGui();
        configView();
        initView();
        this.multipleAgentConfigurationGui.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    private void initView() {
        stimulusConfigurationModelsBase = getStimulusConfigurations();
        stimulusTableModel = new StimulusConfigurationTableModel(multipleAgentConfigurationGui.getStimuliTable(), stimulusConfigurationModelsBase);
        updateEmotion();
    }

    private List<StimulusConfigurationModel> getStimulusConfigurations() {
        return stimuli.stream()
                .map(stimulusDefinitionModel -> new StimulusConfigurationModel(stimulusDefinitionModel))
                .collect(Collectors.toList());
    }

    private String generateAgentName() {
        if (agentTypes.isEmpty()) {
            return "";
        }

        List<String> agentsName = agents.stream()
                .map(model -> model.getName())
                .collect(Collectors.toList());

        int sequence = 1;
        AgentTypeDefinitionModel agentType = (AgentTypeDefinitionModel) multipleAgentConfigurationGui.getAgentTypesCombo().getSelectedItem();
        String tempName = agentType.toString() + sequence;

        while (agentsName.contains(tempName)) {
            tempName = agentType.toString() + ++sequence;
        }

        return tempName;
    }

    public void updateEmotion() {
        EmotionalState emotionalState = new EmotionalState(
                Double.parseDouble(multipleAgentConfigurationGui.getActivationSpinner().getValue().toString()),
                Double.parseDouble(multipleAgentConfigurationGui.getSatisfactionSpinner().getValue().toString())
        );
        Emotion emotion = AffectiveModel.getInstance().searchEmotion(emotionalState);
        multipleAgentConfigurationGui.getEmotionLabel().setText(String.format("%s - %s", translation.get(emotion.getName().toLowerCase()), translation.get(emotion.getType().toString().toLowerCase())));
    }

    private void configView() {
        multipleAgentConfigurationGui.addWindowListener(this);

        multipleAgentConfigurationGui.getAgentTypesCombo().setModel(new DefaultComboBoxModel(agentTypes.toArray()));

        multipleAgentConfigurationGui.getSaveButton().setActionCommand(AgentConfigurationGuiEvent.SAVE.toString());
        multipleAgentConfigurationGui.getSaveButton().addActionListener(this);

        multipleAgentConfigurationGui.getSaveAndNewButton().setActionCommand(AgentConfigurationGuiEvent.SAVE_AND_NEW.toString());
        multipleAgentConfigurationGui.getSaveAndNewButton().addActionListener(this);

        multipleAgentConfigurationGui.getCancelButton().setActionCommand(AgentConfigurationGuiEvent.CANCEL.toString());
        multipleAgentConfigurationGui.getCancelButton().addActionListener(this);

        multipleAgentConfigurationGui.getActivationRandomButton().setActionCommand(AgentConfigurationGuiEvent.SET_RANDOM_ACTIVATION.toString());
        multipleAgentConfigurationGui.getActivationRandomButton().addActionListener(this);

        multipleAgentConfigurationGui.getSatisfactionRandomButton().setActionCommand(AgentConfigurationGuiEvent.SET_RANDOM_SATISFACTION.toString());
        multipleAgentConfigurationGui.getSatisfactionRandomButton().addActionListener(this);

        multipleAgentConfigurationGui.getSatisfactionSpinner().addChangeListener(this);
        multipleAgentConfigurationGui.getActivationSpinner().addChangeListener(this);

        multipleAgentConfigurationGui.getSelectAllButton().setActionCommand(AgentConfigurationGuiEvent.SELECT_ALL.toString());
        multipleAgentConfigurationGui.getSelectAllButton().addActionListener(this);

        multipleAgentConfigurationGui.getDeselectAllButton().setActionCommand(AgentConfigurationGuiEvent.DESELECT_ALL.toString());
        multipleAgentConfigurationGui.getDeselectAllButton().addActionListener(this);

        multipleAgentConfigurationGui.getConfigStimulusButton().setActionCommand(AgentConfigurationGuiEvent.SHOW_STIMULUS_CONFIGURATION_GUI.toString());
        multipleAgentConfigurationGui.getConfigStimulusButton().addActionListener(this);

        multipleAgentConfigurationGui.getRandomEmotionalStateCheckBox().addItemListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(multipleAgentConfigurationGui.getSatisfactionSpinner())
                || e.getSource().equals(multipleAgentConfigurationGui.getActivationSpinner())) {
            eventHandler(MultipleAgentConfigurationGuiEvent.UPDATE_EMOTION);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(multipleAgentConfigurationGui.getRandomEmotionalStateCheckBox())) {
            eventHandler(MultipleAgentConfigurationGuiEvent.SET_RANDOM_EMOTIONAL_STATE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(MultipleAgentConfigurationGuiEvent.valueOf(e.getActionCommand()));
    }

    private void eventHandler(MultipleAgentConfigurationGuiEvent event) {
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
            case SHOW_STIMULUS_CONFIGURATION_GUI:
                showStimulusConfiguration();
                break;
            case SET_RANDOM_EMOTIONAL_STATE:
                setRandomEmotionalState();
                break;
        }
    }

    private void setRandomEmotionalState() {
        if (multipleAgentConfigurationGui.getRandomEmotionalStateCheckBox().isSelected()) {
            multipleAgentConfigurationGui.getActivationSpinner().setValue(0);
            multipleAgentConfigurationGui.getSatisfactionSpinner().setValue(0);

            multipleAgentConfigurationGui.getActivationSpinner().setEnabled(false);
            multipleAgentConfigurationGui.getSatisfactionSpinner().setEnabled(false);

            multipleAgentConfigurationGui.getSatisfactionRandomButton().setEnabled(false);
            multipleAgentConfigurationGui.getActivationRandomButton().setEnabled(false);
        } else {
            multipleAgentConfigurationGui.getActivationSpinner().setEnabled(true);
            multipleAgentConfigurationGui.getSatisfactionSpinner().setEnabled(true);

            multipleAgentConfigurationGui.getSatisfactionRandomButton().setEnabled(true);
            multipleAgentConfigurationGui.getActivationRandomButton().setEnabled(true);
            updateEmotion();
        }
    }

    private void showStimulusConfiguration() {
        if (stimulusTableModel.hasSelected()) {
            new StimulusConfigurationGuiListener(stimulusTableModel.getSelectedElement(), model -> stimulusTableModel.fireTableDataChanged());
        }
    }

    private void deselectAll() {
        stimulusTableModel.deselectElements();
    }

    private void selectAll() {
        stimulusTableModel.selectElements();
    }

    private void close() {
        multipleAgentConfigurationGui.dispose();
    }

    private void saveAndNew() {
        updateAgents();
        initView();
    }

    private void updateAgents() {
        int quantity = Integer.parseInt(multipleAgentConfigurationGui.getQuantitySpinner().getValue().toString());

        for (int i = 0; i < quantity; i++) {
            EmotionalState emotionalState = new EmotionalState();

            if (!multipleAgentConfigurationGui.getRandomEmotionalStateCheckBox().isSelected()) {
                emotionalState.setActivation(Double.parseDouble(multipleAgentConfigurationGui.getActivationSpinner().getValue().toString()));
                emotionalState.setSatisfaction(Double.parseDouble(multipleAgentConfigurationGui.getSatisfactionSpinner().getValue().toString()));
            }

            agents.add(new AgentConfigurationModel(
                    (AgentTypeDefinitionModel) multipleAgentConfigurationGui.getAgentTypesCombo().getSelectedItem(),
                    generateAgentName(),
                    emotionalState,
                    cloneStimulusConfiguration()
            ));
        }

        if (callback != null) {
            callback.afterSave();
        }
    }

    private List<StimulusConfigurationModel> cloneStimulusConfiguration() {
        return stimulusConfigurationModelsBase.stream()
                .map(stimulusConfigurationModel -> new StimulusConfigurationModel(stimulusConfigurationModel))
                .collect(Collectors.toList());
    }

    private void save() {
        updateAgents();
        close();
    }

    private void setRandomSatisfaction() {
        multipleAgentConfigurationGui.getSatisfactionSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
        updateEmotion();
    }

    private void setRandomActivation() {
        multipleAgentConfigurationGui.getActivationSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
        updateEmotion();
    }

}
