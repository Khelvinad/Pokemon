import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Frame {
    private BufferedImage backgroundImage;
    private JFrame frame;
    private Font customFont;

    public Frame() {
        frame = new JFrame("Pokemon Battle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 607);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        loadCustomFont("src/Asset/Pixellari.ttf");

        startPage();
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    private void loadCustomFont(String fontPath) {
        try {
            File fontFile = new File(fontPath);
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            customFont = new Font("Arial", Font.PLAIN, 12);
            e.printStackTrace();
        }
    }

    private JPanel createBackgroundPanel(String imagePath) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    backgroundImage = ImageIO.read(new File(imagePath));
                    if (backgroundImage != null) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    g.setColor(Color.RED);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setLayout(null);
        return panel;
    }
    public void startPage() {
        JPanel backgroundPanel = createBackgroundPanel("src/Asset/startBG.jpg");
        backgroundPanel.setLayout(null);

        JPanel centerPan = new JPanel();
        centerPan.setBounds(390, 100, 300, 400);
        centerPan.setOpaque(false);
        centerPan.setLayout(new BoxLayout(centerPan, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(200, 50);

        JButton startButton = new JButton("Start");
        startButton.setFont(customFont.deriveFont(Font.BOLD, 18f));
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(buttonSize);
        startButton.setMaximumSize(buttonSize);
        startButton.setMinimumSize(buttonSize);

        JButton loadButton = new JButton("Load");
        loadButton.setFont(customFont.deriveFont(Font.BOLD, 18f));
        loadButton.setBackground(Color.WHITE);
        loadButton.setForeground(Color.BLACK);
        loadButton.setFocusPainted(false);
        loadButton.setPreferredSize(buttonSize);
        loadButton.setMaximumSize(buttonSize);
        loadButton.setMinimumSize(buttonSize);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(customFont.deriveFont(Font.BOLD, 18f));
        exitButton.setBackground(Color.WHITE);
        exitButton.setForeground(Color.BLACK);
        exitButton.setPreferredSize(buttonSize);
        exitButton.setMaximumSize(buttonSize);
        exitButton.setMinimumSize(buttonSize);

        centerPan.add(Box.createRigidArea(new Dimension(0, 240)));
        centerPan.add(startButton);
        centerPan.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPan.add(loadButton);
        centerPan.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPan.add(exitButton);

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        StartButtonAction startAction = new StartButtonAction(this);
        LoadButtonAction loadAction = new LoadButtonAction(this);

        startButton.addActionListener(e -> startAction.execute());
        loadButton.addActionListener(e -> loadAction.execute());
        exitButton.addActionListener(e -> System.exit(0));

        backgroundPanel.add(centerPan);
        frame.setContentPane(backgroundPanel);
        frame.revalidate();
        frame.repaint();
    }
}