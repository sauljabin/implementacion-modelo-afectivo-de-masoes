/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;
import jade.settings.ontology.elements.QueryAllSettings;
import jade.settings.ontology.elements.QuerySetting;
import jade.settings.ontology.elements.Setting;
import jade.settings.ontology.elements.SystemSettings;
import jade.settings.ontology.elements.UnexpectedContent;
import jade.settings.ontology.elements.UnexpectedPerformative;

import java.util.Optional;

public class ManualSettingsOntology extends Ontology {

    public static final String ONTOLOGY_NAME = "Settings";
    public static final String CONCEPT_SETTING = "Setting";
    public static final String ACTION_QUERY_ALL_SETTINGS = "QueryAllSettings";
    public static final String ACTION_QUERY_SETTING = "QuerySetting";
    public static final String PREDICATE_UNEXPECTED_PERFORMATIVE = "UnexpectedPerformative";
    public static final String PREDICATE_SYSTEM_SETTINGS = "SystemSettings";
    public static final String PREDICATE_UNEXPECTED_CONTENT = "UnexpectedContent";
    public static final String PRIMITIVE_KEY = "key";
    public static final String PRIMITIVE_VALUE = "value";
    public static final String PRIMITIVE_PERFORMATIVE = "performative";
    public static final String PRIMITIVE_MESSAGE = "message";
    public static final String LIST_SETTINGS = "settings";

    private static Ontology INSTANCE;

    private ManualSettingsOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            ConceptSchema settingSchema = new ConceptSchema(CONCEPT_SETTING);
            add(settingSchema, Setting.class);
            settingSchema.add(PRIMITIVE_VALUE, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            settingSchema.add(PRIMITIVE_KEY, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);

            AgentActionSchema querySettingSchema = new AgentActionSchema(ACTION_QUERY_SETTING);
            add(querySettingSchema, QuerySetting.class);
            querySettingSchema.add(PRIMITIVE_KEY, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);

            AgentActionSchema queryAllSettingsSchema = new AgentActionSchema(ACTION_QUERY_ALL_SETTINGS);
            add(queryAllSettingsSchema, QueryAllSettings.class);

            PredicateSchema unexpectedContentSchema = new PredicateSchema(PREDICATE_UNEXPECTED_CONTENT);
            add(unexpectedContentSchema, UnexpectedContent.class);
            unexpectedContentSchema.add(PRIMITIVE_MESSAGE, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);

            PredicateSchema systemSettingsSchema = new PredicateSchema(PREDICATE_SYSTEM_SETTINGS);
            add(systemSettingsSchema, SystemSettings.class);
            systemSettingsSchema.add(LIST_SETTINGS, settingSchema, 1, ObjectSchema.UNLIMITED);

            PredicateSchema unexpectedPerformativeSchema = new PredicateSchema(PREDICATE_UNEXPECTED_PERFORMATIVE);
            add(unexpectedPerformativeSchema, UnexpectedPerformative.class);
            unexpectedPerformativeSchema.add(PRIMITIVE_MESSAGE, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            unexpectedPerformativeSchema.add(PRIMITIVE_PERFORMATIVE, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
        } catch (Exception e) {
            throw new SettingsOntologyException(e.getMessage(), e);
        }
    }

    public synchronized static Ontology getInstance() {
        if (!Optional.ofNullable(INSTANCE).isPresent()) {
            INSTANCE = new ManualSettingsOntology();
        }
        return INSTANCE;
    }

}
