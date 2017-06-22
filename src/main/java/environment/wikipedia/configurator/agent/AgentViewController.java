/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator.agent;

import gui.WindowsEventsAdapter;

public class AgentViewController extends WindowsEventsAdapter {

    private final AgentView agentView;

    public AgentViewController() {
        this.agentView = new AgentView();
        this.agentView.setVisible(true);
    }

    public static void main(String[] args) {
        new AgentViewController();
    }

}
