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
        this(new JadeBoot());
    }

    public AgentsOption(JadeBoot jadeBoot) {
        this.jadeBoot = jadeBoot;
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
                "  --agents=\"<name>:<class>;<name>:<class>\"\n" +
                "Agent arguments: \n" +
                "  --agents=\"<name>:<class>(arg1,arg2)\"";
        return description;
    }

    @Override
    public boolean hasArg() {
        return true;
    }

    @Override
    public void exec(String optionValue) {
        jadeBoot.boot(optionValue);
    }

}
