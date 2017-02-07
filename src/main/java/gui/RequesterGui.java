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
    private JLabel senderAgentLabel;
    private JLabel senderAgentTitleLabel;
    private JLabel receiverAgentTitleLabel;
    private JTextField receiverAgentTextField;
    private JButton sendRequestButton;
    private JPanel dynamicCanvan;
    private JComboBox actionsComboBox;
    private JLabel actionsLabel;
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
        westPanel.add(senderAgentTitleLabel, "w 110");

        senderAgentLabel = new JLabel("requester");
        senderAgentLabel.setForeground(Color.BLUE);
        westPanel.add(senderAgentLabel, "w 150, wrap");

        receiverAgentTitleLabel = new JLabel("Receiver Agent:");
        westPanel.add(receiverAgentTitleLabel, "grow");

        receiverAgentTextField = new JTextField();
        westPanel.add(receiverAgentTextField, "grow, wrap");

        actionsLabel = new JLabel("Action:");
        westPanel.add(actionsLabel, "grow");

        actionsComboBox = new JComboBox();
        westPanel.add(actionsComboBox, "grow, wrap");

        dynamicCanvan = new JPanel();
        westPanel.add(dynamicCanvan, "span 2, grow, wrap 20");
    }

    private void createMessageLogComponent() {
        messageTextPane = new JTextPane();
        messageScrollPane = new JScrollPane(messageTextPane);
        centerPanel.add(messageScrollPane, "h 100%, w 100%");
    }

    private void createSenderButton() {
        sendRequestButton = new JButton("Send Request");
        sendRequestButton.setActionCommand(RequesterGuiAction.SEND_MESSAGE.toString());
        westPanel.add(sendRequestButton, "span 2, h 30, grow");
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

    public void setSenderAgentName(String name) {
        senderAgentLabel.setText(name);
    }

}
