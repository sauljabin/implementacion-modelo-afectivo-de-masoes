/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import jade.lang.acl.ACLMessage;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
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

    private static final int CONVERSATIONS_CACHE = 100;
    private JPanel westPanel;
    private JPanel centerPanel;
    private JLabel senderAgentLabel;
    private JTextField receiverAgentTextField;
    private JButton sendRequestButton;
    private JPanel dynamicCanvasPanel;
    private JComboBox<RequesterGuiAction> actionsComboBox;
    private JTextPane messageTextPane;
    private List<Color> colors;
    private Iterator<Color> colorIterator;
    private LinkedHashMap<String, Color> conversations;
    private JButton saveMessagesLogButton;
    private JButton clearMessagesLogButton;
    private JTextField keySettingTextField;
    private JTextField simpleContentTextField;
    private JTextField behaviourNameTextField;
    private JTextField behaviourClassNameTextField;
    private JTextField actorNameTextField;
    private JTextField actionNameTextField;

    public RequesterGui() {
        prepareColors();
        setUp();
    }

    public static void main(String[] args) {
        RequesterGui requesterGui = new RequesterGui();
        requesterGui.showGui();
    }

    private void prepareColors() {
        colors = Arrays.asList(
                new Color(30, 90, 255),
                new Color(255, 10, 20),
                new Color(250, 10, 255),
                new Color(0, 200, 100),
                new Color(255, 110, 40),
                new Color(255, 107, 173),
                new Color(178, 91, 255),
                new Color(32, 195, 255),
                new Color(190, 106, 31)
        );
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
        createWestComponents();
        createCenterComponent();
    }

    private void createPrincipalPanels() {
        westPanel = new JPanel(new MigLayout("insets 10"));
        add(westPanel, BorderLayout.WEST);
        centerPanel = new JPanel(new MigLayout("insets 10 0 10 10"));
        add(centerPanel, BorderLayout.CENTER);
    }

    private void createWestComponents() {
        JLabel senderAgentTitleLabel = new JLabel("Sender agent:");
        westPanel.add(senderAgentTitleLabel, "w 110");

        senderAgentLabel = new JLabel();
        senderAgentLabel.setForeground(Color.BLUE);
        westPanel.add(senderAgentLabel, "w 150, wrap");

        JLabel receiverAgentTitleLabel = new JLabel("Receiver agent:");
        westPanel.add(receiverAgentTitleLabel, "grow");

        receiverAgentTextField = new JTextField();
        westPanel.add(receiverAgentTextField, "grow, wrap");

        JLabel actionsLabel = new JLabel("Action:");
        westPanel.add(actionsLabel, "grow");

        actionsComboBox = new JComboBox<>(RequesterGuiAction.values());
        actionsComboBox.setActionCommand(RequesterGuiEvent.CHANGE_ACTION.toString());
        actionsComboBox.addActionListener(new RequesterGuiComboBoxListener(this));
        westPanel.add(actionsComboBox, "grow, wrap");

        dynamicCanvasPanel = new JPanel(new MigLayout("insets 0"));
        westPanel.add(dynamicCanvasPanel, "span 2, grow, wrap 20");

        sendRequestButton = new JButton("Send request");
        sendRequestButton.setActionCommand(RequesterGuiEvent.SEND_MESSAGE.toString());
        westPanel.add(sendRequestButton, "span 2, h 30, grow");
    }

    private void createCenterComponent() {
        JLabel messageTextTitleLabel = new JLabel("Messages:");
        centerPanel.add(messageTextTitleLabel, "w 100%");

        saveMessagesLogButton = new JButton("Save");
        saveMessagesLogButton.setActionCommand(RequesterGuiEvent.SAVE_MESSAGE_LOGS.toString());
        centerPanel.add(saveMessagesLogButton);

        clearMessagesLogButton = new JButton("Clear");
        clearMessagesLogButton.setActionCommand(RequesterGuiEvent.CLEAR_MESSAGE_LOGS.toString());
        centerPanel.add(clearMessagesLogButton, "wrap");

        messageTextPane = new JTextPane();
        messageTextPane.setEditable(false);
        DefaultCaret caret = (DefaultCaret) messageTextPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JPanel messageTextWrapPanel = new JPanel(new BorderLayout());
        messageTextWrapPanel.add(messageTextPane);

        JScrollPane messageTextScrollPane = new JScrollPane(messageTextWrapPanel);
        centerPanel.add(messageTextScrollPane, "span 3, h 100%, w 100%");
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
        saveMessagesLogButton.addActionListener(actionListener);
        clearMessagesLogButton.addActionListener(actionListener);
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

    public void clearMessagesLog() {
        messageTextPane.setText("");
    }

    public String getMessagesLog() {
        return messageTextPane.getText();
    }

    public String getKeySetting() {
        return keySettingTextField.getText();
    }

    public String getSimpleContent() {
        return simpleContentTextField.getText();
    }

    public String getBehaviourName() {
        return behaviourNameTextField.getText();
    }

    public String getBehaviourClassName() {
        return behaviourClassNameTextField.getText();
    }

    public String getActorName() {
        return actorNameTextField.getText();
    }

    public String getActionName() {
        return actionNameTextField.getText();
    }

    public void setGetAllSettingsActionComponents() {
        refreshDynamicCanvas(() -> {
        });
    }

    public void setGetSettingActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel keySettingTitleLabel = new JLabel("Key:");
            dynamicCanvasPanel.add(keySettingTitleLabel, "w 110");
            keySettingTextField = new JTextField();
            dynamicCanvasPanel.add(keySettingTextField, "w 175, wrap");
        });
    }

    public void setGetEmotionalStateActionComponents() {
        refreshDynamicCanvas(() -> {
        });
    }

    public void setEvaluateStimulusActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel nameTitleLabel = new JLabel("Actor:");
            dynamicCanvasPanel.add(nameTitleLabel, "w 110");
            actorNameTextField = new JTextField();
            dynamicCanvasPanel.add(actorNameTextField, "w 175, wrap");

            JLabel classNameTitleLabel = new JLabel("Action name:");
            dynamicCanvasPanel.add(classNameTitleLabel, "w 110");
            actionNameTextField = new JTextField();
            dynamicCanvasPanel.add(actionNameTextField, "w 175, wrap");
        });
    }

    public void setSimpleContentActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel contentTitleLabel = new JLabel("Content:");
            dynamicCanvasPanel.add(contentTitleLabel, "w 110");
            simpleContentTextField = new JTextField();
            dynamicCanvasPanel.add(simpleContentTextField, "w 175, wrap");
        });
    }

    public void setRemoveBehaviourActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel nameTitleLabel = new JLabel("Name:");
            dynamicCanvasPanel.add(nameTitleLabel, "w 110");
            behaviourNameTextField = new JTextField();
            dynamicCanvasPanel.add(behaviourNameTextField, "w 175, wrap");
        });
    }

    public void setAddBehaviourActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel nameTitleLabel = new JLabel("Name:");
            dynamicCanvasPanel.add(nameTitleLabel, "w 110");
            behaviourNameTextField = new JTextField();
            dynamicCanvasPanel.add(behaviourNameTextField, "w 175, wrap");

            JLabel classNameTitleLabel = new JLabel("Class name:");
            dynamicCanvasPanel.add(classNameTitleLabel, "w 110");
            behaviourClassNameTextField = new JTextField();
            dynamicCanvasPanel.add(behaviourClassNameTextField, "w 175, wrap");
        });
    }

    public void setNotifyActionComponents() {
        setEvaluateStimulusActionComponents();
    }

    public void refreshDynamicCanvas(Runnable runnable) {
        dynamicCanvasPanel.removeAll();
        runnable.run();
        revalidate();
        repaint();
    }

}
