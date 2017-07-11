/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.agenttypedefinition;

import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class AgentTypeDefinitionGui extends JDialog {

    private static final Font FONT_11 = new Font("Arial", Font.BOLD, 11);
    private static final String FIELDS_SIZE = "w 100%, h 30, wrap";
    private static final String PANELS_SIZE = "w 100%, wrap";

    private Translation translation = Translation.getInstance();
    private JComboBox<EmotionalAgentClassWrapper> agentTypesCombo;
    private JButton saveAndNewButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField agentTypeName;

    public AgentTypeDefinitionGui() {
        setModal(true);
        setTitle(translation.get("gui.emotional_agent_type_definition"));
        setSize(500, 130);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout());
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(createFieldsPanel(), PANELS_SIZE);
        mainPanel.add(createButtonsPanel(), PANELS_SIZE);

        setLocationRelativeTo(this);
    }

    private JPanel createFieldsPanel() {
        JPanel agentPanel = new JPanel(new MigLayout("insets 0"));

        agentPanel.add(new JLabel(translation.get("gui.agent_type")));

        agentTypesCombo = new JComboBox<>();
        agentPanel.add(agentTypesCombo, FIELDS_SIZE);
        agentTypesCombo.setFont(FONT_11);

        agentPanel.add(new JLabel(translation.get("gui.name")));

        agentTypeName = new JTextField();
        agentPanel.add(agentTypeName, FIELDS_SIZE);

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

    public JComboBox<EmotionalAgentClassWrapper> getAgentTypesCombo() {
        return agentTypesCombo;
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

    public JTextField getAgentTypeName() {
        return agentTypeName;
    }

}
