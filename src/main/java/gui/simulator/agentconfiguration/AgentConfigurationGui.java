/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.agentconfiguration;

import gui.simulator.agenttypedefinition.AgentTypeDefinitionModel;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class AgentConfigurationGui extends JFrame {

    private static final String FIELDS_SIZE = "w 100%, h 30, wrap";
    private static final String EMOTION_FIELDS_SIZE = "w 100%, h 30";
    private static final String PANELS_SIZE = "w 100%, wrap";
    private static final String TABLE_PANEL_SIZE = PANELS_SIZE + ", h 150";

    private Translation translation = Translation.getInstance();
    private JComboBox<AgentTypeDefinitionModel> agentTypesCombo;
    private JTextField nameField;
    private JSpinner activationSpinner;
    private JLabel emotionLabel;
    private JSpinner satisfactionSpinner;
    private JButton activationRandomButton;
    private JButton satisfactionRandomButton;
    private JButton saveAndNewButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton selectAllButton;
    private JButton deselectAllButton;
    private JTable stimuliTable;
    private JButton configStimulusButton;

    public AgentConfigurationGui() {
        setTitle(translation.get("gui.agent"));
        setSize(600, 430);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout());
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(createAgentFieldsPanel(), PANELS_SIZE);

        mainPanel.add(createInitialEmotionPanel(), PANELS_SIZE);

        mainPanel.add(createStimuliPanel(), TABLE_PANEL_SIZE);

        mainPanel.add(createButtonsPanel(), PANELS_SIZE);

        setLocationRelativeTo(this);
    }

    private JPanel createStimuliPanel() {
        JPanel tablePanel = new JPanel(new MigLayout("insets 0"));

        stimuliTable = new JTable();
        JScrollPane scrollAgentsToAddTable = new JScrollPane(stimuliTable);
        tablePanel.add(scrollAgentsToAddTable, "h 100%, w 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        tablePanel.add(buttonsPanel, "h 100%, wrap");

        selectAllButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/select-all.png")));
        buttonsPanel.add(selectAllButton, "wrap");

        deselectAllButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/deselect-all.png")));
        buttonsPanel.add(deselectAllButton, "wrap 20");

        configStimulusButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/config.png")));
        buttonsPanel.add(configStimulusButton);

        return tablePanel;
    }

    private JPanel createInitialEmotionPanel() {
        JPanel emotionPanel = new JPanel(new MigLayout("insets 0"));

        JLabel activationLabel = new JLabel(translation.get("gui.activation_x"));
        emotionPanel.add(activationLabel);

        activationSpinner = new JSpinner();
        activationSpinner.setModel(new SpinnerNumberModel(0.0, -1., 1., .01));
        emotionPanel.add(activationSpinner, EMOTION_FIELDS_SIZE);

        activationRandomButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/random.png")));
        activationRandomButton.setMaximumSize(new Dimension(30, 30));
        emotionPanel.add(activationRandomButton, "h 30, w 30, wrap");

        JLabel satisfactionLabel = new JLabel(translation.get("gui.satisfaction_y"));
        emotionPanel.add(satisfactionLabel);

        satisfactionSpinner = new JSpinner();
        satisfactionSpinner.setModel(new SpinnerNumberModel(0.0, -1., 1., .01));
        emotionPanel.add(satisfactionSpinner, EMOTION_FIELDS_SIZE);

        satisfactionRandomButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/random.png")));
        satisfactionRandomButton.setMaximumSize(new Dimension(30, 30));
        emotionPanel.add(satisfactionRandomButton, "h 30, w 30, wrap");

        emotionLabel = new JLabel();
        emotionLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        emotionLabel.setBackground(new Color(210, 210, 210));
        emotionLabel.setOpaque(true);
        emotionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emotionPanel.add(emotionLabel, "h 50, w 100%, span 3, wrap");

        return emotionPanel;
    }

    private JPanel createAgentFieldsPanel() {
        JPanel agentPanel = new JPanel(new MigLayout("insets 0"));

        JLabel agentLabel = new JLabel(translation.get("gui.agent_type"));
        agentPanel.add(agentLabel);

        agentTypesCombo = new JComboBox<>();
        agentPanel.add(agentTypesCombo, FIELDS_SIZE);

        JLabel nameLabel = new JLabel(translation.get("gui.name"));
        agentPanel.add(nameLabel);

        nameField = new JTextField();
        agentPanel.add(nameField, FIELDS_SIZE);

        return agentPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();

        saveAndNewButton = new JButton(translation.get("gui.save_and_new"));
        buttonsPanel.add(saveAndNewButton);

        saveButton = new JButton(translation.get("gui.save"));
        buttonsPanel.add(saveButton);

        cancelButton = new JButton(translation.get("gui.cancel"));
        buttonsPanel.add(cancelButton);

        return buttonsPanel;
    }

    public JComboBox<AgentTypeDefinitionModel> getAgentTypesCombo() {
        return agentTypesCombo;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JSpinner getActivationSpinner() {
        return activationSpinner;
    }

    public JLabel getEmotionLabel() {
        return emotionLabel;
    }

    public JSpinner getSatisfactionSpinner() {
        return satisfactionSpinner;
    }

    public JButton getActivationRandomButton() {
        return activationRandomButton;
    }

    public JButton getSatisfactionRandomButton() {
        return satisfactionRandomButton;
    }

    public JButton getSaveAndNewButton() {
        return saveAndNewButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getSelectAllButton() {
        return selectAllButton;
    }

    public JButton getDeselectAllButton() {
        return deselectAllButton;
    }

    public JTable getStimuliTable() {
        return stimuliTable;
    }

    public JButton getConfigStimulusButton() {
        return configStimulusButton;
    }

}
