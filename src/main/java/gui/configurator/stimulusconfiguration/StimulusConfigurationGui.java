/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.configurator.stimulusconfiguration;

import net.miginfocom.swing.MigLayout;
import translate.Translation;

import javax.swing.*;
import java.awt.*;

public class StimulusConfigurationGui extends JFrame {

    private static final String FIELDS_SIZE = "w 100%, h 30, wrap";
    private static final String PANELS_SIZE = "w 100%, wrap";

    private Translation translation = Translation.getInstance();

    private JTextField nameField;
    private JTextField valueField;
    private JSpinner activationSpinner;
    private JSpinner satisfactionSpinner;
    private JButton saveButton;
    private JButton cancelButton;
    private JRadioButton selfButton;
    private JRadioButton othersButton;

    public StimulusConfigurationGui() {
        setTitle(translation.get("gui.stimulus_definition"));
        setSize(500, 230);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new MigLayout());
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(createFieldsPanel(), PANELS_SIZE);

        mainPanel.add(createRadioButtonsPanel(), PANELS_SIZE);

        mainPanel.add(createButtonsPanel(), PANELS_SIZE);

        setLocationRelativeTo(this);
    }

    private JPanel createFieldsPanel() {
        JPanel fieldsPanel = new JPanel(new MigLayout("insets 0"));

        JLabel nameLabel = new JLabel(translation.get("gui.name"));
        fieldsPanel.add(nameLabel);

        nameField = new JTextField();
        fieldsPanel.add(nameField, FIELDS_SIZE);

        JLabel valueLabel = new JLabel(translation.get("gui.value"));
        fieldsPanel.add(valueLabel);

        valueField = new JTextField();
        fieldsPanel.add(valueField, FIELDS_SIZE);

        JLabel activationLabel = new JLabel(translation.get("gui.activation_parameter"));
        fieldsPanel.add(activationLabel);

        activationSpinner = new JSpinner(new SpinnerNumberModel(0, -1., 1., .01));
        fieldsPanel.add(activationSpinner, FIELDS_SIZE);

        JLabel satisfactionLabel = new JLabel(translation.get("gui.satisfaction_parameter"));
        fieldsPanel.add(satisfactionLabel);

        satisfactionSpinner = new JSpinner(new SpinnerNumberModel(0, -1., 1., .01));
        fieldsPanel.add(satisfactionSpinner, FIELDS_SIZE);

        return fieldsPanel;
    }

    private JPanel createRadioButtonsPanel() {
        JPanel radioButtonsPanel = new JPanel(new MigLayout("insets 0"));

        selfButton = new JRadioButton(translation.get("gui.self"));
        radioButtonsPanel.add(selfButton);

        othersButton = new JRadioButton(translation.get("gui.others"));
        radioButtonsPanel.add(othersButton);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(selfButton);
        buttonGroup.add(othersButton);

        return radioButtonsPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();

        saveButton = new JButton(translation.get("gui.save"));
        buttonsPanel.add(saveButton);

        cancelButton = new JButton(translation.get("gui.cancel"));
        buttonsPanel.add(cancelButton);

        return buttonsPanel;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getValueField() {
        return valueField;
    }

    public JSpinner getActivationSpinner() {
        return activationSpinner;
    }

    public JSpinner getSatisfactionSpinner() {
        return satisfactionSpinner;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JRadioButton getSelfButton() {
        return selfButton;
    }

    public JRadioButton getOthersButton() {
        return othersButton;
    }

}
