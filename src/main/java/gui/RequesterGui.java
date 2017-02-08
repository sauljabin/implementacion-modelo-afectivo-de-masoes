/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.lang.acl.ACLMessage;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class RequesterGui extends JFrame {

    private static final int CONVERSATIONS_CACHE = 10;
    private JPanel westPanel;
    private JPanel centerPanel;
    private JLabel senderAgentLabel;
    private JLabel senderAgentTitleLabel;
    private JLabel receiverAgentTitleLabel;
    private JTextField receiverAgentTextField;
    private JButton sendRequestButton;
    private JPanel dynamicCanvas;
    private JComboBox<RequesterGuiAction> actionsComboBox;
    private JLabel actionsLabel;
    private JTextPane messageTextPane;
    private JScrollPane messageTextScrollPane;
    private JPanel messageTextWrapPanel;

    private List<Color> colors;
    private Iterator<Color> colorIterator;
    private LinkedHashMap<String, Color> conversations;

    public RequesterGui() {
        prepareColors();
        setUp();
    }

    public static void main(String[] args) {
        RequesterGui requesterGui = new RequesterGui();
        requesterGui.showGui();
    }

    private void prepareColors() {
        colors = Arrays.asList(new Color(30, 90, 255), new Color(255, 10, 20), new Color(250, 10, 255), new Color(0, 200, 100), new Color(255, 110, 40));
        colorIterator = colors.iterator();
        conversations = new LinkedHashMap<>();
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
        senderAgentTitleLabel = new JLabel("Sender agent:");
        westPanel.add(senderAgentTitleLabel, "w 110");

        senderAgentLabel = new JLabel();
        senderAgentLabel.setForeground(Color.BLUE);
        westPanel.add(senderAgentLabel, "w 150, wrap");

        receiverAgentTitleLabel = new JLabel("Receiver agent:");
        westPanel.add(receiverAgentTitleLabel, "grow");

        receiverAgentTextField = new JTextField();
        westPanel.add(receiverAgentTextField, "grow, wrap");

        actionsLabel = new JLabel("Action:");
        westPanel.add(actionsLabel, "grow");

        actionsComboBox = new JComboBox<>(RequesterGuiAction.values());
        actionsComboBox.setActionCommand(RequesterGuiEvent.CHANGE_ACTION.toString());
        westPanel.add(actionsComboBox, "grow, wrap");

        dynamicCanvas = new JPanel();
        westPanel.add(dynamicCanvas, "span 2, grow, wrap 20");
    }

    private void createMessageLogComponent() {
        messageTextPane = new JTextPane();
        messageTextPane.setEditable(false);
        messageTextWrapPanel = new JPanel(new BorderLayout());
        messageTextWrapPanel.add(messageTextPane);
        messageTextScrollPane = new JScrollPane(messageTextWrapPanel);
        centerPanel.add(messageTextScrollPane, "h 100%, w 100%");
    }

    private void createSenderButton() {
        sendRequestButton = new JButton("Send request");
        sendRequestButton.setActionCommand(RequesterGuiEvent.SEND_MESSAGE.toString());
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
        actionsComboBox.addActionListener(actionListener);
    }

    public void setSenderAgentName(String name) {
        senderAgentLabel.setText(name);
    }

    public RequesterGuiAction getSelectedAction() {
        return (RequesterGuiAction) actionsComboBox.getSelectedItem();
    }

    public String getReceiverAgentName() {
        return receiverAgentTextField.getText();
    }

    public void logMessage(ACLMessage message) {

        if (!colorIterator.hasNext()) {
            colorIterator = colors.iterator();
        }

        if (conversations.get(message.getConversationId()) == null) {
            if (conversations.size() >= CONVERSATIONS_CACHE) {
                String key = conversations.keySet().iterator().next();
                conversations.remove(key);
            }
            conversations.put(message.getConversationId(), colorIterator.next());
        }

        StyledDocument document = messageTextPane.getStyledDocument();
        try {
            SimpleAttributeSet attributeSet = new SimpleAttributeSet();

            attributeSet.addAttribute(StyleConstants.Bold, Boolean.TRUE);
            attributeSet.addAttribute(StyleConstants.Foreground, conversations.get(message.getConversationId()));
            document.insertString(document.getLength(), "Conversation: " + message.getConversationId() + "\n", attributeSet);

            attributeSet.addAttribute(StyleConstants.Bold, Boolean.FALSE);
            attributeSet.addAttribute(StyleConstants.Foreground, Color.BLACK);
            document.insertString(document.getLength(), message.toString() + "\n\n", attributeSet);
        } catch (Exception e) {
            throw new GuiException(e);
        }
    }

}
