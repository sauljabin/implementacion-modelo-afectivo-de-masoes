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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BootOption extends ApplicationOption {

    private EnvironmentFactory environmentFactory;
    private JadeBoot jadeBoot;

    public BootOption(EnvironmentFactory environmentFactory, JadeBoot jadeBoot) {
        this.environmentFactory = environmentFactory;
        this.jadeBoot = jadeBoot;
    }

    public BootOption() {
        this(new EnvironmentFactory(), new JadeBoot());
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
    public boolean hasArg() {
        return false;
    }

    @Override
    public void exec(String optionValue) {
        Environment environment = environmentFactory.createEnvironment();
        environment.setup();

        List<EnvironmentAgentInfo> environmentAgentInfoList = environment.getEnvironmentAgentInfoList();
        validateEnvironmentAgentInfo(environmentAgentInfoList);

        jadeBoot.boot(toJadeAgentsOption(environmentAgentInfoList));
    }

    private void validateEnvironmentAgentInfo(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        if (!Optional.ofNullable(environmentAgentInfoList).isPresent() || environmentAgentInfoList.isEmpty())
            throw new InvalidEnvironmentException("No agents in environment");
    }

    private String toJadeAgentsOption(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        List<String> collect = environmentAgentInfoList.stream().map(EnvironmentAgentInfo::toString).collect(Collectors.toList());
        collect.add(new EnvironmentAgentInfo("settings", SettingsAgent.class, null).toString());
        return String.join(";", collect);
    }

}
