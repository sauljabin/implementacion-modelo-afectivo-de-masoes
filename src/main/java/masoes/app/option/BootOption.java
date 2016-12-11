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
import masoes.jade.setting.SettingsAgent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BootOption extends ApplicationOption {

    private EnvironmentFactory environmentFactory;
    private JadeBoot jadeBoot;

    public BootOption() {
        environmentFactory = new EnvironmentFactory();
        jadeBoot = new JadeBoot();
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
        List<EnvironmentAgentInfo> environmentAgentInfoList = environment.getEnvironmentAgentInfoList();
        validateEnvironmentAgentInfo(environmentAgentInfoList);
        jadeBoot.boot(toJadeAgentsOption(environmentAgentInfoList));
    }

    private void validateEnvironmentAgentInfo(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        if (!Optional.ofNullable(environmentAgentInfoList).isPresent() || environmentAgentInfoList.isEmpty())
            throw new InvalidEnvironmentException("No agents in environment");
    }

    private String toJadeAgentsOption(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        List<String> stringList = toStringList(environmentAgentInfoList);
        if (isNotPresentAgentSetting(environmentAgentInfoList))
            stringList.add(new EnvironmentAgentInfo("settings", SettingsAgent.class).toString());
        return String.join(";", stringList);
    }

    private List<String> toStringList(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        return environmentAgentInfoList.stream().map(EnvironmentAgentInfo::toString).collect(Collectors.toList());
    }

    private boolean isNotPresentAgentSetting(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        return !environmentAgentInfoList.stream().filter(agentInfo -> agentInfo.getAgentClass().equals(SettingsAgent.class)).findFirst().isPresent();
    }

}
