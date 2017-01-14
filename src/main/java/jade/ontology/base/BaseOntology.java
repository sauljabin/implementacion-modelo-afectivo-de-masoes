/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.ontology.base;

import jade.content.onto.BeanOntology;
import jade.ontology.SetupOntologyException;

import java.util.Optional;

public class BaseOntology extends BeanOntology {

    public static final String ONTOLOGY_NAME = "base";
    private static BaseOntology INSTANCE;

    private BaseOntology() {
        super(ONTOLOGY_NAME);
        initialize();
    }

    private void initialize() {
        try {
            add(BaseOntology.class.getPackage().getName());
        } catch (Exception e) {
            throw new SetupOntologyException(e.getMessage(), e);
        }
    }

    public synchronized static BaseOntology getInstance() {
        if (!Optional.ofNullable(INSTANCE).isPresent()) {
            INSTANCE = new BaseOntology();
        }
        return INSTANCE;
    }

}
