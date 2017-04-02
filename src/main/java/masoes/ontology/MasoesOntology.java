/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.Predicate;
import jade.content.onto.BeanOntology;
import ontology.SetupOntologyException;
import org.reflections.Reflections;
import util.ToStringBuilder;

import java.util.Set;
import java.util.stream.Collectors;

public class MasoesOntology extends BeanOntology {

    public static final String NAME = "masoes";
    public static final String ACTION_GET_EMOTIONAL_STATE = "GetEmotionalState";
    public static final String ACTION_GET_SOCIAL_EMOTION = "GetSocialEmotion";
    public static final String ACTION_EVALUATE_STIMULUS = "EvaluateStimulus";

    public static final String ACTION_NOTIFY_ACTION = "NotifyAction";
    public static final String ACTION_NOTIFY_EVENT = "NotifyEvent";
    public static final String ACTION_NOTIFY_OBJECT = "NotifyObject";

    public static final String ACTION_CREATE_OBJECT = "CreateObject";
    public static final String ACTION_DELETE_OBJECT = "DeleteObject";
    public static final String ACTION_GET_OBJECT = "GetObject";
    public static final String ACTION_UPDATE_OBJECT = "UpdateObject";

    private static MasoesOntology INSTANCE;
    private static final Reflections REFLECTIONS = new Reflections(MasoesOntology.class.getPackage().getName());

    private MasoesOntology() {
        super(NAME);
        Set<String> packages = getConceptPackages();
        packages.addAll(getPredicatePackages());
        packages.addAll(getActionPackages());

        packages.forEach(
                packageName -> {
                    try {
                        add(packageName);
                    } catch (Exception e) {
                        throw new SetupOntologyException(e);
                    }
                }
        );
    }

    private Set<String> getActionPackages() {
        Set<Class<? extends AgentAction>> subTypesOfAgentAction = REFLECTIONS.getSubTypesOf(AgentAction.class);
        return subTypesOfAgentAction.stream()
                .map(aClass -> aClass.getPackage().getName())
                .collect(Collectors.toSet());
    }

    private Set<String> getPredicatePackages() {
        Set<Class<? extends Predicate>> subTypesOfPredicate = REFLECTIONS.getSubTypesOf(Predicate.class);
        return subTypesOfPredicate.stream()
                .map(aClass -> aClass.getPackage().getName())
                .collect(Collectors.toSet());
    }

    private Set<String> getConceptPackages() {
        Set<Class<? extends Concept>> subTypesOfConcept = REFLECTIONS.getSubTypesOf(Concept.class);
        return subTypesOfConcept.stream()
                .map(aClass -> aClass.getPackage().getName())
                .collect(Collectors.toSet());
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
