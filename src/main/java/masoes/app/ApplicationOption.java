/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.Option;

public abstract class ApplicationOption extends Option implements Comparable<ApplicationOption> {

    public ApplicationOption(String opt, String longOpt, boolean hasArg, String description) {
        super(opt, longOpt, hasArg, description);
    }

    public abstract void exec(String optionValue);

    public abstract int getOrder();

    @Override
    public String toString() {
        return String.format("{option=[-%s,--%s], description=%s}", getOpt(), getLongOpt(), getDescription());
    }

    @Override
    public int compareTo(ApplicationOption applicationOption) {
        return Integer.compare(this.getOrder(), applicationOption.getOrder());
    }
}
