package tile;
import java.awt.*;
import javax.swing.*;

public class ChatBox {
    public JLabel label;
    public JTextArea textArea;

    public ChatBox(JLabel label, JTextArea textArea) {
        this.label = label;
        this.textArea = textArea;
    }

    public static ChatBox createChatBox(String imagePath, String message, float sizeFont, int width, int height, int paddingX, int paddingY) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setLayout(null);
        label.setSize(width, height);
        label.setOpaque(false);

        JTextArea textArea = new JTextArea(message);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setOpaque(false);
        textArea.setFont(new Font("Pixellari", Font.PLAIN, (int) sizeFont));

        int textWidth = width - 2 * paddingX;
        int textHeight = height - 2 * paddingY;
        textArea.setBounds(paddingX, paddingY, textWidth, textHeight);

        label.add(textArea);

        return new ChatBox(label, textArea);
    }

    public void resize(int newWidth, int newHeight, int paddingX, int paddingY) {
        ImageIcon icon = (ImageIcon) label.getIcon();
        Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledImage));
        label.setSize(newWidth, newHeight);

        int textWidth = newWidth - 2 * paddingX;
        int textHeight = newHeight - 2 * paddingY;
        textArea.setBounds(paddingX, paddingY, textWidth, textHeight);

        label.revalidate();
        label.repaint();
    }


    public JLabel getLabel() {
        return label;
    }
}