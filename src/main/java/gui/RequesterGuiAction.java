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
    EVALUATE_STIMULUS("Evaluate stimulus"),
    SEND_SIMPLE_CONTENT("Send simple content");

    private String name;

    RequesterGuiAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
