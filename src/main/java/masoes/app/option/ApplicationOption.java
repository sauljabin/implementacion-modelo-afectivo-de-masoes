/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.apache.commons.cli.Option;

import java.util.Optional;
import java.util.Properties;

public abstract class ApplicationOption implements Comparable<ApplicationOption> {

    private String value;
    private Properties properties;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Option toOption() {
        Option.Builder option = Option.builder(getOpt()).longOpt(getLongOpt()).desc(getDescription());
        switch (getArgType()) {
            case ONE_ARG:
                option.hasArg();
                break;
            case UNLIMITED_ARGS:
                option.hasArgs();
                break;
            case NO_ARGS:
            default:
                option.hasArg(false);
        }
        return option.build();
    }

    public String getKeyOpt() {
        return Optional.ofNullable(getLongOpt()).orElse(getOpt());
    }

    @Override
    public String toString() {
        if (Optional.ofNullable(getLongOpt()).isPresent()) {
            return String.format("{option=[-%s,--%s], order=%d}", getOpt(), getLongOpt(), getOrder());
        } else {
            return String.format("{option=[-%s], order=%d}", getOpt(), getOrder());
        }
    }

    @Override
    public int compareTo(ApplicationOption applicationOption) {
        return Integer.compare(this.getOrder(), applicationOption.getOrder());
    }

    public abstract int getOrder();

    public abstract String getLongOpt();

    public abstract String getOpt();

    public abstract String getDescription();

    public abstract ArgumentType getArgType();

    public abstract void exec();

    public abstract boolean isFinalOption();

}
