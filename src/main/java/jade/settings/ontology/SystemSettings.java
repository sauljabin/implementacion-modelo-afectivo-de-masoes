/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.ontology;

import jade.content.Predicate;
import jade.util.leap.List;


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

}