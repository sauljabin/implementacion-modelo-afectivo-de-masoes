/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.configurable;

import jade.content.onto.BeanOntology;
import ontology.SetupOntologyException;
import util.ToStringBuilder;

public class ConfigurableOntology extends BeanOntology {

    public static final String ONTOLOGY_NAME = "configurable";

    public ConfigurableOntology() {
        super(ONTOLOGY_NAME);
        try {
            add(ConfigurableOntology.class.getPackage().getName());
        } catch (Exception e) {
            throw new SetupOntologyException(e);
        }
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
