/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application;

import application.option.BootOption;
import application.option.EnvironmentOption;
import application.option.HelpOption;
import application.option.JadeOption;
import application.option.MasoesOption;
import application.option.SettingsOption;
import application.option.VersionOption;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OptionsContainer {

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
                        new BootOption(),
                        new MasoesOption());
        Collections.sort(applicationOptions);
        return applicationOptions;
    }

    public ApplicationOption getDefaultApplicationOption() {
        return new BootOption();
    }

}
