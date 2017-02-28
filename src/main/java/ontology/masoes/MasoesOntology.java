/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.content.onto.BeanOntology;
import ontology.SetupOntologyException;
import util.ToStringBuilder;

public class MasoesOntology extends BeanOntology {

    public static final String NAME = "masoes";
    public static final String ACTION_GET_EMOTIONAL_STATE = "GetEmotionalState";
    public static final String ACTION_EVALUATE_STIMULUS = "EvaluateStimulus";
    public static final String ACTION_NOTIFY_ACTION = "NotifyAction";
    public static final String ACTION_CREATE_OBJECT = "CreateObject";
    public static final String ACTION_DELETE_OBJECT = "DeleteObject";
    public static final String ACTION_GET_OBJECT = "GetObject";
    public static final String ACTION_UPDATE_OBJECT = "UpdateObject";
    private static MasoesOntology INSTANCE;

    private MasoesOntology() {
        super(NAME);
        try {
            add(MasoesOntology.class.getPackage().getName());
        } catch (Exception e) {
            throw new SetupOntologyException(e);
        }
    }

    public synchronized static MasoesOntology getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MasoesOntology();
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
