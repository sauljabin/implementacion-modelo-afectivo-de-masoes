/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RequesterGui extends JFrame {

    private JPanel westPanel;
    private JPanel centerPanel;
    private JPanel settingsPanel;
    private JLabel senderAgentLabel;
    private JLabel senderAgentTitleLabel;
    private JLabel receiverAgentTitleLabel;
    private JTextField receiverAgentTextField;
    private JButton sendRequestButton;
    private JTabbedPane messageTabbedPane;
    private JComboBox settingsActionsComboBox;
    private JLabel settingsActionsLabel;
    private JLabel settingsKeyLabel;
    private JTextField settingsKeyTextField;
    private JTextPane messageTextPane;
    private JScrollPane messageScrollPane;

    public RequesterGui() {
        setUp();
    }

    public static void main(String[] args) {
        RequesterGui requesterGui = new RequesterGui();
        requesterGui.showGui();
    }

    private void setUp() {
        setTitle("Requester GUI");
        setSize(1024, 768);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        addComponents();
        setLocationRelativeTo(this);
    }

    private void addComponents() {
        createPrincipalPanels();
        createAgentsComponents();
        createSettingsComponents();
        createMessageLogComponent();
        createSenderButton();
    }

    private void createPrincipalPanels() {
        westPanel = new JPanel(new MigLayout("insets 10"));
        add(westPanel, BorderLayout.WEST);
        centerPanel = new JPanel(new MigLayout("insets 10 0 10 10"));
        add(centerPanel, BorderLayout.CENTER);
    }

    private void createAgentsComponents() {
        senderAgentTitleLabel = new JLabel("Sender Agent:");
        westPanel.add(senderAgentTitleLabel, "w 250, wrap");

        senderAgentLabel = new JLabel("requester");
        senderAgentLabel.setForeground(Color.BLUE);
        westPanel.add(senderAgentLabel, "grow, wrap");

        receiverAgentTitleLabel = new JLabel("Receiver Agent:");
        westPanel.add(receiverAgentTitleLabel, "grow, wrap");

        receiverAgentTextField = new JTextField();
        westPanel.add(receiverAgentTextField, "grow, wrap");

        messageTabbedPane = new JTabbedPane();
        westPanel.add(messageTabbedPane, "grow, wrap 20");
    }

    private void createSettingsComponents() {
        settingsPanel = new JPanel(new MigLayout("insets 10"));
        messageTabbedPane.addTab("Settings", settingsPanel);

        settingsActionsLabel = new JLabel("Action:");
        settingsPanel.add(settingsActionsLabel, "w 30");

        settingsActionsComboBox = new JComboBox();
        settingsPanel.add(settingsActionsComboBox, "w 100%, wrap");

        settingsKeyLabel = new JLabel("Key:");
        settingsPanel.add(settingsKeyLabel, "grow");

        settingsKeyTextField = new JTextField();
        settingsPanel.add(settingsKeyTextField, "grow, wrap");
    }

    private void createMessageLogComponent() {
        messageTextPane = new JTextPane();
        messageScrollPane = new JScrollPane(messageTextPane);
        centerPanel.add(messageScrollPane, "h 100%, w 100%");
    }

    private void createSenderButton() {
        sendRequestButton = new JButton("Send Request");
        sendRequestButton.setActionCommand(RequesterGuiAction.SEND_MESSAGE.toString());
        westPanel.add(sendRequestButton, "h 30, grow");
    }

    public void closeGui() {
        setVisible(false);
        dispose();
    }

    public void showGui() {
        setVisible(true);
    }

    public void addActionListener(ActionListener actionListener) {
        sendRequestButton.addActionListener(actionListener);
    }

    public String getReceiverAgentName() {
        return receiverAgentTextField.getText();
    }

}
