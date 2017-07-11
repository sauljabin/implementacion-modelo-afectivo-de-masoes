/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator.stimulusdefinition;

import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class StimulusDefinitionCrudGui extends JFrame {

    private Translation translation = Translation.getInstance();
    private JTable stimuliTable;
    private JButton cancelButton;
    private JButton addStimulusButton;
    private JButton deleteStimulusButton;
    private JButton editStimulusButton;

    public StimulusDefinitionCrudGui() {
        setTitle(translation.get("gui.stimuli_definition"));
        setSize(750, 350);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout());
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(createTablePanel(), "w 100%, h 100%");

        mainPanel.add(createButtonsPanel(), "h 100%, wrap");

        mainPanel.add(createCancelPanel(), "span 2, w 100%");

        setLocationRelativeTo(this);
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new MigLayout("insets 0"));

        stimuliTable = new JTable();
        JScrollPane scrollTable = new JScrollPane(stimuliTable);
        tablePanel.add(scrollTable, "w 100%, h 100%");

        return tablePanel;
    }

    private JPanel createCancelPanel() {
        JPanel cancelPanel = new JPanel();
        cancelButton = new JButton(translation.get("gui.cancel"));
        cancelPanel.add(cancelButton);
        return cancelPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new MigLayout("insets 0"));

        addStimulusButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/plus.png")));
        buttonsPanel.add(addStimulusButton, "wrap");

        deleteStimulusButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/minus.png")));
        buttonsPanel.add(deleteStimulusButton, "wrap");

        editStimulusButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("images/edit.png")));
        buttonsPanel.add(editStimulusButton);

        return buttonsPanel;
    }

    public JTable getStimuliTable() {
        return stimuliTable;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getAddStimulusButton() {
        return addStimulusButton;
    }

    public JButton getDeleteStimulusButton() {
        return deleteStimulusButton;
    }

    public JButton getEditStimulusButton() {
        return editStimulusButton;
    }

}
