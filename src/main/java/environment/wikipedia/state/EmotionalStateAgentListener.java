/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.state;

import jade.gui.GuiEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EmotionalStateAgentListener extends WindowAdapter {

    private EmotionalStateAgentGui emotionalStateAgentGui;
    private EmotionalStateAgent emotionalStateAgent;

    public EmotionalStateAgentListener(EmotionalStateAgent emotionalStateAgent, EmotionalStateAgentGui emotionalStateAgentGui) {
        this.emotionalStateAgentGui = emotionalStateAgentGui;
        this.emotionalStateAgent = emotionalStateAgent;
        setUpGui();
    }

    private void setUpGui() {
        emotionalStateAgentGui.addWindowListener(this);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GuiEvent guiEvent = new GuiEvent(emotionalStateAgentGui, EmotionalStateAgentEvent.CLOSE_WINDOW.getInt());
        emotionalStateAgent.postGuiEvent(guiEvent);
    }

}
