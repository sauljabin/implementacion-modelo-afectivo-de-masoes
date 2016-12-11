/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.jade.JadeBoot;

public class AgentsOption extends ApplicationOption {

    private JadeBoot jadeBoot;

    public AgentsOption() {
        jadeBoot = new JadeBoot();
    }

    @Override
    public int getOrder() {
        return 40;
    }

    @Override
    public String getLongOpt() {
        return "agents";
    }

    @Override
    public String getOpt() {
        return "a";
    }

    @Override
    public String getDescription() {
        String description = "Starts JADE with agents, examples:\n" +
                "Adds agents: \n" +
                "-a <name>:<class>;<name>:<class>\n" +
                "Agent arguments: \n" +
                "-a <name>:<class>(arg1,arg2)";
        return description;
    }

    @Override
    public ArgumentType getArgType() {
        return ArgumentType.ONE_ARG;
    }

    @Override
    public void exec() {
        jadeBoot.boot(getValue());
    }

}
