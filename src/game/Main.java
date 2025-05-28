package game;

import java.awt.Dimension;
import javax.swing.JFrame;
import starting.StartScreen;

public class Main {
    public static void main(String[] args) {
        // Tampilkan StartScreen
        StartScreen startScreen = new StartScreen();

        // Set listener untuk memulai game setelah tombol "Start" diklik
        startScreen.addStartListener(Main::startGame);
    }

    private static void startGame() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Pokemon Battle");
        window.setPreferredSize(new Dimension(1080, 607));

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
        gamePanel.requestFocusInWindow();
    }
}