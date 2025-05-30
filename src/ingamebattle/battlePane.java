package ingamebattle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import logic.*;
import tile.ChatBox;

public class battlePane {
    private JPanel panel;
    private Battle battle;
    private Font customFont;
    private JLabel playerPokemonLabel, enemyPokemonLabel;
    private JLabel playerHPLabel, enemyHPLabel;
    private JButton[] moveButtons;
    private JButton[] actionButtons;
    private Pokemon playerPokemon, enemyPokemon;
    private BufferedImage backgroundImage;
    private ChatBox chatBox;
    private JPanel movePanel;
    private JPanel chatPanel;
    private StringBuilder logText = new StringBuilder();

    public battlePane(Pokemon playerPokemon, Pokemon enemyPokemon) {
        this.playerPokemon = playerPokemon;
        this.enemyPokemon = enemyPokemon;

        panel = createBackgroundPanel("src/Asset/battleBG.png");
        panel.setPreferredSize(new Dimension(1080, 607));
        panel.setLayout(null);

        battle = new Battle(playerPokemon, enemyPokemon);
        loadCustomFont("src/Asset/Pixellari.ttf");

        initBattleUI();
    }

    private void initBattleUI() {
        playerPokemonLabel = new JLabel(loadPokemon(playerPokemon.getImagePathB(), 270));
        playerPokemonLabel.setBounds(100, 250, 150, 150);
        panel.add(playerPokemonLabel);

        enemyPokemonLabel = new JLabel(loadPokemon(enemyPokemon.getImagePathF(), 150));
        enemyPokemonLabel.setBounds(750, 100, 150, 150);
        panel.add(enemyPokemonLabel);

        playerHPLabel = new JLabel();
        playerHPLabel.setBounds(100, 200, 300, 30);
        playerHPLabel.setFont(customFont.deriveFont(18f));
        playerHPLabel.setForeground(Color.WHITE);
        panel.add(playerHPLabel);

        enemyHPLabel = new JLabel();
        enemyHPLabel.setBounds(750, 70, 300, 30);
        enemyHPLabel.setFont(customFont.deriveFont(18f));
        enemyHPLabel.setForeground(Color.WHITE);
        panel.add(enemyHPLabel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBounds(0, 370, 1070, 180);
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BorderLayout());
        panel.add(bottomPanel);

        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setOpaque(true);
        chatPanel.setPreferredSize(new Dimension(880, 180));

        chatBox = ChatBox.createChatBox("src/Asset/textBox.png", "", 20, 880, 175, 30, 30);

        String fullText = "your TURN!!";
        int delay = 50;
        Timer typingTimer = new Timer(delay, null);
        final int[] charIndex = {0};
        typingTimer.addActionListener(e -> {
            if (charIndex[0] < fullText.length()) {
                chatBox.textArea.setText(chatBox.textArea.getText() + fullText.charAt(charIndex[0]));
                charIndex[0]++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        typingTimer.start();

        chatPanel.add(chatBox.label, BorderLayout.WEST);
        bottomPanel.add(chatPanel, BorderLayout.WEST);

        movePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        movePanel.setOpaque(false);
        movePanel.setPreferredSize(new Dimension(200, 100));
        movePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        bottomPanel.add(movePanel, BorderLayout.CENTER);

        JPanel actionButtonsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        actionButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 30));
        actionButtonsPanel.setOpaque(false);

        actionButtons = new JButton[4];
        String[] buttonNames = {"Battle", "Pokemon", "Bag", "Run"};

        actionButtons[0] = new JButton(buttonNames[0]);
        actionButtons[0].setFont(customFont.deriveFont(18f));
        actionButtons[0].setPreferredSize(new Dimension(150, 40));
        actionButtons[0].addActionListener(e -> {
            showMoveButtons();
        });
        actionButtonsPanel.add(actionButtons[0]);

        for (int i = 1; i < buttonNames.length; i++) {
            actionButtons[i] = new JButton(buttonNames[i]);
            actionButtons[i].setFont(customFont.deriveFont(16f));
            actionButtons[i].setPreferredSize(new Dimension(150, 40));
            actionButtons[i].addActionListener(e -> {
                chatBox.textArea.setText("You selected " + ((JButton)e.getSource()).getText());
            });
            actionButtonsPanel.add(actionButtons[i]);
        }

        bottomPanel.add(actionButtonsPanel, BorderLayout.EAST);
        updateHP();
    }

    private void showMoveButtons() {
        movePanel.removeAll();
        moveButtons = new JButton[playerPokemon.getMoves().size()];
        for (int i = 0; i < playerPokemon.getMoves().size(); i++) {
            Move move = playerPokemon.getMoves().get(i);
            JButton moveButton = new JButton(move.getName());
            moveButton.setFont(customFont.deriveFont(12f));
            moveButton.setPreferredSize(new Dimension(100, 40));
            moveButton.addActionListener(e -> {
                updateHP();
                chatBox.textArea.setText("You used " + move.getName() + "!");
                hideMoveButtons();
            });
            movePanel.add(moveButton);
        }

        chatPanel.setPreferredSize(new Dimension(660, 175));
        chatBox.resize(660, 175, 30, 30);

        chatPanel.revalidate();
        chatPanel.repaint();
        chatBox.label.revalidate();
        chatBox.label.repaint();

        panel.revalidate();
        panel.repaint();
    }


    private void hideMoveButtons() {
        movePanel.removeAll();

        chatPanel.setPreferredSize(new Dimension(880, 175));
        chatBox.resize(880, 175, 30, 30);


        chatPanel.revalidate();
        chatPanel.repaint();
        chatBox.label.revalidate();
        chatBox.label.repaint();

        panel.revalidate();
        panel.repaint();
    }

    private void updateHP() {
        playerHPLabel.setText(playerPokemon.getName() + " HP: " + playerPokemon.getHealth() + "/" + playerPokemon.getMaxHealth());
        enemyHPLabel.setText(enemyPokemon.getName() + " HP: " + enemyPokemon.getHealth() + "/" + enemyPokemon.getMaxHealth());
    }

    public JPanel getPanel() {
        return panel;
    }

    private ImageIcon loadPokemon(String path, int scale) {
        try {
            ImageIcon rawIcon = new ImageIcon(getClass().getResource(path));
            Image scaledImage = rawIcon.getImage().getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.out.println("Error loading image: " + path);
            return null;
        }
    }

    private void loadCustomFont(String fontPath) {
        try {
            File fontFile = new File(fontPath);
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 18);
        }
    }

    private JPanel createBackgroundPanel(String imagePath) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    backgroundImage = ImageIO.read(new File(imagePath));
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                } catch (IOException e) {
                    g.setColor(Color.RED);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
    }
}
