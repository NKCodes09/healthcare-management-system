package view;

import javax.swing.*;
import java.awt.*;

public class StaffPanel extends JPanel {

    public StaffPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Staff Panel", SwingConstants.CENTER),
                BorderLayout.CENTER);
    }
}
