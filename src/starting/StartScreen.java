package starting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class StartScreen extends JFrame {
    private final List<Runnable> onStartListeners = new ArrayList<>();

    public StartScreen() {
        setTitle("Pokemon Battle");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Load gambar background
        ImageIcon bgImage = new ImageIcon(getClass().getResource("src/Asset/pokemon.png"));
        JLabel background = new JLabel(bgImage);
        background.setLayout(new BorderLayout());

        // Tombol "Start"
        JButton enterButton = new JButton("Start");
        enterButton.setFont(new Font("Arial", Font.BOLD, 16));
        enterButton.setPreferredSize(new Dimension(100, 40));
        enterButton.addActionListener(_ -> {
            dispose();
            notifyStartListeners();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(enterButton);

        background.add(buttonPanel, BorderLayout.SOUTH);
        add(background);

        setVisible(true);
    }

    public void addStartListener(Runnable listener) {
        onStartListeners.add(listener);
    }

    private void notifyStartListeners() {
        onStartListeners.forEach(Runnable::run);
    }
}