/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.ontology.elements;

import jade.content.Predicate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;


public class SystemSettings implements Predicate {

    private List settings;

    public List getSettings() {
        settings = new ArrayList();
        return settings;
    }

    public void setSettings(List settings) {
        this.settings = settings;
    }

}
