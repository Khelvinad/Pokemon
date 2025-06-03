package ingamebattle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import logic.*;
import tile.ChatBox;

public class battlePane {
    private JPanel panel;
    private Battle battle;
    private Font customFont;
    private JLabel playerPokemonLabel, enemyPokemonLabel;

    private JLabel playerNameLabel;
    private JProgressBar playerHPBar;
    private JLabel playerHPTextLabel;

    private JLabel enemyNameLabel;
    private JProgressBar enemyHPBar;
    private JLabel enemyHPTextLabel;

    private JButton[] moveButtons;
    private JButton[] actionButtons;
    private Pokemon playerPokemon, enemyPokemon;
    private Inventory playerInventory; 
    private BufferedImage backgroundImage;
    private ChatBox chatBox;
    private JPanel movePanel;
    private JPanel chatPanel;
    private StringBuilder logText = new StringBuilder();
    private Timer typingTimer;
    private int charIndex;
    private int Turn_Delay = 1500;

    public battlePane(Pokemon playerPokemon, Pokemon enemyPokemon, Inventory playerInventory) {
        this.playerPokemon = playerPokemon;
        this.enemyPokemon = enemyPokemon;
        this.playerInventory = playerInventory;

        panel = createBackgroundPanel("src/Asset/battleBG.png");
        panel.setPreferredSize(new Dimension(1080, 607));
        panel.setLayout(null);

        battle = new Battle(playerPokemon, enemyPokemon);
        loadCustomFont("src/Asset/Pixellari.ttf");

        initBattleUI();
        startTypingEffect("Your TURN!!");
    }
    
    private void startTypingEffect(String fullText) {
        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }
        chatBox.getTextArea().setText("");
        charIndex = 0;
        int delay = 5;

        typingTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (charIndex < fullText.length()) {
                    chatBox.getTextArea().setText(chatBox.getTextArea().getText() + fullText.charAt(charIndex));
                    charIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        typingTimer.start();
    }

    private void initBattleUI() {
        playerPokemonLabel = new JLabel(loadPokemon(playerPokemon.getImagePathB(), 270));
        playerPokemonLabel.setBounds(100, 250, 150, 150);
        panel.add(playerPokemonLabel);

        enemyPokemonLabel = new JLabel(loadPokemon(enemyPokemon.getImagePathF(), 150));
        enemyPokemonLabel.setBounds(750, 100, 150, 150);
        panel.add(enemyPokemonLabel);

        playerNameLabel = new JLabel(playerPokemon.getName());
        playerNameLabel.setFont(customFont.deriveFont(16f));
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setBounds(100, 200, 150, 20);
        panel.add(playerNameLabel);

        playerHPBar = new JProgressBar(0, playerPokemon.getMaxHealth());
        playerHPBar.setValue(playerPokemon.getHealth());
        playerHPBar.setBounds(100, 225, 150, 15);
        playerHPBar.setForeground(Color.GREEN);
        playerHPBar.setBackground(Color.DARK_GRAY);
        playerHPBar.setFocusable(false);
        playerHPBar.setBorderPainted(false);
        panel.add(playerHPBar);

        playerHPTextLabel = new JLabel(playerPokemon.getHealth() + "/" + playerPokemon.getMaxHealth());
        playerHPTextLabel.setFont(customFont.deriveFont(12f));
        playerHPTextLabel.setForeground(Color.WHITE);
        playerHPTextLabel.setBounds(260, 225, 100, 15);
        panel.add(playerHPTextLabel);

        enemyNameLabel = new JLabel(enemyPokemon.getName());
        enemyNameLabel.setFont(customFont.deriveFont(16f));
        enemyNameLabel.setForeground(Color.WHITE);
        enemyNameLabel.setBounds(750, 40, 150, 20);
        panel.add(enemyNameLabel);

        enemyHPBar = new JProgressBar(0, enemyPokemon.getMaxHealth());
        enemyHPBar.setValue(enemyPokemon.getHealth());
        enemyHPBar.setBounds(750, 65, 150, 15);
        enemyHPBar.setForeground(Color.GREEN);
        enemyHPBar.setBackground(Color.DARK_GRAY);
        enemyHPBar.setFocusable(false);
        enemyHPBar.setBorderPainted(false);
        panel.add(enemyHPBar);

        enemyHPTextLabel = new JLabel(enemyPokemon.getHealth() + "/" + enemyPokemon.getMaxHealth());
        enemyHPTextLabel.setFont(customFont.deriveFont(12f));
        enemyHPTextLabel.setForeground(Color.WHITE);
        enemyHPTextLabel.setBounds(910, 65, 100, 15);
        panel.add(enemyHPTextLabel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBounds(0, 370, 1070, 180);
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BorderLayout());
        panel.add(bottomPanel);

        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setOpaque(false);
        chatPanel.setPreferredSize(new Dimension(880, 180));

        chatBox = ChatBox.createChatBox("src/Asset/textBox.png", "", 20, 880, 175, 30, 30);
        chatPanel.add(chatBox.getLabel(), BorderLayout.WEST);
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

        //Battel Button
        actionButtons[0] = new JButton(buttonNames[0]);
        actionButtons[0].setFont(customFont.deriveFont(18f));
        actionButtons[0].setPreferredSize(new Dimension(150, 40));
        actionButtons[0].addActionListener(e -> {
            if (!battle.isBattleOver()) {
                showMoveButtons();
            }
        });
        actionButtonsPanel.add(actionButtons[0]);
        
        //Pokemon Button
        actionButtons[1] = new JButton(buttonNames[1]);
        actionButtons[1].setFont(customFont.deriveFont(16f));
        actionButtons[1].setPreferredSize(new Dimension(150, 40));
        actionButtons[1].addActionListener(e -> {
            startTypingEffect("Selected " + buttonNames[1] + ". POKETHOL KONTOL");
        });
        actionButtonsPanel.add(actionButtons[1]);

        //Bag Button
        actionButtons[2] = new JButton(buttonNames[2]);
        actionButtons[2].setFont(customFont.deriveFont(16f));
        actionButtons[2].setPreferredSize(new Dimension(150, 40));
        actionButtons[2].addActionListener(e -> {
            if (!battle.isBattleOver()) {
                showBagScreen();
            }
        });
        actionButtonsPanel.add(actionButtons[2]);
        
        //Run Button
        actionButtons[3] = new JButton(buttonNames[3]);
        actionButtons[3].setFont(customFont.deriveFont(16f));
        actionButtons[3].setPreferredSize(new Dimension(150, 40));
        actionButtons[3].addActionListener(e -> {
            System.exit(0);
        });
        actionButtonsPanel.add(actionButtons[3]);

        bottomPanel.add(actionButtonsPanel, BorderLayout.EAST);
        updateHP();
    }

    private void setBattleActionsEnabled(boolean enabled) {
        for (JButton btn : actionButtons) {
            btn.setEnabled(enabled);
        }
        if (!enabled && movePanel.getComponentCount() > 0) {
        for(Component comp : movePanel.getComponents()){
            if(comp instanceof JButton){
                ((JButton) comp).setEnabled(false);
            }
        }
    }
    }

    private void showMoveButtons() {
        movePanel.removeAll();
        movePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0)); 
        moveButtons = new JButton[playerPokemon.getMoves().size()];
        logText.setLength(0);
        startTypingEffect("Choose a move for " + playerPokemon.getName() + "!");

        for (int i = 0; i < playerPokemon.getMoves().size(); i++) {
            Move move = playerPokemon.getMoves().get(i);
            JButton moveButton = new JButton(move.getName());
            moveButton.setFont(customFont.deriveFont(12f));
            moveButton.setPreferredSize(new Dimension(100, 40));
            
            moveButton.addActionListener(e -> {
                if (battle.isBattleOver()) return;

                String playerAttackLog = battle.executePlayerTurn(move);
                logText.append(playerAttackLog).append("\n");
                updateHP();

                if (battle.isBattleOver()) {
                    logText.append(battle.getBattleResult()).append("\n");
                    startTypingEffect(logText.toString());
                    hideMoveButtons();
                    setBattleActionsEnabled(false);
                    return;
                }
                
                hideMoveButtons();
                setBattleActionsEnabled(false);
                Timer delayTimer = new Timer(Turn_Delay, (actionEvent) -> {
                    String enemyAttackLog = battle.executeEnemyTurn();
                    logText.append(enemyAttackLog).append("\n");
                    updateHP();

                    if (battle.isBattleOver()) {
                        logText.append(battle.getBattleResult()).append("\n");
                        setBattleActionsEnabled(false);
                    } else {
                        logText.append("Your TURN!!").append("\n");
                        setBattleActionsEnabled(true);
                    }
                    startTypingEffect(logText.toString());

                });
                delayTimer.setRepeats(false);
                delayTimer.start();
            });
            movePanel.add(moveButton);
        }

        chatPanel.setPreferredSize(new Dimension(660, 175));
        chatBox.resize(660, 175, 30, 30);

        chatPanel.revalidate();
        chatPanel.repaint();
        chatBox.getLabel().revalidate();
        chatBox.getLabel().repaint();

        panel.revalidate();
        panel.repaint();
    }

    private void hideMoveButtons() {
        movePanel.removeAll();

        chatPanel.setPreferredSize(new Dimension(880, 175));
        chatBox.resize(880, 175, 30, 30);
        
        movePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0)); 
        movePanel.revalidate();
        movePanel.repaint();

        chatPanel.revalidate();
        chatPanel.repaint();
        chatBox.getLabel().revalidate();
        chatBox.getLabel().repaint();

        panel.revalidate();
        panel.repaint();
    }
    
    private void showBagScreen() {
        logText.setLength(0);
        setBattleActionsEnabled(false); 

        movePanel.removeAll();
        movePanel.setLayout(new GridLayout(0, 1, 5, 5)); 

        List<String> itemNames = playerInventory.getItemNames();

        if (itemNames.isEmpty()) {
            startTypingEffect("Your bag is empty.");
            JButton backButtonEmpty = new JButton("Back");
            backButtonEmpty.setFont(customFont.deriveFont(12f));
            backButtonEmpty.setPreferredSize(new Dimension(150,35));
            backButtonEmpty.addActionListener(e -> {
                hideMoveButtons(); 
                setBattleActionsEnabled(true);
                startTypingEffect("Your TURN!!");
            });
            movePanel.add(backButtonEmpty);
        } else {
            startTypingEffect("Choose an item to use.");
            for (String itemName : itemNames) {
                int quantity = playerInventory.getItemQuantity(itemName);
                if (quantity > 0) {
                    JButton itemButton = new JButton(itemName + " x" + quantity);
                    itemButton.setFont(customFont.deriveFont(12f));
                    itemButton.setPreferredSize(new Dimension(150, 35)); 
                    itemButton.addActionListener(e -> {
                        String useResult = playerInventory.useItem(itemName, playerPokemon);
                        logText.append(useResult).append("\n");
                        updateHP();
                        startTypingEffect(logText.toString());

                        if (useResult.contains("full!") || useResult.contains("not available.") || useResult.contains("does not exist")) {
                            showBagScreen(); 
                        } else {
                            hideMoveButtons();
                            setBattleActionsEnabled(false);

                            if (battle.isBattleOver()) { 
                                logText.append(battle.getBattleResult()).append("\n");
                                startTypingEffect(logText.toString());
                                return; 
                            }

                            Timer enemyTurnTimer = new Timer(Turn_Delay, ae -> {
                                String enemyAttackLog = battle.executeEnemyTurn();
                                logText.append(enemyAttackLog).append("\n");
                                updateHP();

                                if (battle.isBattleOver()) {
                                    logText.append(battle.getBattleResult()).append("\n");
                                } else {
                                    logText.append("Your TURN!!").append("\n");
                                    setBattleActionsEnabled(true); 
                                }
                                startTypingEffect(logText.toString()); 
                            });
                            enemyTurnTimer.setRepeats(false);
                            enemyTurnTimer.start();
                            setBattleActionsEnabled(true);
                        }
                    });
                    movePanel.add(itemButton);
                }
            }
            JButton backButton = new JButton("Back");
            backButton.setFont(customFont.deriveFont(12f));
            backButton.setPreferredSize(new Dimension(150,35));
            backButton.addActionListener(e -> {
                hideMoveButtons(); 
                setBattleActionsEnabled(true);
                startTypingEffect("Your TURN!!");
            });
            movePanel.add(backButton);
        }

        chatPanel.setPreferredSize(new Dimension(660, 175)); 
        chatBox.resize(660, 175, 30, 30);

        movePanel.revalidate();
        movePanel.repaint();
        chatPanel.revalidate();
        chatPanel.repaint();
        panel.revalidate();
        panel.repaint();
    }

    private void updateHP() {
        playerNameLabel.setText(playerPokemon.getName());
        playerHPBar.setMaximum(playerPokemon.getMaxHealth());
        playerHPBar.setValue(playerPokemon.getHealth());
        playerHPTextLabel.setText(playerPokemon.getHealth() + "/" + playerPokemon.getMaxHealth());
        setHPBarColor(playerHPBar, playerPokemon.getHealth(), playerPokemon.getMaxHealth());

        enemyNameLabel.setText(enemyPokemon.getName());
        enemyHPBar.setMaximum(enemyPokemon.getMaxHealth());
        enemyHPBar.setValue(enemyPokemon.getHealth());
        enemyHPTextLabel.setText(enemyPokemon.getHealth() + "/" + enemyPokemon.getMaxHealth());
        setHPBarColor(enemyHPBar, enemyPokemon.getHealth(), enemyPokemon.getMaxHealth());
    }

    private void setHPBarColor(JProgressBar hpBar, int currentHP, int maxHP) {
        double percentage = (double) currentHP / maxHP;
        if (percentage > 0.5) {
            hpBar.setForeground(new Color(0, 200, 0));
        } else if (percentage > 0.2) {
            hpBar.setForeground(Color.ORANGE);
        } else {
            hpBar.setForeground(Color.RED);
        }
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
