package bankify;

import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    private JProgressBar progress;
    private Runnable onFinish; // 🔥 important

    public LoadingScreen(Runnable onFinish) {
        this.onFinish = onFinish;

        setTitle("Loading...");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        JPanel panel = new GradientPanel();
        panel.setLayout(null);
        setContentPane(panel);

        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setBounds(180, 90, 265, 195);
        lblLogo.setIcon(new ImageIcon(
                LoadingScreen.class.getResource("/Resources/loadinglogo.jpg")));
        panel.add(lblLogo);

        progress = new JProgressBar();
        progress.setBounds(50, 320, 500, 20);
        progress.setForeground(new Color(30, 127, 179));
        progress.setBackground(Color.BLACK);
        progress.setFont(new Font("Tw Cen MT", Font.BOLD, 17));
        progress.setStringPainted(true);
        progress.setBorderPainted(false);
        progress.setOpaque(false);
        panel.add(progress);

        setVisible(true);
        fill();
    }

    class GradientPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void fill() {
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    final int value = i;
                    SwingUtilities.invokeLater(() -> {
                        progress.setValue(value);
                        progress.setString("Loading... " + value + "%");
                    });
                    Thread.sleep(35);
                }
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    if (onFinish != null) {
                        onFinish.run();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
