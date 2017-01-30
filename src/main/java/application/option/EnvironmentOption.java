/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationOption;
import application.ArgumentType;
import environment.AgentCommand;
import environment.Environment;
import environment.EnvironmentFactory;
import gui.RequesterGuiAgent;
import settings.agent.SettingsAgent;
import settings.loader.ApplicationSettings;
import settings.loader.JadeSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnvironmentOption extends ApplicationOption {

    private static final String AGENT_DELIMITER = ";";
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
        jadeSettings.set(JadeSettings.AGENTS, getAgents());
    }

    private String getAgents() {
        return String.join(AGENT_DELIMITER, toStringList(getEnvironmentAgentInfoList(environmentFactory.createEnvironment())));
    }

    private List<AgentCommand> getEnvironmentAgentInfoList(Environment environment) {
        List<AgentCommand> agentCommands = new ArrayList<>();

        agentCommands.addAll(Optional.ofNullable(environment.getAgentCommands()).orElse(new ArrayList<>()));

        if (!isPresentSettingAgent(agentCommands)) {
            agentCommands.add(new AgentCommand("settings", SettingsAgent.class));
        }

        if (!isPresentRequesterAgent(agentCommands) && isJadeGui()) {
            agentCommands.add(new AgentCommand("requester", RequesterGuiAgent.class));
        }

        if (!isJadeGui()) {
            agentCommands = agentCommands.stream().filter(
                    agentCommandFormatter -> !agentCommandFormatter.getAgentClass().equals(RequesterGuiAgent.class)
            ).collect(Collectors.toList());
        }

        return agentCommands;
    }

    private boolean isJadeGui() {
        return Boolean.valueOf(jadeSettings.get(JadeSettings.GUI));
    }

    private List<String> toStringList(List<AgentCommand> agentCommandList) {
        return agentCommandList.stream().map(AgentCommand::format).collect(Collectors.toList());
    }

    private boolean isPresentSettingAgent(List<AgentCommand> agentCommandList) {
        return agentCommandList.stream().filter(
                agentInfo -> agentInfo.getAgentClass().equals(SettingsAgent.class)
        ).findFirst().isPresent();
    }

    private boolean isPresentRequesterAgent(List<AgentCommand> agentCommandList) {
        return agentCommandList.stream().filter(
                agentInfo -> agentInfo.getAgentClass().equals(RequesterGuiAgent.class)
        ).findFirst().isPresent();
    }

}
