/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

import jade.Boot;

public class JadeOption extends ApplicationOption {

    public JadeOption() {
        super("j", "jade", true, "Start Jade framework with arguments");
    }

    @Override
    public void exec(String optionValue) {
        Boot.main(optionValue.split(" "));
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
