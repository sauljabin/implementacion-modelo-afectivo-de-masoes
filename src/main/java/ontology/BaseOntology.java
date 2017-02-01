/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.BeanOntology;
import util.ToStringBuilder;

public class BaseOntology extends BeanOntology {

    public static final String ONTOLOGY_NAME = "base";

    public BaseOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            add(BaseOntology.class.getPackage().getName());
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
