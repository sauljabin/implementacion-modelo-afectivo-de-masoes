/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationOption;
import application.ApplicationSettings;
import application.ArgumentType;
import environment.AgentParameter;
import environment.Environment;
import environment.EnvironmentFactory;
import gui.RequesterGuiAgent;
import jade.JadeSettings;
import settings.SettingsAgent;

import java.util.List;
import java.util.Optional;

public class EnvironmentOption extends ApplicationOption {

    private JadeSettings jadeSettings;
    private EnvironmentFactory environmentFactory;
    private ApplicationSettings applicationSettings;

    public EnvironmentOption() {
        applicationSettings = ApplicationSettings.getInstance();
        jadeSettings = JadeSettings.getInstance();
        environmentFactory = new EnvironmentFactory();
    }

    @Override
    public int getOrder() {
        return 50;
    }

    @Override
    public String getLongOpt() {
        return null;
    }

    @Override
    public String getOpt() {
        return "E";
    }

    @Override
    public String getDescription() {
        return "Sets the environment (dummy, wikipedia), example:\n" +
                "-Edummy";
    }

    @Override
    public ArgumentType getArgType() {
        return ArgumentType.ONE_ARG;
    }

    @Override
    public boolean isFinalOption() {
        return false;
    }

    @Override
    public void exec() {
        applicationSettings.set(ApplicationSettings.MASOES_ENV, getValue());

        Environment environment = environmentFactory.createEnvironment(getValue());
        processEnvironment(environment);

        jadeSettings.set(JadeSettings.AGENTS, environment.toJadeParameter());
    }

    private void processEnvironment(Environment environment) {
        Optional<AgentParameter> settingAgent = findSettingAgent(environment.getAgentParameters());
        if (!settingAgent.isPresent()) {
            environment.add(new AgentParameter("settings", SettingsAgent.class));
        }

        Optional<AgentParameter> requesterAgent = findRequesterAgent(environment.getAgentParameters());
        if (!requesterAgent.isPresent() && isJadeGui()) {
            environment.add(new AgentParameter("requester", RequesterGuiAgent.class));
        }

        if (requesterAgent.isPresent() && !isJadeGui()) {
            environment.remove(requesterAgent.get());
        }
    }

    private Optional<AgentParameter> findSettingAgent(List<AgentParameter> agentParameterList) {
        return agentParameterList.stream().filter(
                agentParameter -> agentParameter.getAgentClass().equals(SettingsAgent.class)
        ).findFirst();
    }

    private Optional<AgentParameter> findRequesterAgent(List<AgentParameter> agentParameterList) {
        return agentParameterList.stream().filter(
                agentInfo -> agentInfo.getAgentClass().equals(RequesterGuiAgent.class)
        ).findFirst();
    }

    private boolean isJadeGui() {
        return Boolean.valueOf(jadeSettings.get(JadeSettings.GUI));
    }

}
