/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import jade.Boot;

public class JadeOption extends ApplicationOption {

    @Override
    public void exec(String optionValue) {
        Boot.main(optionValue.split(" "));
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getOpt() {
        return null;
    }

    @Override
    public String getLongOpt() {
        return "jade";
    }

    @Override
    public String getDescription() {
        String description = "Starts JADE framework with arguments, examples:\n" +
                "Starts with gui: \n" +
                "  --jade=-gui\n" +
                "Adds agents: \n" +
                "  --jade=\"-agents <name>:<class>;<name>:<class>\"\n" +
                "Agent arguments: \n" +
                "  --jade=\"-agents <name>:<class>(argument,'argument 2')\"";
        return description;
    }

    @Override
    public boolean hasArg() {
        return true;
    }
}
