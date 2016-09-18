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
    private ApplicationOptions options;

    public HelpOption(ApplicationOptions options) {
        this(options, new HelpFormatter());
    }

    public HelpOption(ApplicationOptions options, HelpFormatter formatter) {
        super("h", "help", false, "Shows the options");
        this.options = options;
        this.formatter = formatter;
    }

    @Override
    public void exec(String optionValue) {
        try {
            formatter.printHelp(Setting.APP_NAME.getValue(), options.toOptions());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
