package bankify.admin;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AdminMyProfile extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPanel;
    private JTextField textField, textField_1, textField_3, textField_4, textField_5;
    private DatePicker datePicker;
    private JLabel err1, err2, err3, err4, err5, err6;

    public AdminMyProfile() {
        setTitle("Bankify Admin - My Profile");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Sidebar ခေါ်တဲ့နေရာမှာ "Settings" အစား "AdminProfile" လို့ ပြောင်းပေးလိုက်ပါ
        // ဒါမှ Sidebar က Settings ခလုတ်ကို နှိပ်ရင် AdminSettingsPage ကို ပြန်သွားမှာပါ
        AdminSidebar sidebar = new AdminSidebar(this, "AdminProfile");
        
        contentPanel = createContentPanel();

        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 127, 179));
        panel.setLayout(null);

     // --- Header (Pill Shape) ---
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 70, 70);
                g2.dispose();
            }
        };
        headerPanel.setLayout(null);
        headerPanel.setBackground(new Color(0, 191, 255));
        
        // Header အကျယ်ကို ၂၆၀ အထိ တိုးလိုက်ပါပြီ (စာသား အဝင်အထွက် အဆင်ပြေအောင်)
        headerPanel.setBounds(330, 60, 260, 70); 

        JLabel iconLabel = new JLabel("");
        // Icon ကို ဘယ်ဘက်ကနေ ၂၀ ပေးထားပါတယ်
        iconLabel.setBounds(20, 12, 55, 45); 
        URL iconURL = getClass().getResource("/Resources/my_profile.png");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            Image scaled = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaled));
        }
        headerPanel.add(iconLabel);

        JLabel titleLabel = new JLabel("Admin Profile");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tw Cen MT", Font.BOLD, 24));
        
        // --- Padding ထည့်သည့်နေရာ ---
        // x ကို ၈၅ အထိ တိုးလိုက်ပါတယ် (Icon နဲ့ စာသားကြား Space ပိုရသွားပါလိမ့်မယ်)
        titleLabel.setBounds(85, 12, 160, 45); 
        
        headerPanel.add(titleLabel);
        panel.add(headerPanel);
        // --- Styles & Positioning ---
        Font labelFont = new Font("Tw Cen MT", Font.BOLD, 18);
        Font fieldFont = new Font("Tw Cen MT", Font.PLAIN, 18);
        Font errorFont = new Font("Tw Cen MT", Font.BOLD, 14);
        Color errorColor = Color.RED;

        int fieldWidth = 370;
        int fieldHeight = 45;
        int col1X = 60;
        int col2X = 460;

        // Row 1: First Name & Last Name
        addLabel(panel, "First Name", col1X, 200, labelFont);
        textField = createField(panel, col1X, 240, fieldWidth, fieldHeight, fieldFont);
        err1 = createErrorLabel(panel, col1X, 285, fieldWidth, errorFont, errorColor);

        addLabel(panel, "Last Name", col2X, 200, labelFont);
        textField_1 = createField(panel, col2X, 240, fieldWidth, fieldHeight, fieldFont);
        err2 = createErrorLabel(panel, col2X, 285, fieldWidth, errorFont, errorColor);

        // Row 2: DOB & Address
        addLabel(panel, "Date of Birth", col1X, 320, labelFont);
        setupDatePicker(panel, col1X, 360, fieldWidth, fieldHeight);
        err3 = createErrorLabel(panel, col1X, 405, fieldWidth, errorFont, errorColor);

        addLabel(panel, "Address", col2X, 320, labelFont);
        textField_3 = createField(panel, col2X, 360, fieldWidth, fieldHeight, fieldFont);
        err4 = createErrorLabel(panel, col2X, 405, fieldWidth, errorFont, errorColor);

        // Row 3: Email & Phone
        addLabel(panel, "Email", col1X, 440, labelFont);
        textField_4 = createField(panel, col1X, 480, fieldWidth, fieldHeight, fieldFont);
        err5 = createErrorLabel(panel, col1X, 525, fieldWidth, errorFont, errorColor);

        addLabel(panel, "Phone Number", col2X, 440, labelFont);
        textField_5 = createField(panel, col2X, 480, fieldWidth, fieldHeight, fieldFont);
        err6 = createErrorLabel(panel, col2X, 525, fieldWidth, errorFont, errorColor);

        // Buttons
        JButton btnEdit = new RoundedCornerButton("Edit");
        btnEdit.setBounds(col2X + 130, 580, 100, 55);
        btnEdit.addActionListener(e -> enableEditing());
        panel.add(btnEdit);

        JButton btnSave = new RoundedCornerButton("Save");
        btnSave.setBounds(col2X + 250, 580, 100, 55);
        btnSave.addActionListener(e -> saveProfile());
        panel.add(btnSave);

        disableTextFields();
        return panel;
    }

    // --- Helper Methods for UI ---
    private void addLabel(JPanel p, String text, int x, int y, Font f) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(f);
        l.setBounds(x, y, 150, 30);
        p.add(l);
    }

    private JTextField createField(JPanel p, int x, int y, int w, int h, Font f) {
        JTextField tf = new JTextField();
        tf.setFont(f);
        tf.setBounds(x, y, w, h);
        p.add(tf);
        return tf;
    }

    private JLabel createErrorLabel(JPanel p, int x, int y, int w, Font f, Color c) {
        JLabel l = new JLabel("");
        l.setForeground(c);
        l.setFont(f);
        l.setBounds(x, y, w, 25);
        p.add(l);
        return l;
    }

    private void setupDatePicker(JPanel p, int x, int y, int w, int h) {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        settings.setAllowEmptyDates(false);
        datePicker = new DatePicker(settings);
        datePicker.setBounds(x, y, w, h);
        datePicker.getComponentDateTextField().setFont(new Font("Tw Cen MT", Font.PLAIN, 18));
        
        URL calIcon = getClass().getResource("/Resources/calendar.png");
        if (calIcon != null) {
            ImageIcon icon = new ImageIcon(calIcon);
            Image scaled = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            datePicker.getComponentToggleCalendarButton().setIcon(new ImageIcon(scaled));
            datePicker.getComponentToggleCalendarButton().setText("");
        }
        p.add(datePicker);
    }

    // --- Logic Methods ---
    private void enableEditing() {
        textField.setEditable(true);
        textField_1.setEditable(true);
        datePicker.setEnabled(true);
        textField_3.setEditable(true);
        textField_4.setEditable(true);
        textField_5.setEditable(true);
        clearErrors();
    }

    private void disableTextFields() {
        textField.setEditable(false);
        textField_1.setEditable(false);
        if(datePicker != null) datePicker.setEnabled(false);
        textField_3.setEditable(false);
        textField_4.setEditable(false);
        textField_5.setEditable(false);
    }

    private void clearErrors() {
        err1.setText(""); err2.setText(""); err3.setText("");
        err4.setText(""); err5.setText(""); err6.setText("");
    }

    private void saveProfile() {
        clearErrors();
        boolean hasError = false;

        if (textField.getText().trim().isEmpty()) { err1.setText("! First name required"); hasError = true; }
        if (textField_1.getText().trim().isEmpty()) { err2.setText("! Last name required"); hasError = true; }
        if (datePicker.getDate() == null) { err3.setText("! Date of birth required"); hasError = true; }
        if (textField_3.getText().trim().isEmpty()) { err4.setText("! Address required"); hasError = true; }

        String email = textField_4.getText().trim();
        if (email.isEmpty()) {
            err5.setText("! Email is required");
            hasError = true;
        } else if (!isValidEmail(email)) {
            err5.setText("! Invalid Gmail format");
            hasError = true;
        }

        String phone = textField_5.getText().trim();
        if (phone.isEmpty()) {
            err6.setText("! Phone required");
            hasError = true;
        } else if (!isValidPhoneNumber(phone)) {
            err6.setText("! Invalid (10-15 digits)");
            hasError = true;
        }

        if (!hasError) {
            JOptionPane.showMessageDialog(this, "Admin Profile saved successfully!");
            disableTextFields();
        }
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@gmail\\.com$").matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        String digits = phone.replaceAll("[^0-9]", "");
        return digits.length() >= 10 && digits.length() <= 15;
    }

    // --- Custom Components ---
    private class RoundedCornerButton extends JButton {
        private Color baseColor;
        public RoundedCornerButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            baseColor = text.equals("Edit") ? new Color(220, 20, 60) : new Color(50, 205, 50);
            setBackground(baseColor);
            setForeground(Color.WHITE);
            setFont(new Font("Tw Cen MT", Font.BOLD, 18));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 55, 55);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminMyProfile().setVisible(true));
    }
}