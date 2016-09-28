/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import jade.Boot;

import java.util.ArrayList;
import java.util.List;

public class JadeOption extends ApplicationOption {

    @Override
    public void exec(String optionValue) {
        Boot.main(findJadeArguments(optionValue));
    }

    @Override
    public int getOrder() {
        return 40;
    }

    @Override
    public String getOpt() {
        return "j";
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
                "  --jade=\"-agents <name>:<class>(arg1,arg2)\"";
        return description;
    }

    @Override
    public boolean hasArg() {
        return true;
    }

    private String[] findJadeArguments(String optionValue) {
        List<String> arguments = new ArrayList<>();

        String tempString = "";
        boolean ignoreWhiteSpace = false;

        for (int i = 0; i < optionValue.length(); i++) {
            char c = optionValue.charAt(i);

            if (c == '(') {
                ignoreWhiteSpace = true;
            } else if (c == ')') {
                ignoreWhiteSpace = false;
            }

            if (c == ' ' && !ignoreWhiteSpace) {
                arguments.add(tempString);
                tempString = "";
            } else {
                tempString += c;
            }
        }
        arguments.add(tempString);
        return arguments.toArray(new String[arguments.size()]);
    }
}
