/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package translate;

import application.ApplicationSettings;
import settings.SettingsLoader;

public class Translation extends SettingsLoader {

    public static final String ES = "ES";
    public static final String EN = "EN";
    private static final String ES_PATH = "translations/ES.properties";
    private static final String EN_PATH = "translations/EN.properties";
    private static Translation INSTANCE;
    private String currentTranslation;

    private Translation() {
        load();
    }

    public synchronized static Translation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Translation();
        }
        return INSTANCE;
    }

    public synchronized void load() {
        ApplicationSettings settings = ApplicationSettings.getInstance();
        loadLanguage(settings.get(ApplicationSettings.APP_LANGUAGE));
    }

    public String getCurrentTranslation() {
        return currentTranslation;
    }

    public synchronized void loadLanguage(String language) {
        if (language.equalsIgnoreCase(ES)) {
            load(ES_PATH);
            currentTranslation = Translation.ES;
        } else {
            load(EN_PATH);
            currentTranslation = Translation.EN;
        }
    }

    @Override
    public synchronized String get(String key) {
        String value = super.get(key);
        if (value != null) {
            return value;
        } else {
            return key;
        }
    }

}
