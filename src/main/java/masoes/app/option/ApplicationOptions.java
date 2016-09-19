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

public class ApplicationOptions {

    List<ApplicationOption> applicationOptions;

    public ApplicationOptions(List<ApplicationOption> applicationOptions) {
        this.applicationOptions = applicationOptions;
    }

    public ApplicationOptions() {
        this(new ArrayList<>());
        addOptions();
    }

    private void addOptions() {
        applicationOptions.add(new HelpOption(this));
        applicationOptions.add(new VersionOption());
        applicationOptions.add(new JadeOption());
        Collections.sort(applicationOptions);
    }

    public List<ApplicationOption> getApplicationOptions() {
        return applicationOptions;
    }

    public Options toOptions() {
        Options options = new Options();
        getApplicationOptions().forEach(option -> options.addOption(option.toOption()));
        return options;
    }

}
