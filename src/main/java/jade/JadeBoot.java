/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class JadeBoot {

    private JadeSettings jadeSettings;
    private ProfileImpl jadeProfile;
    private Runtime jadeRuntime;
    private AgentContainer mainContainer;

    public JadeBoot() {
        jadeProfile = new ProfileImpl();
        jadeRuntime = Runtime.instance();
        jadeSettings = JadeSettings.getInstance();
    }

    public void boot() {
        jadeSettings.toMap().forEach((key, value) -> jadeProfile.setParameter(key, value));
        try {
            mainContainer = jadeRuntime.createMainContainer(jadeProfile);
            mainContainer.addPlatformListener(new JadePlatformListener());
        } catch (Exception e) {
            throw new JadeBootException(e);
        }
    }

    public void addAgent(String agentName, Agent agent) {
        try {
            mainContainer.acceptNewAgent(agentName, agent);
        } catch (Exception e) {
            throw new AgentException(e);
        }
    }

    public AgentController getAgent(String name) {
        try {
            return mainContainer.getAgent(name);
        } catch (Exception e) {
            throw new AgentException(e);
        }
    }

    public void kill() {
        try {
            mainContainer.getPlatformController().kill();
        } catch (Exception e) {
            throw new ContainerException(e);
        }
    }

}
