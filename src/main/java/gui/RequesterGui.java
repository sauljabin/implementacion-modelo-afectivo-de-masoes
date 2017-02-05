/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui;

import javax.swing.*;
import java.awt.*;

public class RequesterGui extends JFrame {

    private static final String ONTOLOGY_REQUESTER_GUI = "Requester GUI";

    public void setUp() {
        setTitle(ONTOLOGY_REQUESTER_GUI);
        setSize(1024, 768);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(this);
        setVisible(true);
    }

    public void closeGui() {
        setVisible(false);
        dispose();
    }

}
