/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ApplicationOptions extends Options {

    public ApplicationOptions() {
        addOptions();
    }

    private void addOptions() {
        this.addOption(new HelpOption(this));
        this.addOption(new VersionOption());
        this.addOption(new JadeOption());
    }

    @Override
    public Collection<Option> getOptions() {
        Collection<Option> options = super.getOptions();
        List<ApplicationOption> returnOption = new ArrayList<>();
        for (Option option : options) {
            ApplicationOption applicationOption = (ApplicationOption) option;
            returnOption.add(applicationOption);
        }
        Collections.sort(returnOption);
        return new ArrayList<>(returnOption);
    }

}
