/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import masoes.MasoesSettings;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalSpace;
import masoes.component.behavioural.EmotionalState;
import masoes.ontology.state.AgentState;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfiguratorAgentGui extends JFrame {

    private JPanel centerPanel;
    private Translation translation;

    private JComboBox<Object> agentTypesToAddCombo;
    private AgentsStateTableModel agentsStateTableModel;
    private AgentsToAddTableModel agentsToAddTableModel;
    private JSpinner activationIncreaseSpinner;
    private JSpinner satisfactionIncreaseSpinner;
    private JSpinner activationToAddSpinner;
    private JSpinner satisfactionToAddSpinner;
    private JButton startButton;
    private JButton cleanButton;
    private JButton addAgentButton;
    private JButton removeAgentButton;
    private JLabel emotionToAddLabel;
    private JTable agentsToAddTable;

    public ConfiguratorAgentGui() {
        translation = Translation.getInstance();
        setUp();
    }

    public static void main(String[] args) {
        ConfiguratorAgentGui configuratorAgentGui = new ConfiguratorAgentGui();
        configuratorAgentGui.showGui();
    }

    private void setUp() {
        setTitle(translation.get("gui.configurator"));
        setSize(1024, 768);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        addComponents();
        setLocationRelativeTo(this);
    }

    private void addComponents() {
        addEastComponents();
        addCenterComponents();
    }

    private void addEastComponents() {
        JPanel westPanel = new JPanel(new MigLayout("insets 10"));
        add(westPanel, BorderLayout.EAST);

        startButton = new JButton(translation.get("gui.start"));
        westPanel.add(startButton, "grow, h 25, wrap");

        cleanButton = new JButton(translation.get("gui.clean"));
        westPanel.add(cleanButton, "grow, h 25");
    }

    private void addCenterComponents() {
        centerPanel = new JPanel(new MigLayout("insets 10 10 10 0"));
        add(centerPanel, BorderLayout.CENTER);
        addGlobalVariablesComponents();
        addInitialAgentConfigurationComponents();
        addCurrentStateComponents();
    }

    private void addCurrentStateComponents() {
        JPanel currentStatePanel = new JPanel(new MigLayout("insets 0 5 0 5"));
        currentStatePanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.current_emotional_states")));
        centerPanel.add(currentStatePanel, "w 100%, h 60%");

        agentsStateTableModel = new AgentsStateTableModel();
        JTable agentStateTable = new JTable(agentsStateTableModel);
        agentStateTable.setFillsViewportHeight(true);

        JScrollPane scrollAgentStateTable = new JScrollPane(agentStateTable);
        currentStatePanel.add(scrollAgentStateTable, "h 100%, w 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        currentStatePanel.add(buttonsPanel, "h 100%, wrap");

        JButton windowAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/window.png")));
        buttonsPanel.add(windowAgentButton, "w 25, h 25");
    }

    private void addInitialAgentConfigurationComponents() {
        JPanel initialAgentConfigurationPanel = new JPanel(new MigLayout("insets 5 5 0 5"));
        initialAgentConfigurationPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.initial_agent_configuration")));
        centerPanel.add(initialAgentConfigurationPanel, "w 100%, h 40%, wrap");

        JPanel configAgentPanel = new JPanel(new MigLayout("insets 0"));
        initialAgentConfigurationPanel.add(configAgentPanel, "w 100%, span 2, wrap");

        agentTypesToAddCombo = new JComboBox<>(AgentTypeToAdd.values());
        configAgentPanel.add(agentTypesToAddCombo);

        configAgentPanel.add(new JLabel(translation.get("gui.activation_x")));

        EmotionalState initialEmotionalState = new EmotionalState(.5, .5);
        Emotion initialEmotion = new EmotionalSpace().searchEmotion(initialEmotionalState);

        activationToAddSpinner = new JSpinner();
        activationToAddSpinner.setModel(new SpinnerNumberModel(initialEmotionalState.getActivation(), -1., 1., .01));
        configAgentPanel.add(activationToAddSpinner, "w 70");

        configAgentPanel.add(new JLabel(translation.get("gui.satisfaction_y")));

        satisfactionToAddSpinner = new JSpinner();
        satisfactionToAddSpinner.setModel(new SpinnerNumberModel(initialEmotionalState.getSatisfaction(), -1., 1., .01));
        configAgentPanel.add(satisfactionToAddSpinner, "w 70");

        emotionToAddLabel = new JLabel();
        emotionToAddLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        emotionToAddLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        emotionToAddLabel.setBackground(new Color(210, 210, 210));
        emotionToAddLabel.setOpaque(true);
        emotionToAddLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setEmotionToAdd(initialEmotion);

        configAgentPanel.add(emotionToAddLabel, "h 20, w 200");

        addAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/add-icon.png")));
        configAgentPanel.add(addAgentButton, "w 25, h 25");

        agentsToAddTableModel = new AgentsToAddTableModel();
        agentsToAddTable = new JTable(agentsToAddTableModel);
        agentsToAddTable.setFillsViewportHeight(true);

        JScrollPane scrollAgentsToAddTable = new JScrollPane(agentsToAddTable);
        initialAgentConfigurationPanel.add(scrollAgentsToAddTable, "h 100%, w 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        initialAgentConfigurationPanel.add(buttonsPanel, "h 100%, wrap");

        removeAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/delete-icon.png")));
        buttonsPanel.add(removeAgentButton, "w 25, h 25, wrap");
    }

    private void addGlobalVariablesComponents() {
        JPanel globalVariablesPanel = new JPanel(new MigLayout("insets 5"));
        globalVariablesPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.variables")));

        centerPanel.add(globalVariablesPanel, "w 100%, wrap");

        JLabel activationIncreaseLabel = new JLabel(translation.get("gui.activation_increase"));
        globalVariablesPanel.add(activationIncreaseLabel, "w 70");

        double activationIncrease = Double.parseDouble(MasoesSettings.getInstance().get(MasoesSettings.MASOES_ACTIVATION_INCREASE));

        activationIncreaseSpinner = new JSpinner();
        activationIncreaseSpinner.setModel(new SpinnerNumberModel(activationIncrease, 0., 1., .01));
        globalVariablesPanel.add(activationIncreaseSpinner, "w 70");

        JLabel satisfactionIncreaseLabel = new JLabel(translation.get("gui.satisfaction_increase"));
        globalVariablesPanel.add(satisfactionIncreaseLabel, "w 70");

        double satisfactionIncrease = Double.parseDouble(MasoesSettings.getInstance().get(MasoesSettings.MASOES_SATISFACTION_INCREASE));

        satisfactionIncreaseSpinner = new JSpinner();
        satisfactionIncreaseSpinner.setModel(new SpinnerNumberModel(satisfactionIncrease, 0., 1., .01));
        globalVariablesPanel.add(satisfactionIncreaseSpinner, "w 70, wrap");
    }

    public void closeGui() {
        setVisible(false);
        dispose();
    }

    public void setAgentStates(List<AgentState> agentStates) {
        agentsStateTableModel.setAgentStates(agentStates);
    }

    public void showGui() {
        setVisible(true);
    }

    public void addActionListener(ActionListener actionListener) {
        activationIncreaseSpinner.addChangeListener(e -> actionListener.actionPerformed(new ActionEvent(
                e.getSource(),
                ConfiguratorAgentEvent.UPDATE_ACTIVATION_INCREASE.getInt(),
                ConfiguratorAgentEvent.UPDATE_ACTIVATION_INCREASE.toString()
        )));

        satisfactionIncreaseSpinner.addChangeListener(e -> actionListener.actionPerformed(new ActionEvent(
                e.getSource(),
                ConfiguratorAgentEvent.UPDATE_SATISFACTION_INCREASE.getInt(),
                ConfiguratorAgentEvent.UPDATE_SATISFACTION_INCREASE.toString()
        )));

        activationToAddSpinner.addChangeListener(e -> actionListener.actionPerformed(new ActionEvent(
                e.getSource(),
                ConfiguratorAgentEvent.UPDATE_ACTIVATION_TO_ADD.getInt(),
                ConfiguratorAgentEvent.UPDATE_ACTIVATION_TO_ADD.toString()
        )));

        satisfactionToAddSpinner.addChangeListener(e -> actionListener.actionPerformed(new ActionEvent(
                e.getSource(),
                ConfiguratorAgentEvent.UPDATE_SATISFACTION_TO_ADD.getInt(),
                ConfiguratorAgentEvent.UPDATE_SATISFACTION_TO_ADD.toString()
        )));

        startButton.setActionCommand(ConfiguratorAgentEvent.START.toString());
        startButton.addActionListener(actionListener);

        cleanButton.setActionCommand(ConfiguratorAgentEvent.CLEAN.toString());
        cleanButton.addActionListener(actionListener);

        addAgentButton.setActionCommand(ConfiguratorAgentEvent.ADD_AGENT.toString());
        addAgentButton.addActionListener(actionListener);

        removeAgentButton.setActionCommand(ConfiguratorAgentEvent.REMOVE_AGENTS.toString());
        removeAgentButton.addActionListener(actionListener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public double getActivationIncrease() {
        return (double) activationIncreaseSpinner.getValue();
    }

    public double getSatisfactionIncrease() {
        return (double) satisfactionIncreaseSpinner.getValue();
    }

    public double getActivationToAdd() {
        return (double) activationToAddSpinner.getValue();
    }

    public double getSatisfactionToAdd() {
        return (double) satisfactionToAddSpinner.getValue();
    }

    public AgentTypeToAdd getAgentTypeToAdd() {
        return (AgentTypeToAdd) agentTypesToAddCombo.getSelectedItem();
    }

    public void setEmotionToAdd(Emotion emotion) {
        emotionToAddLabel.setText(String.format("%s - %s", translation.get(emotion.getName().toLowerCase()), translation.get(emotion.getType().toString().toLowerCase())));
    }

    public void addAgentToAdd(AgentToAdd agentToAdd) {
        agentsToAddTableModel.addAgentToAdd(agentToAdd);
    }

    public void removeAgentToAdd(AgentToAdd agentToAdd) {
        agentsToAddTableModel.removeAgentToAdd(agentToAdd);
    }

    public List<AgentToAdd> getAgentsToAdd() {
        return agentsToAddTableModel.getAgentsToAdd();
    }

    public List<AgentToAdd> getSelectedAgentToAdd() {
        List<AgentToAdd> agentToAdds = new ArrayList<>();
        Arrays.stream(agentsToAddTable.getSelectedRows())
                .forEach(index -> agentToAdds.add(agentsToAddTableModel.getAgentsToAdd().get(index)));
        return agentToAdds;
    }

}
