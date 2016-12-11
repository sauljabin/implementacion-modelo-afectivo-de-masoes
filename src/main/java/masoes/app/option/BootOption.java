/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.env.Environment;
import masoes.env.EnvironmentAgentInfo;
import masoes.env.EnvironmentFactory;
import masoes.env.InvalidEnvironmentException;
import masoes.jade.JadeBoot;
import masoes.jade.agent.SettingsAgent;
import masoes.jade.settings.JadeSettings;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BootOption extends ApplicationOption {

    private JadeSettings jadeSettings;
    private EnvironmentFactory environmentFactory;
    private JadeBoot jadeBoot;

    public BootOption() {
        environmentFactory = new EnvironmentFactory();
        jadeBoot = new JadeBoot();
        jadeSettings = JadeSettings.getInstance();
    }

    @Override
    public int getOrder() {
        return 60;
    }

    @Override
    public String getLongOpt() {
        return "boot";
    }

    @Override
    public String getOpt() {
        return "b";
    }

    @Override
    public String getDescription() {
        return "Starts the application";
    }

    @Override
    public ArgumentType getArgType() {
        return ArgumentType.NO_ARGS;
    }

    @Override
    public void exec() {
        Environment environment = environmentFactory.createEnvironment();
        validateEnvironmentAgentInfo(environment.getEnvironmentAgentInfoList());
        toJadeAgentsOption(environment.getEnvironmentAgentInfoList());
        jadeBoot.boot();
    }

    private void validateEnvironmentAgentInfo(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        if (!Optional.ofNullable(environmentAgentInfoList).isPresent() || environmentAgentInfoList.isEmpty()) {
            throw new InvalidEnvironmentException("No agents in environment");
        }
    }

    private void toJadeAgentsOption(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        List<String> stringList = toStringList(environmentAgentInfoList);
        if (isNotPresentAgentSetting(environmentAgentInfoList)) {
            stringList.add(new EnvironmentAgentInfo("settings", SettingsAgent.class).toString());
        }
        jadeSettings.set(JadeSettings.AGENTS, String.join(";", stringList));
    }

    private List<String> toStringList(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        return environmentAgentInfoList.stream().map(EnvironmentAgentInfo::toString).collect(Collectors.toList());
    }

    private boolean isNotPresentAgentSetting(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        return !environmentAgentInfoList.stream().filter(agentInfo -> agentInfo.getAgentClass().equals(SettingsAgent.class)).findFirst().isPresent();
    }

}
