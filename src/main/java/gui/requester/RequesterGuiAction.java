/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.requester;

import translate.Translation;

public enum RequesterGuiAction {

    GET_ALL_SETTINGS("gui.action.get_all_settings"),
    GET_SETTING("gui.action.get_setting"),
    ADD_BEHAVIOUR("gui.action.add_behaviour"),
    REMOVE_BEHAVIOUR("gui.action.remove_behaviour"),
    GET_EMOTIONAL_STATE("gui.action.get_emotional_state"),
    GET_SOCIAL_EMOTION("gui.action.get_social_emotion"),
    EVALUATE_ACTION_STIMULUS("gui.action.evaluate_action_stimulus"),
    EVALUATE_EVENT_STIMULUS("gui.action.evaluate_event_stimulus"),
    EVALUATE_OBJECT_STIMULUS("gui.action.evaluate_object_stimulus"),
    SEND_SIMPLE_CONTENT("gui.action.send_simple_content"),
    NOTIFY_ACTION("gui.action.notify_action"),
    NOTIFY_EVENT("gui.action.notify_event"),
    NOTIFY_OBJECT("gui.action.notify_object"),
    GET_SERVICES("gui.action.get_services"),
    GET_AGENTS("gui.action.get_agents"),
    REGISTER_AGENT("gui.action.register_agent"),
    DEREGISTER_AGENT("gui.action.deregister_agent"),
    SEARCH_AGENT("gui.action.search_agent"),
    CREATE_OBJECT("gui.action.create_object"),
    UPDATE_OBJECT("gui.action.update_object"),
    GET_OBJECT("gui.action.get_object"),
    DELETE_OBJECT("gui.action.delete_object");

    private String translationKey;

    RequesterGuiAction(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public String toString() {
        return Translation.getInstance().get(translationKey);
    }

}
