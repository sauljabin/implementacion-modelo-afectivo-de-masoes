/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package settings.ontology;

import jade.content.Predicate;
import jade.util.leap.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class SystemSettings implements Predicate {

    private List settings;

    public SystemSettings() {
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("settings", settings.toArray())
                .toString();
    }

}