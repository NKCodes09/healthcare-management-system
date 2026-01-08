package view;

import javax.swing.*;
import java.awt.*;

public class FacilityPanel extends JPanel {

    public FacilityPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Facility Panel", SwingConstants.CENTER),
                BorderLayout.CENTER);
    }
}
