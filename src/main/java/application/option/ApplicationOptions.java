/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApplicationOptions {

    public Options toOptions() {
        Options options = new Options();
        getApplicationOptionList().forEach(option -> options.addOption(option.toOption()));
        return options;
    }

    public List<ApplicationOption> getApplicationOptionList() {
        List<ApplicationOption> applicationOptions = Arrays
                .asList(new HelpOption(),
                        new VersionOption(),
                        new JadeOption(),
                        new SettingsOption(),
                        new EnvironmentOption(),
                        new BootOption());
        Collections.sort(applicationOptions);
        return applicationOptions;
    }

    public ApplicationOption getDefaultApplicationOption() {
        return new BootOption();
    }

}
