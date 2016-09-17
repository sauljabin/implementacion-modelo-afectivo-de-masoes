/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.Options;

public class ApplicationOptions extends Options {

    public ApplicationOptions() {
        super();
        this.addOption(new HelpOption(this));
        this.addOption(new VersionOption());
        this.addOption(new JadeOption());
    }
}
