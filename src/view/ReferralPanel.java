package view;

import javax.swing.*;
import java.awt.*;

public class ReferralPanel extends JPanel {

    public ReferralPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Referral Panel (Singleton used here)", SwingConstants.CENTER),
                BorderLayout.CENTER);
    }
}
