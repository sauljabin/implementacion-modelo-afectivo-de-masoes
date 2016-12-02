/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.apache.commons.cli.Option;

import java.util.Comparator;
import java.util.Optional;

public class OptionWrapper extends Option implements Comparable<OptionWrapper> {

    private ApplicationOption applicationOption;

    public OptionWrapper(ApplicationOption applicationOption) {
        super(applicationOption.getOpt(), applicationOption.getLongOpt(), applicationOption.hasArg(), applicationOption.getDescription());
        this.applicationOption = applicationOption;
    }

    public static Comparator<Option> comparator() {
        return (optionA, optionB) -> {
            if (optionsAreWrapper(optionA, optionB)) {
                return castToOptionWrapper(optionA).compareTo(castToOptionWrapper(optionB));
            }
            return getOptionKey(optionA).compareToIgnoreCase((getOptionKey(optionB)));
        };
    }

    private static boolean optionsAreWrapper(Option optionA, Option optionB) {
        return optionA instanceof OptionWrapper && optionB instanceof OptionWrapper;
    }

    private static OptionWrapper castToOptionWrapper(Option option) {
        return (OptionWrapper) option;
    }

    private static String getOptionKey(Option option) {
        return Optional.ofNullable(option.getLongOpt()).orElse(option.getOpt());
    }

    @Override
    public int compareTo(OptionWrapper optionWrapper) {
        return Integer.compare(this.getOrder(), optionWrapper.getOrder());
    }

    public int getOrder() {
        return applicationOption.getOrder();
    }

}
