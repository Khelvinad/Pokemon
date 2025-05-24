import java.awt.*;
import java.io.*;
import javax.swing.*;

abstract class MenuButtonAction {
    protected Frame frameApp;
    protected JFrame frame;
    protected Font customFont;

    public MenuButtonAction(Frame frameApp) {
        this.frameApp = frameApp;
        this.frame = frameApp.getFrame();
        loadCustomFont("src/Asset/Pixellari.ttf");
    }

    public abstract void execute();

    protected JButton createBackButton() {
        JButton backButton = new JButton("Back");
        backButton.setHorizontalAlignment(SwingConstants.LEFT);
        backButton.setVerticalAlignment(SwingConstants.CENTER);
        backButton.setPreferredSize(new Dimension(100, 50));
        backButton.setMargin(new Insets(2, 5, 2, 5));
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
}
