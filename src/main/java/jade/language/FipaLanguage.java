/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.language;

import jade.content.lang.sl.SLCodec;
import jade.domain.FIPANames;

import java.util.Optional;

public class FipaLanguage extends SLCodec {

    public static final String LANGUAGE_NAME = FIPANames.ContentLanguage.FIPA_SL;
    private static FipaLanguage INSTANCE;

    private FipaLanguage() {

    }

    public synchronized static FipaLanguage getInstance() {
        if (!Optional.ofNullable(INSTANCE).isPresent()) {
            INSTANCE = new FipaLanguage();
        }
        return INSTANCE;
    }

}
