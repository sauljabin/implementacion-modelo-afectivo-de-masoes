/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.apache.commons.cli.Option;

import java.util.Optional;

public abstract class ApplicationOption implements Comparable<ApplicationOption> {

    public Option toOption() {
        return new OptionWrapper(this);
    }

    public String getKeyOpt() {
        return Optional.ofNullable(getLongOpt()).orElse(getOpt());
    }

    @Override
    public String toString() {
        return String.format("{option=[-%s,--%s], order=%d}", getOpt(), getLongOpt(), getOrder());
    }

    @Override
    public int compareTo(ApplicationOption applicationOption) {
        return Integer.compare(this.getOrder(), applicationOption.getOrder());
    }

    public abstract int getOrder();

    public abstract String getLongOpt();

    public abstract String getOpt();

    public abstract String getDescription();

    public abstract boolean hasArg();

    public abstract void exec(String optionValue);

}