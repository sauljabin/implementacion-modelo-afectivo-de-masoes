/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationOptionManager {

    private static ApplicationOptionManager INSTANCE;
    private List<ApplicationOption> applicationOptions;

    private ApplicationOptionManager() {
        addOptions();
    }

    public synchronized static ApplicationOptionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationOptionManager();
        }
        return INSTANCE;
    }

    private void addOptions() {
        applicationOptions = new ArrayList<>();
        applicationOptions.add(new HelpOption());
        applicationOptions.add(new VersionOption());
        applicationOptions.add(new AgentsOption());
        applicationOptions.add(new SettingsOption());
        applicationOptions.add(new EnvironmentOption());
        applicationOptions.add(new BootOption());
        Collections.sort(applicationOptions);
    }

    public synchronized Options toOptions() {
        Options options = new Options();
        getApplicationOptions().forEach(option -> options.addOption(option.toOption()));
        return options;
    }

    public synchronized List<ApplicationOption> getApplicationOptions() {
        return applicationOptions;
    }

    public synchronized ApplicationOption getDefaultOption() {
        return new HelpOption();
    }

}
