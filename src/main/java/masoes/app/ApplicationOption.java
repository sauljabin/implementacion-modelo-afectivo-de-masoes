/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.Option;

public abstract class ApplicationOption extends Option {

    public ApplicationOption(String opt, String longOpt, boolean hasArg, String description) {
        super(opt, longOpt, hasArg, description);
    }

    public abstract void exec(String optionValue);

    @Override
    public String toString() {
        return String.format("{option=[-%s,--%s], description=%s}", getOpt(), getLongOpt(), getDescription());
    }
}
