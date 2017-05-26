/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import jade.lang.acl.ACLMessage;
import logger.LogWriter;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import translate.Translation;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
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

    private static final int CONVERSATIONS_CACHE = 50;
    private static final String FIELD_W = "w 190, wrap";
    private static final String LABEL_W = "w 90";
    private JLabel senderAgentLabel;
    private JComboBox<RequesterGuiAction> actionsComboBox;
    private JTextPane messageTextPane;
    private JTextArea propertiesTextArea;
    private JPanel westPanel;
    private JPanel centerPanel;
    private JPanel dynamicCanvasPanel;
    private JButton sendRequestButton;
    private JButton saveMessagesLogButton;
    private JButton clearMessagesLogButton;
    private JTextField receiverAgentTextField;
    private JTextField keySettingTextField;
    private JTextField simpleContentTextField;
    private JTextField behaviourNameTextField;
    private JTextField behaviourClassNameTextField;
    private JTextField actorNameTextField;
    private JTextField actionNameTextField;
    private JTextField agentNameTextField;
    private JTextField serviceNameTextField;
    private JTextField creatorNameTextField;
    private JTextField objectNameTextField;
    private JTextField affectedNameTextField;
    private JTextField eventNameTextField;

    private List<Color> colors;
    private Iterator<Color> colorIterator;
    private LinkedHashMap<String, Color> conversations;
    private Translation translation;

    private Logger logger = LoggerFactory.getLogger(RequesterGui.class);
    private LogWriter logWriter = new LogWriter();

    public RequesterGui() {
        translation = Translation.getInstance();
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
        setTitle(translation.get("gui.send_message_interface"));
        setSize(900, 600);
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
        JLabel senderAgentTitleLabel = new JLabel(translation.get("gui.sender"));
        westPanel.add(senderAgentTitleLabel, LABEL_W);

        senderAgentLabel = new JLabel();
        senderAgentLabel.setForeground(Color.BLUE);
        westPanel.add(senderAgentLabel, FIELD_W);

        JLabel receiverAgentTitleLabel = new JLabel(translation.get("gui.receiver"));
        westPanel.add(receiverAgentTitleLabel, LABEL_W);

        receiverAgentTextField = new JTextField();
        westPanel.add(receiverAgentTextField, FIELD_W);

        JLabel actionsLabel = new JLabel(translation.get("gui.action"));
        westPanel.add(actionsLabel, LABEL_W);

        Font font = new Font("Arial", Font.BOLD, 10);
        actionsComboBox = new JComboBox<>(RequesterGuiAction.values());
        actionsComboBox.setFont(font);
        actionsComboBox.setActionCommand(RequesterGuiEvent.CHANGE_ACTION.toString());
        actionsComboBox.addActionListener(new RequesterGuiComboBoxListener(this));
        westPanel.add(actionsComboBox, FIELD_W);

        dynamicCanvasPanel = new JPanel(new MigLayout("insets 0"));
        westPanel.add(dynamicCanvasPanel, "span 2, grow, wrap 20");

        sendRequestButton = new JButton(translation.get("gui.send_message"));
        sendRequestButton.setActionCommand(RequesterGuiEvent.SEND_MESSAGE.toString());
        westPanel.add(sendRequestButton, "span 2, h 30, grow");
    }

    private void createCenterComponent() {
        JLabel messageTextTitleLabel = new JLabel(translation.get("gui.messages"));
        centerPanel.add(messageTextTitleLabel, "w 100%");

        saveMessagesLogButton = new JButton(translation.get("gui.save"));
        saveMessagesLogButton.setActionCommand(RequesterGuiEvent.SAVE_MESSAGE_LOGS.toString());
        centerPanel.add(saveMessagesLogButton);

        clearMessagesLogButton = new JButton(translation.get("gui.clean"));
        clearMessagesLogButton.setActionCommand(RequesterGuiEvent.CLEAR_MESSAGE_LOGS.toString());
        centerPanel.add(clearMessagesLogButton, "wrap");

        messageTextPane = new JTextPane();
        messageTextPane.setEditable(false);

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

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(() -> {
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

            Document blank = new DefaultStyledDocument();
            messageTextPane.setDocument(blank);

            try {
                SimpleAttributeSet attributeSet = new SimpleAttributeSet();

                attributeSet.addAttribute(StyleConstants.Bold, Boolean.TRUE);
                attributeSet.addAttribute(StyleConstants.Foreground, conversations.get(message.getConversationId()));
                document.insertString(document.getLength(), String.format("%s %s\n", translation.get("gui.conversation"), message.getConversationId()), attributeSet);

                attributeSet.addAttribute(StyleConstants.Bold, Boolean.FALSE);
                attributeSet.addAttribute(StyleConstants.Foreground, Color.BLACK);
                document.insertString(document.getLength(), message.toString() + "\n\n", attributeSet);

                messageTextPane.setDocument(document);
                messageTextPane.setCaretPosition(document.getLength());
            } catch (Exception e) {
                logWriter.exception(e).error(logger);
            }
        });
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

    public String getAffectedName() {
        return affectedNameTextField.getText();
    }

    public String getCreatorName() {
        return creatorNameTextField.getText();
    }

    public String getServiceName() {
        return serviceNameTextField.getText();
    }

    public String getActionName() {
        return actionNameTextField.getText();
    }

    public String getEventName() {
        return eventNameTextField.getText();
    }

    public String getObjectName() {
        return objectNameTextField.getText();
    }

    public String getObjectProperties() {
        return propertiesTextArea.getText();
    }

    public void setGetAllSettingsActionComponents() {
        refreshDynamicCanvas(() -> {
        });
    }

    public void setGetSettingActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel keySettingTitleLabel = new JLabel(translation.get("gui.name"));
            dynamicCanvasPanel.add(keySettingTitleLabel, LABEL_W);
            keySettingTextField = new JTextField();
            dynamicCanvasPanel.add(keySettingTextField, FIELD_W);
        });
    }

    public void setGetEmotionalStateActionComponents() {
        refreshDynamicCanvas(() -> {
        });
    }

    public void setEvaluateActionStimulusComponents() {
        refreshDynamicCanvas(() -> {
            JLabel actorTitleLabel = new JLabel(translation.get("gui.actor"));
            dynamicCanvasPanel.add(actorTitleLabel, LABEL_W);
            actorNameTextField = new JTextField();
            dynamicCanvasPanel.add(actorNameTextField, FIELD_W);

            JLabel actionNameTitleLabel = new JLabel(translation.get("gui.action"));
            dynamicCanvasPanel.add(actionNameTitleLabel, LABEL_W);
            actionNameTextField = new JTextField();
            dynamicCanvasPanel.add(actionNameTextField, FIELD_W);
        });
    }

    public void setEvaluateObjectStimulusComponents() {
        refreshDynamicCanvas(() -> {
            JLabel creatorTitleLabel = new JLabel(translation.get("gui.creator"));
            dynamicCanvasPanel.add(creatorTitleLabel, LABEL_W);
            creatorNameTextField = new JTextField();
            dynamicCanvasPanel.add(creatorNameTextField, FIELD_W);

            JLabel objectNameTitleLabel = new JLabel(translation.get("gui.object"));
            dynamicCanvasPanel.add(objectNameTitleLabel, LABEL_W);
            objectNameTextField = new JTextField();
            dynamicCanvasPanel.add(objectNameTextField, FIELD_W);
        });
    }

    public void setEvaluateEventStimulusComponents() {
        refreshDynamicCanvas(() -> {
            JLabel actorTitleLabel = new JLabel(translation.get("gui.affected"));
            dynamicCanvasPanel.add(actorTitleLabel, LABEL_W);
            affectedNameTextField = new JTextField();
            dynamicCanvasPanel.add(affectedNameTextField, FIELD_W);

            JLabel actionNameTitleLabel = new JLabel(translation.get("gui.event"));
            dynamicCanvasPanel.add(actionNameTitleLabel, LABEL_W);
            eventNameTextField = new JTextField();
            dynamicCanvasPanel.add(eventNameTextField, FIELD_W);
        });
    }

    public void setSimpleContentActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel contentTitleLabel = new JLabel(translation.get("gui.content"));
            dynamicCanvasPanel.add(contentTitleLabel, LABEL_W);
            simpleContentTextField = new JTextField();
            dynamicCanvasPanel.add(simpleContentTextField, FIELD_W);
        });
    }

    public void setRemoveBehaviourActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel nameTitleLabel = new JLabel(translation.get("gui.name"));
            dynamicCanvasPanel.add(nameTitleLabel, LABEL_W);
            behaviourNameTextField = new JTextField();
            dynamicCanvasPanel.add(behaviourNameTextField, FIELD_W);
        });
    }

    public void setAddBehaviourActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel nameTitleLabel = new JLabel(translation.get("gui.name"));
            dynamicCanvasPanel.add(nameTitleLabel, LABEL_W);
            behaviourNameTextField = new JTextField();
            dynamicCanvasPanel.add(behaviourNameTextField, FIELD_W);

            JLabel classNameTitleLabel = new JLabel(translation.get("gui.class"));
            dynamicCanvasPanel.add(classNameTitleLabel, LABEL_W);
            behaviourClassNameTextField = new JTextField();
            dynamicCanvasPanel.add(behaviourClassNameTextField, FIELD_W);
        });
    }

    public void setNotifyActionComponents() {
        setEvaluateActionStimulusComponents();
    }

    public void setNotifyObjectComponents() {
        setEvaluateObjectStimulusComponents();
    }

    public void setNotifyEventComponents() {
        setEvaluateEventStimulusComponents();
    }

    public void setGetServicesActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel nameTitleLabel = new JLabel(translation.get("gui.agent"));
            dynamicCanvasPanel.add(nameTitleLabel, LABEL_W);
            agentNameTextField = new JTextField();
            dynamicCanvasPanel.add(agentNameTextField, FIELD_W);
        });
    }

    public void setGetAgentsActionComponents() {
        refreshDynamicCanvas(() -> {
        });
    }

    public void setSearchAgentActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel nameTitleLabel = new JLabel(translation.get("gui.service"));
            dynamicCanvasPanel.add(nameTitleLabel, LABEL_W);
            serviceNameTextField = new JTextField();
            dynamicCanvasPanel.add(serviceNameTextField, FIELD_W);
        });
    }

    public void setDeregisterAgentActionComponents() {
        setGetServicesActionComponents();
    }

    public void setRegisterAgentActionComponents() {
        refreshDynamicCanvas(() -> {
            JLabel agentNameTitle = new JLabel(translation.get("gui.agent"));
            dynamicCanvasPanel.add(agentNameTitle, LABEL_W);
            agentNameTextField = new JTextField();
            dynamicCanvasPanel.add(agentNameTextField, FIELD_W);

            JLabel serviceNameTitle = new JLabel(translation.get("gui.service"));
            dynamicCanvasPanel.add(serviceNameTitle, LABEL_W);
            serviceNameTextField = new JTextField();
            dynamicCanvasPanel.add(serviceNameTextField, FIELD_W);
        });
    }

    public void setCreateObjectComponents() {
        refreshDynamicCanvas(() -> {
            JLabel creatorTitleLabel = new JLabel(translation.get("gui.creator"));
            dynamicCanvasPanel.add(creatorTitleLabel, LABEL_W);
            creatorNameTextField = new JTextField();
            dynamicCanvasPanel.add(creatorNameTextField, FIELD_W);

            JLabel objectNameTitleLabel = new JLabel(translation.get("gui.object"));
            dynamicCanvasPanel.add(objectNameTitleLabel, LABEL_W);
            objectNameTextField = new JTextField();
            dynamicCanvasPanel.add(objectNameTextField, FIELD_W);

            JLabel propertiesTitleLabel = new JLabel(translation.get("gui.properties"));
            dynamicCanvasPanel.add(propertiesTitleLabel, LABEL_W);
            propertiesTextArea = new JTextArea();

            DefaultCaret caret = (DefaultCaret) propertiesTextArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

            JScrollPane propertiesTextScrollPane = new JScrollPane(propertiesTextArea);

            dynamicCanvasPanel.add(propertiesTextScrollPane, "h 80, " + FIELD_W);
        });
    }

    public void setUpdateObjectComponents() {
        setCreateObjectComponents();
    }

    public void setDeleteObjectComponents() {
        setEvaluateObjectStimulusComponents();
    }

    public void setGetObjectComponents() {
        setDeleteObjectComponents();
    }

    public void refreshDynamicCanvas(Runnable runnable) {
        dynamicCanvasPanel.removeAll();
        runnable.run();
        revalidate();
        repaint();
    }

    public String getAgentName() {
        return agentNameTextField.getText();
    }

    public void setGetSocialEmotionActionComponents() {
        refreshDynamicCanvas(() -> {
        });
    }

}
