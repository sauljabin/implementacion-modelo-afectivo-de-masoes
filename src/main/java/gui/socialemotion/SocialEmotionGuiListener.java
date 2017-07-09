/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.socialemotion;

import jade.gui.GuiEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SocialEmotionGuiListener extends WindowAdapter {

    private SocialEmotionGui socialEmotionGui;
    private SocialEmotionGuiAgent socialEmotionGuiAgent;

    public SocialEmotionGuiListener(SocialEmotionGuiAgent socialEmotionGuiAgent, SocialEmotionGui socialEmotionGui) {
        this.socialEmotionGui = socialEmotionGui;
        this.socialEmotionGuiAgent = socialEmotionGuiAgent;
        setUpGui();
    }

    private void setUpGui() {
        socialEmotionGui.addWindowListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(socialEmotionGui, SocialEmotionGuiEvent.CLOSE_WINDOW.getInt());
        socialEmotionGuiAgent.postGuiEvent(guiEvent);
    }

}
