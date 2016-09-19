/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import jade.Boot;

public class JadeOption extends ApplicationOption {

    private static final String STR_NL = "\n";
    private static final String STR_QUOT = "\"";

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
        return "j";
    }

    @Override
    public String getLongOpt() {
        return "jade";
    }

    @Override
    public String getDescription() {
        String description = "Starts Jade framework with arguments, examples:" + STR_NL +
                "Starts with gui: -gui" +
                STR_NL +
                "Adds agents (separate with ';'):" +
                STR_NL +
                "-agents <name>:<class>;<name>:<class>" +
                STR_NL +
                "Adds agents with arguments (separate with ','):" +
                STR_NL +
                "-agents <name>:<class>(<" + STR_QUOT + "argument 1" + STR_QUOT + ">,<" + STR_QUOT + "argument 2" + STR_QUOT + ">)" +
                STR_NL;
        return description;
    }

    @Override
    public boolean hasArg() {
        return true;
    }
}
