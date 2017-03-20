/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings.ontology;

import jade.content.Predicate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import util.ToStringBuilder;

public class SystemSettings implements Predicate {

    private List settings;

    public SystemSettings() {
        this(new ArrayList());
    }

    public SystemSettings(List settings) {
        this.settings = settings;
    }

    public List getSettings() {
        return settings;
    }

    public void setSettings(List settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("settings", settings.toArray())
                .toString();
    }

}
