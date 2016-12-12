/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.settings.ApplicationSettings;
import masoes.env.Environment;
import masoes.env.EnvironmentAgentInfo;
import masoes.env.EnvironmentFactory;
import masoes.jade.agent.SettingsAgent;
import masoes.jade.settings.JadeSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnvironmentOption extends ApplicationOption {

    private final JadeSettings jadeSettings;
    private final EnvironmentFactory environmentFactory;
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
        List<EnvironmentAgentInfo> environmentAgentInfoList = getEnvironmentAgentInfoList(environmentFactory.createEnvironment());
        List<String> stringEnvironmentAgentInfoList = toStringList(environmentAgentInfoList);
        return String.join(";", stringEnvironmentAgentInfoList);
    }

    private List<EnvironmentAgentInfo> getEnvironmentAgentInfoList(Environment environment) {
        List<EnvironmentAgentInfo> environmentAgentInfoList = new ArrayList<>();
        environmentAgentInfoList.addAll(Optional.ofNullable(environment.getEnvironmentAgentInfoList()).orElse(new ArrayList<>()));
        if (isNotPresentAgentSetting(environmentAgentInfoList)) {
            environmentAgentInfoList.add(new EnvironmentAgentInfo("settings", SettingsAgent.class));
        }
        return environmentAgentInfoList;
    }

    private List<String> toStringList(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        return environmentAgentInfoList.stream().map(EnvironmentAgentInfo::toString).collect(Collectors.toList());
    }

    private boolean isNotPresentAgentSetting(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        return !environmentAgentInfoList.stream().filter(agentInfo -> agentInfo.getAgentClass().equals(SettingsAgent.class)).findFirst().isPresent();
    }

}
