package view;

import javax.swing.*;
import java.awt.*;

public class PrescriptionPanel extends JPanel {

    public PrescriptionPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Prescription Panel (to be implemented)", SwingConstants.CENTER),
                BorderLayout.CENTER);
    }
}
