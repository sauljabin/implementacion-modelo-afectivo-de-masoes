/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.socialemotion;

import agent.AgentException;
import agent.AgentLogger;
import jade.JadeSettings;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class SocialEmotionGuiAgent extends GuiAgent {

    private SocialEmotionGui socialEmotionGui;
    private AgentLogger logger;
    private SocialEmotionGuiListener socialEmotionGuiListener;

    public SocialEmotionGuiAgent() {
        socialEmotionGui = new SocialEmotionGui();
        socialEmotionGuiListener = new SocialEmotionGuiListener(this, socialEmotionGui);
        logger = new AgentLogger(this);
    }

    @Override
    protected void setup() {
        if (!isGUIEnabled()) {
            throw new AgentException(getLocalName() + ": gui option is disabled");
        }

        addBehaviour(new SocialEmotionGuiAgentBehaviour(this));
        socialEmotionGui.showGui();
    }

    private boolean isGUIEnabled() {
        return Boolean.parseBoolean(JadeSettings.getInstance().get(JadeSettings.GUI));
    }

    public SocialEmotionGui getSocialEmotionGui() {
        return socialEmotionGui;
    }

    @Override
    protected void takeDown() {
        socialEmotionGui.closeGui();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (SocialEmotionGuiEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    doDelete();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
        }
    }

}
