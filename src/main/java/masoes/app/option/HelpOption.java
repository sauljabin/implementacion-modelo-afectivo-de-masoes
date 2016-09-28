/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.setting.Setting;
import org.apache.commons.cli.HelpFormatter;

public class HelpOption extends ApplicationOption {

    private final HelpFormatter formatter;

    public HelpOption() {
        this(new HelpFormatter());
    }

    public HelpOption(HelpFormatter formatter) {
        this.formatter = formatter;
        formatter.setSyntaxPrefix("Usage: ");
        formatter.setLongOptSeparator("=");
        formatter.setOptionComparator(OptionWrapper.comparator());
    }

    @Override
    public void exec(String optionValue) {
        try {
            formatter.printHelp(Setting.APP_NAME.getValue(), ApplicationOptionManager.getInstance().toOptions());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrder() {
        return 20;
    }

    @Override
    public String getOpt() {
        return "h";
    }

    @Override
    public String getLongOpt() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows the options";
    }

    @Override
    public boolean hasArg() {
        return false;
    }
}
