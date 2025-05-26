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

    public JLabel createChatBoxLabel(String message,float sizeFont, int width, int height, int paddingX, int paddingY) {
        String imagePath = "src/Asset/chatbox.png";
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setLayout(null);
        label.setSize(width, height);
        label.setOpaque(false);

        JTextArea textArea = new JTextArea(message);
        textArea.setBackground(Color.WHITE);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setOpaque(true);
        textArea.setBackground(Color.WHITE);
        textArea.setFont(customFont.deriveFont(Font.PLAIN, sizeFont));

        int textWidth = width - 2 * paddingX;
        int textHeight = height - 2 * paddingY;

        int textX = (width - textWidth) / 2;  
        int textY = (height - textHeight) / 2;
        textArea.setBounds(textX, textY, textWidth, textHeight);

        label.add(textArea);

        return label;
    }



}
