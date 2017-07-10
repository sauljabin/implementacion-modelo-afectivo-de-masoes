/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.agenttypedefinition;

import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class AgentTypeDefinitionCrudGui extends JFrame {

    private Translation translation = Translation.getInstance();
    private JButton addAgentButton;
    private JButton deleteAgentButton;
    private JButton editAgentButton;
    private JButton cancelButton;
    private JTable agentTypesTable;

    public AgentTypeDefinitionCrudGui() {
        setTitle(translation.get("gui.emotional_agent_types_definition"));
        setSize(600, 350);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout());
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(createTablePanel(), "w 100%, h 100%");

        mainPanel.add(createButtonsPanel(), "h 100%, wrap");

        mainPanel.add(createCancelPanel(), "span 2, w 100%");

        setLocationRelativeTo(this);
    }

    private JPanel createCancelPanel() {
        JPanel cancelPanel = new JPanel();
        cancelButton = new JButton(translation.get("gui.cancel"));
        cancelPanel.add(cancelButton);
        return cancelPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));

        addAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/plus.png")));
        buttonsPanel.add(addAgentButton, "wrap");

        deleteAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/minus.png")));
        buttonsPanel.add(deleteAgentButton, "wrap");

        editAgentButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/edit.png")));
        buttonsPanel.add(editAgentButton);

        return buttonsPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new MigLayout("insets 0"));

        agentTypesTable = new JTable();
        JScrollPane scrollTable = new JScrollPane(agentTypesTable);
        tablePanel.add(scrollTable, "w 100%, h 100%");

        return tablePanel;
    }

    public JButton getAddAgentButton() {
        return addAgentButton;
    }

    public JButton getDeleteAgentButton() {
        return deleteAgentButton;
    }

    public JButton getEditAgentButton() {
        return editAgentButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JTable getAgentTypesTable() {
        return agentTypesTable;
    }

}
