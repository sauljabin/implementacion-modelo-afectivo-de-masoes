/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.message;

import jade.lang.acl.MessageTemplate;

public class MasoesMessageTemplate extends MessageTemplate {

    public MasoesMessageTemplate() {
        super(new MasoesMatchExpression());
    }

}
