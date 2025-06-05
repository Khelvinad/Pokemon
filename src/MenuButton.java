import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

abstract class MenuButtonAction {
    protected Frame frameApp;
    protected JFrame frame;
    protected Font customFont;
    protected BufferedImage backgroundImage;

    public MenuButtonAction(Frame frameApp) {
        this.frameApp = frameApp;
        this.frame = frameApp.getFrame();
        loadCustomFont("src/Asset/Pixellari.ttf");
    }

    public abstract void execute();

    protected JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setFont(customFont.deriveFont(Font.PLAIN, 25f));
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frameApp.startPage();
            frame.revalidate();
            frame.repaint();
        });
        return backButton;
    }

    protected  void loadCustomFont(String fontPath) {
        try {
            File fontFile = new File(fontPath);
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    protected JPanel createBackgroundPanel(String imagePath) {
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
}