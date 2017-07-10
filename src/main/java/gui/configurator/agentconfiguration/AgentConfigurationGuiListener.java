/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.agentconfiguration;

import gui.WindowsEventsAdapter;
import gui.configurator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.configurator.stimulusdefinition.StimulusDefinitionModel;
import gui.configurator.stimulusdefinition.table.SelectableStimulusTableModel;
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

public class AgentConfigurationGuiListener extends WindowsEventsAdapter {

    private Translation translation = Translation.getInstance();

    private AgentConfigurationGui agentConfigurationGui;
    private AgentConfigurationModel agentConfigurationModel;
    private List<AgentConfigurationModel> agents;
    private List<AgentTypeDefinitionModel> agentTypes;
    private AgentConfigurationGuiCallback callback;
    private SelectableStimulusTableModel stimulusTableModel;
    private List<StimulusDefinitionModel> stimuli;

    public AgentConfigurationGuiListener(List<AgentConfigurationModel> agents, List<AgentTypeDefinitionModel> agentTypes, List<StimulusDefinitionModel> stimuli, AgentConfigurationGuiCallback callback) {
        this(null, agents, agentTypes, stimuli, callback);
    }

    public AgentConfigurationGuiListener(AgentConfigurationModel agentConfigurationModel, List<AgentConfigurationModel> agents, List<AgentTypeDefinitionModel> agentTypes, List<StimulusDefinitionModel> stimuli, AgentConfigurationGuiCallback callback) {
        this.agentConfigurationModel = agentConfigurationModel;
        this.agents = agents;
        this.agentTypes = agentTypes;
        this.callback = callback;
        this.stimuli = stimuli;

        this.agentConfigurationGui = new AgentConfigurationGui();
        configView();
        initView();
        this.agentConfigurationGui.setVisible(true);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    private void initView() {
        if (agentConfigurationModel == null) {
            agentConfigurationModel = new AgentConfigurationModel();
            agentConfigurationModel.setName(generateAgentName());
            agentConfigurationModel.setStimuli(stimuli);
            if (!agentTypes.isEmpty()) {
                agentConfigurationModel.setAgentType(agentTypes.get(0));
            }
        }

        agentConfigurationGui.getNameField().setText(agentConfigurationModel.getName());
        agentConfigurationGui.getActivationSpinner().setValue(agentConfigurationModel.getActivation());
        agentConfigurationGui.getSatisfactionSpinner().setValue(agentConfigurationModel.getSatisfaction());
        agentConfigurationGui.getAgentTypesCombo().setSelectedItem(agentConfigurationModel.getAgentType());

        stimulusTableModel = new SelectableStimulusTableModel(agentConfigurationGui.getStimuliTable(), stimuli);
        stimulusTableModel.selectStimuli(agentConfigurationModel.getStimuli());

        updateEmotion();
    }

    private String generateAgentName() {

        if (agentTypes.isEmpty()) {
            return "";
        }

        List<String> agentsName = agents.stream()
                .map(agentConfigurationModel -> agentConfigurationModel.getName())
                .collect(Collectors.toList());

        int sequence = 1;
        AgentTypeDefinitionModel agentType = (AgentTypeDefinitionModel) agentConfigurationGui.getAgentTypesCombo().getSelectedItem();
        String tempName = agentType.toString() + sequence;

        while (agentsName.contains(tempName)) {
            tempName = agentType.toString() + ++sequence;
        }

        return tempName;
    }

    public void updateEmotion() {
        EmotionalState emotionalState = new EmotionalState((Double) agentConfigurationGui.getActivationSpinner().getValue(), (Double) agentConfigurationGui.getSatisfactionSpinner().getValue());
        Emotion emotion = AffectiveModel.getInstance().searchEmotion(emotionalState);
        agentConfigurationGui.getEmotionLabel().setText(String.format("%s - %s", translation.get(emotion.getName().toLowerCase()), translation.get(emotion.getType().toString().toLowerCase())));
    }

    private void configView() {
        agentConfigurationGui.addWindowListener(this);

        agentConfigurationGui.getAgentTypesCombo().setModel(new DefaultComboBoxModel(agentTypes.toArray()));

        agentConfigurationGui.getSaveButton().setActionCommand(AgentConfigurationGuiEvent.SAVE.toString());
        agentConfigurationGui.getSaveButton().addActionListener(this);

        agentConfigurationGui.getSaveAndNewButton().setActionCommand(AgentConfigurationGuiEvent.SAVE_AND_NEW.toString());
        agentConfigurationGui.getSaveAndNewButton().addActionListener(this);

        agentConfigurationGui.getCancelButton().setActionCommand(AgentConfigurationGuiEvent.CANCEL.toString());
        agentConfigurationGui.getCancelButton().addActionListener(this);

        agentConfigurationGui.getActivationRandomButton().setActionCommand(AgentConfigurationGuiEvent.SET_RANDOM_ACTIVATION.toString());
        agentConfigurationGui.getActivationRandomButton().addActionListener(this);

        agentConfigurationGui.getSatisfactionRandomButton().setActionCommand(AgentConfigurationGuiEvent.SET_RANDOM_SATISFACTION.toString());
        agentConfigurationGui.getSatisfactionRandomButton().addActionListener(this);

        agentConfigurationGui.getSatisfactionSpinner().addChangeListener(this);
        agentConfigurationGui.getActivationSpinner().addChangeListener(this);

        agentConfigurationGui.getSelectAllButton().setActionCommand(AgentConfigurationGuiEvent.SELECT_ALL.toString());
        agentConfigurationGui.getSelectAllButton().addActionListener(this);

        agentConfigurationGui.getDeselectAllButton().setActionCommand(AgentConfigurationGuiEvent.DESELECT_ALL.toString());
        agentConfigurationGui.getDeselectAllButton().addActionListener(this);

        agentConfigurationGui.getAgentTypesCombo().addItemListener(this);

        if (agentConfigurationModel != null) {
            agentConfigurationGui.getSaveAndNewButton().setVisible(false);
        }

        agentConfigurationGui.getNameField().setEditable(false);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(agentConfigurationGui.getSatisfactionSpinner())
                || e.getSource().equals(agentConfigurationGui.getActivationSpinner())) {
            eventHandler(AgentConfigurationGuiEvent.UPDATE_EMOTION);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventHandler(AgentConfigurationGuiEvent.valueOf(e.getActionCommand()));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(agentConfigurationGui.getAgentTypesCombo())) {
            eventHandler(AgentConfigurationGuiEvent.UPDATE_AGENT_TYPE);
        }
    }

    private void updateModel() {
        agentConfigurationModel.setAgentType((AgentTypeDefinitionModel) agentConfigurationGui.getAgentTypesCombo().getSelectedItem());
        agentConfigurationModel.setName(agentConfigurationGui.getNameField().getText());
        agentConfigurationModel.setActivation((Double) agentConfigurationGui.getActivationSpinner().getValue());
        agentConfigurationModel.setSatisfaction((Double) agentConfigurationGui.getSatisfactionSpinner().getValue());
        agentConfigurationModel.setStimuli(stimulusTableModel.getSelectedStimuli());
        if (callback != null) {
            callback.afterSave(agentConfigurationModel);
        }
    }

    private void eventHandler(AgentConfigurationGuiEvent event) {
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
        agentConfigurationGui.getNameField().setText(generateAgentName());
    }

    private void deselectAll() {
        stimulusTableModel.deselectStimuli();
    }

    private void selectAll() {
        stimulusTableModel.selectStimuli();
    }

    private void close() {
        agentConfigurationGui.dispose();
    }

    private void saveAndNew() {
        updateModel();
        agentConfigurationModel = null;
        initView();
    }

    private void save() {
        updateModel();
        close();
    }

    private void setRandomSatisfaction() {
        agentConfigurationGui.getSatisfactionSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
        updateEmotion();
    }

    private void setRandomActivation() {
        agentConfigurationGui.getActivationSpinner().setValue(RandomGenerator.getDouble(-1., 1.));
        updateEmotion();
    }

}
