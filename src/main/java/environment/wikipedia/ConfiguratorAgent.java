/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia;

import agent.AgentLogger;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class ConfiguratorAgent extends GuiAgent {

    private ConfiguratorAgentGui configuratorAgentGui;
    private ConfiguratorAgentListener configuratorAgentListener;
    private AgentLogger logger;

    public ConfiguratorAgent() {
        configuratorAgentGui = new ConfiguratorAgentGui();
        configuratorAgentListener = new ConfiguratorAgentListener(this, configuratorAgentGui);
        logger = new AgentLogger(this);
    }

    @Override
    protected void setup() {
        configuratorAgentGui.showGui();
    }

    @Override
    protected void takeDown() {
        configuratorAgentGui.closeGui();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (ConfiguratorAgentEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    doDelete();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
            configuratorAgentGui.showError(e.getMessage());
        }
    }

}
