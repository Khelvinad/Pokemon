package tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class ChatBox {
    private JLabel label;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    

    public ChatBox(JLabel label, JTextArea textArea, JScrollPane scrollPane) {
        this.label = label;
        this.textArea = textArea;
        this.scrollPane = scrollPane;
    }

    public static ChatBox createChatBox(String imagePath, String message, float sizeFont, int width, int height, int paddingX, int paddingY) {
        ImageIcon icon = null;
        java.net.URL imgUrl = ChatBox.class.getResource(imagePath);

        if (imgUrl != null) {
            icon = new ImageIcon(imgUrl);
            if (icon.getImage() == null || icon.getIconWidth() == -1 || icon.getIconHeight() == -1) {
                icon = null;
            }
        }

        Image finalImage;
        if (icon != null && icon.getImage() != null) {
            finalImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } else {
            BufferedImage placeholder = new BufferedImage(Math.max(1, width), Math.max(1, height), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = placeholder.createGraphics();
            g2d.setComposite(AlphaComposite.Clear);
            g2d.fillRect(0, 0, width, height);
            g2d.dispose();
            finalImage = placeholder;
        }

        JLabel backgroundLabel = new JLabel(new ImageIcon(finalImage));
        backgroundLabel.setLayout(null);
        backgroundLabel.setSize(width, height);
        backgroundLabel.setOpaque(false);

        JTextArea textArea = new JTextArea(message);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setOpaque(false);
        textArea.setFont(new Font("Pixellari", Font.PLAIN, (int) sizeFont));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        int textWidth = width - (2 * paddingX);
        int textHeight = height - (2 * paddingY);
        scrollPane.setBounds(paddingX, paddingY, textWidth, textHeight);

        backgroundLabel.add(scrollPane);

        return new ChatBox(backgroundLabel, textArea, scrollPane);
    }

    public void resize(int newWidth, int newHeight, int paddingX, int paddingY) {
        ImageIcon currentIcon = (ImageIcon) this.label.getIcon();
        if (currentIcon != null && currentIcon.getImage() != null &&
            currentIcon.getIconWidth() > 0 && currentIcon.getIconHeight() > 0) {
            Image scaledImage = currentIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            this.label.setIcon(new ImageIcon(scaledImage));
        } else if (currentIcon != null) {
            BufferedImage placeholder = new BufferedImage(Math.max(1, newWidth), Math.max(1, newHeight), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = placeholder.createGraphics();
            g2d.setComposite(AlphaComposite.Clear);
            g2d.fillRect(0, 0, newWidth, newHeight);
            g2d.dispose();
            this.label.setIcon(new ImageIcon(placeholder));
        }

        this.label.setSize(newWidth, newHeight);

        int textWidth = newWidth - (2 * paddingX);
        int textHeight = newHeight - (2 * paddingY);
        if (this.scrollPane != null) {
            this.scrollPane.setBounds(paddingX, paddingY, textWidth, textHeight);
        }

        this.label.revalidate();
        this.label.repaint();
    }

    public JLabel getLabel() {
        return this.label;
    }

    public JTextArea getTextArea() {
        return this.textArea;
    }
}
