/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import masoes.setting.Setting;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class HelpOption extends ApplicationOption {

    private final HelpFormatter formatter;
    private Options options;

    public HelpOption(Options options) {
        this(options, new HelpFormatter());
    }

    public HelpOption(Options options, HelpFormatter formatter) {
        super("h", "help", false, "Shows the options");
        this.options = options;
        this.formatter = formatter;
    }

    @Override
    public void exec() {
        try {
            formatter.printHelp(Setting.APP_NAME.getValue(), options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
