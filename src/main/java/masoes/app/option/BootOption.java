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
        List<EnvironmentAgentInfo> environmentAgentInfoList = environment.getEnvironmentAgentInfoList();
        validateEnvironment(environmentAgentInfoList);
        jadeBoot.boot(toJadeAgentsOption(environmentAgentInfoList));
    }

    private void validateEnvironment(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        if (!Optional.ofNullable(environmentAgentInfoList).isPresent() || environmentAgentInfoList.isEmpty())
            throw new InvalidEnvironmentException("No agents in environment");
    }

    private String toJadeAgentsOption(List<EnvironmentAgentInfo> environmentAgentInfoList) {
        return String.join(";", environmentAgentInfoList.stream().map(EnvironmentAgentInfo::toString).collect(Collectors.toList()));
    }

}
