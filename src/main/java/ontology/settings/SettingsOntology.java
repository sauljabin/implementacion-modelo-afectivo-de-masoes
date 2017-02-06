/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.settings;

import jade.content.onto.BeanOntology;
import ontology.SetupOntologyException;
import util.ToStringBuilder;

public class SettingsOntology extends BeanOntology {

    public static final String NAME = "settings";
    private static SettingsOntology INSTANCE;

    private SettingsOntology() {
        super(NAME);
        try {
            add(SettingsOntology.class.getPackage().getName());
        } catch (Exception e) {
            throw new SetupOntologyException(e);
        }
    }

    public synchronized static SettingsOntology getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SettingsOntology();
        }
        return INSTANCE;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("name", getName())
                .append("actions", getActionNames().toArray())
                .append("predicates", getPredicateNames().toArray())
                .append("concepts", getConceptNames().toArray())
                .toString();
    }

}
