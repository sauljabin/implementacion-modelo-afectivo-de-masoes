/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.core.ontology;

import jade.content.onto.BeanOntology;
import jade.ontology.SetupOntologyException;
import jade.ontology.base.BaseOntology;

import java.util.Optional;

public class MasoesOntology extends BeanOntology {

    public static final String ONTOLOGY_NAME = "base";
    private static MasoesOntology INSTANCE;

    private MasoesOntology() {
        super(ONTOLOGY_NAME, BaseOntology.getInstance());
        initialize();
    }

    public synchronized static MasoesOntology getInstance() {
        if (!Optional.ofNullable(INSTANCE).isPresent()) {
            INSTANCE = new MasoesOntology();
        }
        return INSTANCE;
    }

    private void initialize() {
        try {
            add(MasoesOntology.class.getPackage().getName());
        } catch (Exception e) {
            throw new SetupOntologyException(e.getMessage(), e);
        }
    }

}
