/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.ontology.base;

import jade.content.onto.BasicOntology;
import jade.content.onto.BeanOntology;
import jade.ontology.SetupOntologyException;

public class BaseOntology extends BeanOntology {

    public static final String ONTOLOGY_NAME = "base";

    public BaseOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            add(BaseOntology.class.getPackage().getName());
        } catch (Exception e) {
            throw new SetupOntologyException(e.getMessage(), e);
        }
    }

}
