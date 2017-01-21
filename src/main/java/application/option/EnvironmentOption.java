/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import jade.command.AgentCommandFormatter;
import masoes.environment.Environment;
import masoes.environment.EnvironmentFactory;
import settings.agent.SettingsAgent;
import settings.application.ApplicationSettings;
import settings.jade.JadeSettings;

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

    private List<AgentCommandFormatter> getEnvironmentAgentInfoList(Environment environment) {
        List<AgentCommandFormatter> agentCommands = new ArrayList<>();

        agentCommands.addAll(Optional.ofNullable(environment.getAgentCommands()).orElse(new ArrayList<>()));

        if (isNotPresentAgentSetting(agentCommands)) {
            agentCommands.add(new AgentCommandFormatter("settings", SettingsAgent.class));
        }

        return agentCommands;
    }

    private List<String> toStringList(List<AgentCommandFormatter> agentCommandList) {
        return agentCommandList.stream().map(AgentCommandFormatter::format).collect(Collectors.toList());
    }

    private boolean isNotPresentAgentSetting(List<AgentCommandFormatter> agentCommandList) {
        return !agentCommandList.stream().filter(agentInfo -> agentInfo.getAgentClass().equals(SettingsAgent.class)).findFirst().isPresent();
    }

}
