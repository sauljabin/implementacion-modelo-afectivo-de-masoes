/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

public enum RequesterGuiAction {

    GET_ALL_SETTINGS("Consultar configuraciones"),
    GET_SETTING("Consultar configuración"),
    ADD_BEHAVIOUR("Agregar comportamiento"),
    REMOVE_BEHAVIOUR("Remover comportamiento"),
    GET_EMOTIONAL_STATE("Consultar estado emocional"),
    EVALUATE_ACTION_STIMULUS("Evaluar acción"),
    EVALUATE_OBJECT_STIMULUS("Evaluar objeto"),
    SEND_SIMPLE_CONTENT("Enviar contenido simple"),
    NOTIFY_ACTION("Notificar Acción"),
    GET_SERVICES("Consultar servicios"),
    GET_AGENTS("Consultar agentes"),
    REGISTER_AGENT("Registrar agente"),
    DEREGISTER_AGENT("Eliminar registro de agente"),
    SEARCH_AGENT("Buscar agente en DF"),
    CREATE_OBJECT("Crear objeto"),
    UPDATE_OBJECT("Actualizar objeto"),
    GET_OBJECT("Obtener objeto"),
    DELETE_OBJECT("Eliminar objeto");

    private String name;

    RequesterGuiAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
