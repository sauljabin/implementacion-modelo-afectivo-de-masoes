/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package language;

import jade.content.lang.sl.SLCodec;

public class SemanticLanguage extends SLCodec {

    public static final String NAME = "fipa-sl";
    private static SemanticLanguage INSTANCE;

    private SemanticLanguage() {
    }

    public synchronized static SemanticLanguage getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SemanticLanguage();
        }
        return INSTANCE;
    }

}
