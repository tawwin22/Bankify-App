package bankify;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class depositAgent extends JFrame {

    private String userName;
    private String dateTime;

    public depositAgent(String name, String date) {
        this.userName = name;
        this.dateTime = date;
        setupFrame();
    }

    public depositAgent() {
        this("Maung Maung", "2026/01/20 10:30 AM");
    }

    private void setupFrame() {
        setTitle("Bankify - Agent Approval");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel contentPanel = createContent();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 127, 179));
        panel.setLayout(null);

        // --- Layout Variables ---
        int pillWidth = 220; 
        int pillHeight = 60;
        int pillX = (1200 - pillWidth) / 2;
        int pillY = 80;

        // --- 0. Back Icon (Placed near Agent Box) ---
        JButton btnBack = new JButton();
        try {
            URL backUrl = getClass().getResource("/Resources/exit_arrow.png");
            if (backUrl != null) {
                btnBack.setIcon(new ImageIcon(new ImageIcon(backUrl).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            } else { btnBack.setText("<-"); }
        } catch (Exception e) { btnBack.setText("<-"); }

        // Agent box ရဲ့ ဘယ်ဘက် အကွာအဝေး 80 လောက်မှာ ထားပေးလိုက်ပါတယ်
        btnBack.setBounds(pillX - 150, pillY + 5, 50, 50); 
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            this.dispose();
            new AgentRequestListPage().setVisible(true);
        });
        panel.add(btnBack);

        // --- 1. Agent Pill Header ---
        JPanel pillPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 191, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 60, 60);
                g2.dispose();
            }
        };
        pillPanel.setBounds(pillX, pillY, pillWidth, pillHeight);
        pillPanel.setOpaque(false);
        pillPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 12));
        pillPanel.add(createScaledImageLabel("/Resources/agent.png", 35, 35));
        
        JLabel lblPillText = new JLabel("Agent");
        lblPillText.setForeground(Color.WHITE);
        lblPillText.setFont(new Font("Tw Cen MT", Font.BOLD, 28));
        pillPanel.add(lblPillText);
        panel.add(pillPanel);

        // --- 2. Fields (User ID, Name, Amount, Date) ---
        int contentWidth = 500;
        int centerX = (1200 - contentWidth) / 2;
        
        createReadOnlyField(panel, "User ID", "987654", centerX, 180, contentWidth);
        createReadOnlyField(panel, "User Name", userName, centerX, 280, contentWidth);
        createReadOnlyField(panel, "Amount", "100,000 MMK", centerX, 380, contentWidth);
        createReadOnlyField(panel, "Date & Time", dateTime, centerX, 480, contentWidth);

        // --- 3. Buttons (Accept & Deny) ---
        RoundedButton btnAccept = new RoundedButton("Accept");
        btnAccept.setBounds(centerX, 610, 220, 60);
        btnAccept.setBackground(new Color(50, 205, 50));
        // Action Listener ပြန်ထည့်ထားပါတယ်
        btnAccept.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Request for " + userName + " has been Approved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new AgentRequestListPage().setVisible(true);
        });
        panel.add(btnAccept);

        RoundedButton btnDeny = new RoundedButton("Deny");
        btnDeny.setBounds(centerX + 280, 610, 220, 60);
        btnDeny.setBackground(new Color(220, 20, 60));
        // Action Listener ပြန်ထည့်ထားပါတယ်
        btnDeny.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Request for " + userName + " has been Denied.", "Declined", JOptionPane.WARNING_MESSAGE);
            this.dispose();
            new AgentRequestListPage().setVisible(true);
        });
        panel.add(btnDeny);

        return panel;
    }

    private void createReadOnlyField(JPanel panel, String labelText, String valueText, int x, int y, int width) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Tw Cen MT", Font.BOLD, 20));
        label.setBounds(x, y, width, 30);
        panel.add(label);

        JPanel fieldPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        fieldPanel.setOpaque(false);
        fieldPanel.setBounds(x, y + 35, width, 50);
        fieldPanel.setLayout(null);

        JLabel valueLabel = new JLabel(valueText);
        valueLabel.setFont(new Font("Tw Cen MT", Font.BOLD, 22));
        valueLabel.setBounds(20, 0, width - 40, 50);
        fieldPanel.add(valueLabel);
        panel.add(fieldPanel);
    }

    private JLabel createScaledImageLabel(String path, int width, int height) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) return new JLabel(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        } catch (Exception e) {}
        return new JLabel();
    }

    private class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Tw Cen MT", Font.BOLD, 22));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new depositAgent().setVisible(true));
    }
}