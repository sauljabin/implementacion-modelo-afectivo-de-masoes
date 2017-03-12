/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package translate;

import application.ApplicationSettings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class TranslationTest {

    private Translation translation;
    private Translation translationSpy;

    @Before
    public void setUp() {
        translation = Translation.getInstance();
        translationSpy = spy(Translation.getInstance());
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(translation, "INSTANCE", null);
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(Translation.getInstance(), is(translation));
    }

    @Test
    public void shouldLoadSpanishLanguage() {
        translationSpy.loadLanguage(Translation.ES);
        verify(translationSpy).loadLanguage(Translation.ES);
        verify(translationSpy).load("translations/ES.properties");
        assertThat(translationSpy.getCurrentTranslation(), is(Translation.ES));
    }

    @Test
    public void shouldLoadSpanishLanguageWithSettings() {
        ApplicationSettings.getInstance().set(ApplicationSettings.APP_LANGUAGE, Translation.ES);
        translationSpy.load();
        verify(translationSpy).load("translations/ES.properties");
        assertThat(translationSpy.getCurrentTranslation(), is(Translation.ES));
    }

    @Test
    public void shouldLoadEnglishLanguage() {
        translationSpy.loadLanguage(Translation.EN);
        verify(translationSpy).loadLanguage(Translation.EN);
        verify(translationSpy).load("translations/EN.properties");
        assertThat(translationSpy.getCurrentTranslation(), is(Translation.EN));
    }

    @Test
    public void shouldLoadEnglishLanguageWithSettings() {
        ApplicationSettings.getInstance().set(ApplicationSettings.APP_LANGUAGE, Translation.EN);
        translationSpy.load();
        verify(translationSpy).load("translations/EN.properties");
        assertThat(translationSpy.getCurrentTranslation(), is(Translation.EN));
    }

    @Test
    public void shouldLoadEnglishByDefaultLanguage() {
        String language = "NO-EXIST";
        translationSpy.loadLanguage(language);
        verify(translationSpy).loadLanguage(language);
        verify(translationSpy).load("translations/EN.properties");
        assertThat(translationSpy.getCurrentTranslation(), is(Translation.EN));
    }

    @Test
    public void shouldLoadEnglishByDefaultLanguageWithSettings() {
        String language = "NO-EXIST";
        ApplicationSettings.getInstance().set(ApplicationSettings.APP_LANGUAGE, language);
        translationSpy.load();
        verify(translationSpy).load("translations/EN.properties");
        assertThat(translationSpy.getCurrentTranslation(), is(Translation.EN));
    }

}