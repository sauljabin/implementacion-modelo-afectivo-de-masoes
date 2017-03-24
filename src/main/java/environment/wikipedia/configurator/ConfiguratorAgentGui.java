/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import masoes.MasoesSettings;
import masoes.component.behavioural.EmotionalSpace;
import masoes.ontology.state.AgentState;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConfiguratorAgentGui extends JFrame {

    private AgentsStateTableModel agentsStateTableModel;
    private AgentsToAddTableModel agentsToAddTableModel;
    private JSpinner activationIncreaseSpinner;
    private JSpinner satisfactionIncreaseSpinner;
    private JButton startButton;
    private JButton cleanButton;

    private Translation translation;

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
        addWestComponents();
        addCenterComponents();
    }

    private void addWestComponents() {
        JPanel westPanel = new JPanel(new MigLayout("insets 10"));
        add(westPanel, BorderLayout.WEST);

        JPanel globalVariablesPanel = new JPanel(new MigLayout("insets 10"));
        globalVariablesPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.variables")));

        westPanel.add(globalVariablesPanel, "wrap");

        JLabel activationIncreaseLabel = new JLabel(translation.get("gui.activation_increase"));
        globalVariablesPanel.add(activationIncreaseLabel, "w 70");

        double activationIncrease = Double.parseDouble(MasoesSettings.getInstance().get(MasoesSettings.MASOES_ACTIVATION_INCREASE));

        activationIncreaseSpinner = new JSpinner();
        activationIncreaseSpinner.setModel(new SpinnerNumberModel(activationIncrease, 0., 1., .01));
        globalVariablesPanel.add(activationIncreaseSpinner, "w 70, wrap");

        JLabel satisfactionIncreaseLabel = new JLabel(translation.get("gui.satisfaction_increase"));
        globalVariablesPanel.add(satisfactionIncreaseLabel, "w 70");

        double satisfactionIncrease = Double.parseDouble(MasoesSettings.getInstance().get(MasoesSettings.MASOES_SATISFACTION_INCREASE));

        satisfactionIncreaseSpinner = new JSpinner();
        satisfactionIncreaseSpinner.setModel(new SpinnerNumberModel(satisfactionIncrease, 0., 1., .01));
        globalVariablesPanel.add(satisfactionIncreaseSpinner, "w 70, wrap");

        startButton = new JButton(translation.get("gui.start"));
        westPanel.add(startButton, "grow, h 25, wrap");

        cleanButton = new JButton(translation.get("gui.clean"));
        westPanel.add(cleanButton, "grow, h 25");
    }

    private void addCenterComponents() {
        JPanel centerPanel = new JPanel(new MigLayout("insets 10 0 10 10"));
        add(centerPanel, BorderLayout.CENTER);

        JPanel addAgentsPanel = new JPanel(new MigLayout("insets 0 5 0 5"));
        addAgentsPanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.add_agents")));
        centerPanel.add(addAgentsPanel, "w 100%, h 40%, wrap");

        JPanel configAgentPanel = new JPanel(new MigLayout("insets 0"));
        addAgentsPanel.add(configAgentPanel, "w 100%, span 2, wrap");

        JComboBox<Object> agentTypesCombo = new JComboBox<>(AgentToAddType.values());
        configAgentPanel.add(agentTypesCombo);

        Object[] emotions = new EmotionalSpace().getEmotions()
                .stream()
                .map(emotion -> new EmotionToAdd(emotion))
                .toArray();

        JComboBox<Object> emotionCombo = new JComboBox<>(emotions);
        configAgentPanel.add(emotionCombo);

        JButton addAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/add-icon.png")));
        configAgentPanel.add(addAgentButton, "w 25, h 25");

        agentsToAddTableModel = new AgentsToAddTableModel();
        JTable agentsToAddTable = new JTable(agentsToAddTableModel);
        agentsToAddTable.setFillsViewportHeight(true);

        JScrollPane scrollAgentsToAddTable = new JScrollPane(agentsToAddTable);
        addAgentsPanel.add(scrollAgentsToAddTable, "h 100%, w 100%");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));
        addAgentsPanel.add(buttonsPanel, "h 100%, wrap");

        JButton removeAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/delete-icon.png")));
        buttonsPanel.add(removeAgentButton, "w 25, h 25, wrap");

        JButton windowAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/window.png")));
        buttonsPanel.add(windowAgentButton, "w 25, h 25");

        JPanel currentStatePanel = new JPanel(new MigLayout("insets 0 5 5 5"));
        currentStatePanel.setBorder(BorderFactory.createTitledBorder(translation.get("gui.current_emotional_states")));
        centerPanel.add(currentStatePanel, "w 100%, h 60%");

        agentsStateTableModel = new AgentsStateTableModel();
        JTable agentStateTable = new JTable(agentsStateTableModel);
        agentStateTable.setFillsViewportHeight(true);

        JScrollPane scrollAgentStateTable = new JScrollPane(agentStateTable);
        currentStatePanel.add(scrollAgentStateTable, "h 100%, w 100%");
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

        startButton.setActionCommand(ConfiguratorAgentEvent.START.toString());
        startButton.addActionListener(actionListener);

        cleanButton.setActionCommand(ConfiguratorAgentEvent.CLEAN.toString());
        cleanButton.addActionListener(actionListener);
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

}
