/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia;

import masoes.MasoesSettings;
import masoes.ontology.state.AgentState;
import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ConfiguratorAgentGui extends JFrame {

    private Translation translation;
    private AgentStateTableModel agentStateTableModel;
    private JSpinner activationIncreaseSpinner;
    private JSpinner satisfactionIncreaseSpinner;

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
        setSize(900, 600);
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

        JLabel activationIncreaseLabel = new JLabel(translation.get("gui.activation_increase"));
        westPanel.add(activationIncreaseLabel, "w 70");

        double activationIncrease = Double.parseDouble(MasoesSettings.getInstance().get(MasoesSettings.MASOES_ACTIVATION_INCREASE));

        activationIncreaseSpinner = new JSpinner();
        activationIncreaseSpinner.setModel(new SpinnerNumberModel(activationIncrease, -1., 1., .01));
        westPanel.add(activationIncreaseSpinner, "w 70, wrap");

        JLabel satisfactionIncreaseLabel = new JLabel(translation.get("gui.satisfaction_increase"));
        westPanel.add(satisfactionIncreaseLabel, "w 70");

        double satisfactionIncrease = Double.parseDouble(MasoesSettings.getInstance().get(MasoesSettings.MASOES_SATISFACTION_INCREASE));

        satisfactionIncreaseSpinner = new JSpinner();
        satisfactionIncreaseSpinner.setModel(new SpinnerNumberModel(satisfactionIncrease, -1., 1., .01));
        westPanel.add(satisfactionIncreaseSpinner, "w 70, wrap");
    }

    private void addCenterComponents() {
        JPanel centerPanel = new JPanel(new MigLayout("insets 10 0 10 10"));
        add(centerPanel, BorderLayout.CENTER);

        agentStateTableModel = new AgentStateTableModel();
        JTable agentStateTable = new JTable(agentStateTableModel);
        agentStateTable.setFillsViewportHeight(true);

        JScrollPane scrollAgentStateTable = new JScrollPane(agentStateTable);
        centerPanel.add(scrollAgentStateTable, "h 100%, w 100%");
    }

    public void closeGui() {
        setVisible(false);
        dispose();
    }

    public void setAgentStates(List<AgentState> agentStates) {
        agentStateTableModel.setAgentStates(agentStates);
    }

    public void showGui() {
        setVisible(true);
    }

    public void addActionListener(ActionListener actionListener) {
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
