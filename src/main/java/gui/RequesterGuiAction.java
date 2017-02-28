/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

public enum RequesterGuiAction {

    GET_ALL_SETTINGS("Get all settings"),
    GET_SETTING("Get setting"),
    ADD_BEHAVIOUR("Add behaviour"),
    REMOVE_BEHAVIOUR("Remove behaviour"),
    GET_EMOTIONAL_STATE("Get emotional state"),
    EVALUATE_ACTION_STIMULUS("Evaluate action stimulus"),
    EVALUATE_OBJECT_STIMULUS("Evaluate object stimulus"),
    SEND_SIMPLE_CONTENT("Send simple content"),
    NOTIFY_ACTION("Notify action"),
    GET_SERVICES("Get agent services"),
    GET_AGENTS("Get agents"),
    REGISTER_AGENT("Register agent"),
    DEREGISTER_AGENT("Deregister agent"),
    SEARCH_AGENT("Search agent in DF"),
    CREATE_OBJECT("Create object"),
    UPDATE_OBJECT("Update object"),
    GET_OBJECT("Get object"),
    DELETE_OBJECT("Delete object");

    private String name;

    RequesterGuiAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
