package ingamebattle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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
    private ActionListener onRunAttemptListener;
    private ActionListener onBattleCompleteListener; 

    private JLabel playerNameLabel;
    private JProgressBar playerHPBar;
    private JLabel playerHPTextLabel;

    private JLabel enemyNameLabel;
    private JProgressBar enemyHPBar;
    private JLabel enemyHPTextLabel;

    private JButton[] actionButtons; 
    private Pokemon playerPokemon, enemyPokemon;
    private List<Pokemon> pokemonParty;
    private Inventory playerInventory;
    private BufferedImage backgroundImage;
    private ChatBox chatBox;
    private JPanel movePanel; 
    private JPanel chatPanel; 
    private StringBuilder logText = new StringBuilder();
    private Timer typingTimer;
    private int charIndex;
    private int Turn_Delay = 1500; 

    public battlePane(Pokemon playerPokemon, Pokemon enemyPokemon, Inventory playerInventory, ActionListener runListener, ActionListener battleCompleteListener) {
        this.playerPokemon = playerPokemon;
        this.enemyPokemon = enemyPokemon;
        this.playerInventory = playerInventory;
        this.pokemonParty = playerInventory.getPokemons();
        this.onRunAttemptListener = runListener;
        this.onBattleCompleteListener = battleCompleteListener;

        panel = createBackgroundPanel("/Asset/battleBG.png");
        panel.setPreferredSize(new Dimension(1080, 607));
        panel.setLayout(null);

        battle = new Battle(this.playerPokemon, this.enemyPokemon);
        loadCustomFont("/Asset/Pixellari.ttf");

        initBattleUI(); 
        startTypingEffect("Battle starts! Your TURN!!");
    }

    private void startTypingEffect(String fullText) {
        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }
        if (chatBox != null && chatBox.getTextArea() != null) {
            chatBox.getTextArea().setText("");
        } else {
            System.err.println("ChatBox or its TextArea is null in startTypingEffect.");
            return;
        }
        charIndex = 0;
        int delay = 25;

        typingTimer = new Timer(delay, e -> {
            if (chatBox != null && chatBox.getTextArea() != null && charIndex < fullText.length()) {
                chatBox.getTextArea().append(String.valueOf(fullText.charAt(charIndex)));
                charIndex++;
            } else {
                ((Timer) e.getSource()).stop();
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

        chatBox = ChatBox.createChatBox("/Asset/textBox.png", "", 20, 880, 175, 30, 30);
        if (chatBox != null && chatBox.getLabel() != null) {
            chatPanel.add(chatBox.getLabel(), BorderLayout.WEST); 
        }
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

        for(int i = 0; i < 4; i++) {
            actionButtons[i] = new JButton(buttonNames[i]);
            actionButtons[i].setFont(customFont.deriveFont(i == 0 ? 18f : 16f)); 
            actionButtons[i].setPreferredSize(new Dimension(150, 40));
            final int actionIndex = i;
            actionButtons[i].addActionListener(e -> handleMainAction(actionIndex));
            actionButtonsPanel.add(actionButtons[i]);
        }
        bottomPanel.add(actionButtonsPanel, BorderLayout.EAST); 

        updateHP(); 
    }

    private void handleMainAction(int actionIndex) {
        if (battle.isBattleOver()) {
            startTypingEffect(battle.getBattleResult() + "\nThe battle has ended.");
            return;
        }

        switch (actionIndex) {
            case 0: 
                showMoveButtons();
                break;
            case 1: 
                showPokemonSwitchScreen();
                break;
            case 2: 
                showBagScreen();
                break;
            case 3: 
                System.out.println("Attempting to run...");
                if (onRunAttemptListener != null) {
                    setBattleActionsEnabled(false); 
                    onRunAttemptListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "runAttempted"));
                } else {
                    startTypingEffect("You can't run from this battle!");
                }
                break;
        }
    }

    private void setBattleActionsEnabled(boolean enabled) {
        for (JButton btn : actionButtons) { 
            if (btn != null) btn.setEnabled(enabled);
        }
        if (!enabled) { 
            for(Component comp : movePanel.getComponents()){
                if(comp instanceof JButton){
                    ((JButton) comp).setEnabled(false);
                }
            }
        }
    }

    private void showMoveButtons() {
        setBattleActionsEnabled(false); 
        movePanel.removeAll(); 
        movePanel.setLayout(new GridLayout(2, 2, 5, 5)); 
        
        chatPanel.setPreferredSize(new Dimension(660, 175)); 
        if (chatBox != null) chatBox.resize(660, 175, 30, 30);

        startTypingEffect("Choose a move for " + playerPokemon.getName() + "!");

        List<Move> moves = playerPokemon.getMoves();
        int movesToShow = Math.min(moves.size(), 4);

        for (int i = 0; i < movesToShow; i++) {
            Move move = moves.get(i);
            JButton moveButton = new JButton(move.getName());
            moveButton.setFont(customFont.deriveFont(12f));

            moveButton.addActionListener(e -> {
                
                String playerAttackLog = battle.executePlayerTurn(move);
                logText.setLength(0); 
                logText.append(playerAttackLog).append("\n");
                updateHP();
                startTypingEffect(logText.toString()); 

                if (battle.isBattleOver()) {
                    logText.append(battle.getBattleResult()).append("\n");
                    startTypingEffect(logText.toString()); 

                    if (onBattleCompleteListener != null) {
                        Timer battleEndTimer = new Timer(Turn_Delay + 1000, ae -> 
                            onBattleCompleteListener.actionPerformed(
                                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "battleComplete_PlayerTurn")
                            )
                        );
                        battleEndTimer.setRepeats(false);
                        battleEndTimer.start();
                    }
                    return; 
                }

                Timer enemyTurnTimer = new Timer(Turn_Delay, (actionEvent) -> {
                    logText.setLength(0); 
                    String enemyAttackLog = battle.executeEnemyTurn();
                    logText.append(enemyAttackLog).append("\n");
                    updateHP();
                    
                    if (battle.isBattleOver()) {
                        logText.append(battle.getBattleResult()).append("\n");
                        startTypingEffect(logText.toString()); 

                        if (onBattleCompleteListener != null) {
                            Timer enemyBattleEndTimer = new Timer(Turn_Delay + 500, ae -> 
                                onBattleCompleteListener.actionPerformed(
                                    new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "battleComplete_EnemyTurn")
                                )
                            );
                            enemyBattleEndTimer.setRepeats(false);
                            enemyBattleEndTimer.start();
                        }
                    } else {
                        logText.append("Your TURN!!").append("\n");
                        startTypingEffect(logText.toString());
                        resetSubActionPanel(); 
                        setBattleActionsEnabled(true); 
                    }
                });
                enemyTurnTimer.setRepeats(false);
                enemyTurnTimer.start();
            });
            movePanel.add(moveButton);
        }

        if (movePanel.getComponentCount() < 4 || moves.isEmpty()) {
            JButton backFromMovesButton = new JButton("Back");
            backFromMovesButton.setFont(customFont.deriveFont(12f));
            backFromMovesButton.addActionListener(ev -> {
                resetSubActionPanel(); 
                startTypingEffect("Choose an action."); 
                setBattleActionsEnabled(true); 
            });
            movePanel.add(backFromMovesButton);
        }
        
        refreshDynamicPanels(); 
    }

    private void resetSubActionPanel() {
        movePanel.removeAll(); 
        movePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0)); 
        
        chatPanel.setPreferredSize(new Dimension(880, 180));
        if (chatBox != null) chatBox.resize(880, 175, 30, 30);

        refreshDynamicPanels();
    }

    private void showBagScreen() {
        setBattleActionsEnabled(false); 
        movePanel.removeAll();
        movePanel.setLayout(new GridLayout(0, 1, 5, 5)); 

        chatPanel.setPreferredSize(new Dimension(660, 175)); 
        if (chatBox != null) chatBox.resize(660, 175, 30, 30);

        List<String> itemNames = playerInventory.getItemNames();
        logText.setLength(0);

        if (itemNames.isEmpty() || playerInventory.getItemNames().stream().allMatch(name -> playerInventory.getItemQuantity(name) == 0)) {
            startTypingEffect("Your bag is empty.");
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
                        updateHP();
                        logText.setLength(0);
                        logText.append(useResult).append("\n");
                        startTypingEffect(logText.toString()); 

                        if (battle.isBattleOver()) {
                            logText.append(battle.getBattleResult()).append("\n");
                            startTypingEffect(logText.toString());
                            
                            if (onBattleCompleteListener != null) {
                                Timer itemBattleEndTimer = new Timer(Turn_Delay + 500, ae ->
                                    onBattleCompleteListener.actionPerformed(
                                        new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "battleComplete_ItemUse")
                                    )
                                );
                                itemBattleEndTimer.setRepeats(false);
                                itemBattleEndTimer.start();
                            }
                            return;
                        }
                        
                        Timer enemyTurnTimer = new Timer(Turn_Delay, (actionEvent) -> {
                            logText.setLength(0); 
                            String enemyAttackLog = battle.executeEnemyTurn();
                            logText.append(enemyAttackLog).append("\n");
                            updateHP();
                            
                            if (battle.isBattleOver()) {
                                logText.append(battle.getBattleResult()).append("\n");
                                startTypingEffect(logText.toString());

                                if (onBattleCompleteListener != null) {
                                    Timer enemyBattleEndTimer = new Timer(Turn_Delay + 500, ae -> 
                                        onBattleCompleteListener.actionPerformed(
                                            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "battleComplete_EnemyTurnAfterItem")
                                        )
                                    );
                                    enemyBattleEndTimer.setRepeats(false);
                                    enemyBattleEndTimer.start();
                                }
                            } else {
                                logText.append("Your TURN!!").append("\n");
                                startTypingEffect(logText.toString());
                                resetSubActionPanel();
                                setBattleActionsEnabled(true);
                            }
                        });
                        enemyTurnTimer.setRepeats(false);
                        enemyTurnTimer.start();
                    });
                    movePanel.add(itemButton);
                }
            }
        }
        
        JButton backFromBagButton = new JButton("Back");
        backFromBagButton.setFont(customFont.deriveFont(12f));
        backFromBagButton.setPreferredSize(new Dimension(150,35));
        backFromBagButton.addActionListener(e -> {
            resetSubActionPanel();
            startTypingEffect("Choose an action.");
            setBattleActionsEnabled(true);
        });
        movePanel.add(backFromBagButton);
        refreshDynamicPanels();
    }

    private void showPokemonSwitchScreen() {
        setBattleActionsEnabled(false); 
        movePanel.removeAll();
        movePanel.setLayout(new GridLayout(2, 2, 5, 5)); 

        chatPanel.setPreferredSize(new Dimension(660, 175)); 
        if (chatBox != null) chatBox.resize(660, 175, 30, 30);

        logText.setLength(0);
        int pokemonButtonsAdded = 0;
        final int MAX_POKEMON_BUTTONS_TO_SHOW = 3;

        boolean canSwitch = false;
        for (Pokemon p : pokemonParty) {
            if (p != this.playerPokemon && p.getHealth() > 0) {
                canSwitch = true;
                break;
            }
        }

        if (!canSwitch) {
            startTypingEffect(pokemonParty.size() <= 1 ? "You have no other Pokemon to switch to!" : "No healthy Pokemon available to switch!");
        } else {
            startTypingEffect("Choose a Pokemon to switch to.");
        }
        
        if (canSwitch) {
            for (Pokemon pokemonInParty : pokemonParty) {
                if (pokemonButtonsAdded >= MAX_POKEMON_BUTTONS_TO_SHOW) break;

                if (pokemonInParty != this.playerPokemon && pokemonInParty.getHealth() > 0) {
                    JButton pokemonButton = new JButton(
                        "<html><center>" + pokemonInParty.getName() +
                        "<br>(HP: " + pokemonInParty.getHealth() + "/" + pokemonInParty.getMaxHealth() +
                        ")</center></html>"
                    );
                    pokemonButton.setFont(customFont.deriveFont(11f));
                    pokemonButton.addActionListener(e -> {
                        Pokemon oldPokemon = this.playerPokemon;
                        this.playerPokemon = pokemonInParty;
                        battle.setPlayerPokemon(this.playerPokemon);

                        playerPokemonLabel.setIcon(loadPokemon(this.playerPokemon.getImagePathB(), 270));
                        updateHP();

                        logText.setLength(0);
                        logText.append(oldPokemon.getName()).append(" returns! Go, ").append(this.playerPokemon.getName()).append("!\n");
                        startTypingEffect(logText.toString()); 

                        Timer enemyTurnTimer = new Timer(Turn_Delay, (actionEvent) -> {
                            logText.setLength(0); 
                            String enemyAttackLog = battle.executeEnemyTurn();
                            logText.append(enemyAttackLog).append("\n");
                            updateHP();
                            
                            if (battle.isBattleOver()) {
                                logText.append(battle.getBattleResult()).append("\n");
                                startTypingEffect(logText.toString());

                                if (onBattleCompleteListener != null) {
                                    Timer enemyBattleEndTimer = new Timer(Turn_Delay + 500, ae -> 
                                        onBattleCompleteListener.actionPerformed(
                                            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "battleComplete_EnemyTurnAfterSwitch")
                                        )
                                    );
                                    enemyBattleEndTimer.setRepeats(false);
                                    enemyBattleEndTimer.start();
                                }
                            } else {
                                logText.append("Your TURN!!").append("\n");
                                startTypingEffect(logText.toString());
                                resetSubActionPanel();
                                setBattleActionsEnabled(true);
                            }
                        });
                        enemyTurnTimer.setRepeats(false);
                        enemyTurnTimer.start();
                    });
                    movePanel.add(pokemonButton);
                    pokemonButtonsAdded++;
                }
            }
        }

        JButton backFromPokemonButton = new JButton("Back");
        backFromPokemonButton.setFont(customFont.deriveFont(12f));
        backFromPokemonButton.addActionListener(e -> {
            resetSubActionPanel();
            startTypingEffect("Choose an action.");
            setBattleActionsEnabled(true);
        });
        movePanel.add(backFromPokemonButton);
        
        refreshDynamicPanels();
    }

    private void updateHP() {
        if (playerPokemon != null) {
            playerNameLabel.setText(playerPokemon.getName());
            playerHPBar.setMaximum(playerPokemon.getMaxHealth());
            playerHPBar.setValue(playerPokemon.getHealth());
            playerHPTextLabel.setText(playerPokemon.getHealth() + "/" + playerPokemon.getMaxHealth());
            setHPBarColor(playerHPBar, playerPokemon.getHealth(), playerPokemon.getMaxHealth());
        }
        if (enemyPokemon != null) {
            enemyNameLabel.setText(enemyPokemon.getName());
            enemyHPBar.setMaximum(enemyPokemon.getMaxHealth());
            enemyHPBar.setValue(enemyPokemon.getHealth());
            enemyHPTextLabel.setText(enemyPokemon.getHealth() + "/" + enemyPokemon.getMaxHealth());
            setHPBarColor(enemyHPBar, enemyPokemon.getHealth(), enemyPokemon.getMaxHealth());
        }
    }

    private void setHPBarColor(JProgressBar hpBar, int currentHP, int maxHP) {
        double percentage = (maxHP == 0) ? 0 : (double) currentHP / maxHP;
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
        if (path == null || path.trim().isEmpty()) {
            System.err.println("Error loading image: path is null or empty for loadPokemon. Scale: " + scale);
            return createPlaceholderIcon(scale);
        }
        try {
            InputStream imgStream = getClass().getResourceAsStream(path);
            if (imgStream == null) {
                System.err.println("Error loading image from classpath: " + path + " (Stream is null)");
                return createPlaceholderIcon(scale);
            }
            BufferedImage bufferedImage = ImageIO.read(imgStream);
            imgStream.close(); 
            if (bufferedImage == null) {
                System.err.println("Error loading image: ImageIO.read returned null for " + path);
                return createPlaceholderIcon(scale);
            }
            Image scaledImage = bufferedImage.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Exception in loadPokemon for path: " + path);
            e.printStackTrace();
            return createPlaceholderIcon(scale);
        }
    }

    private ImageIcon createPlaceholderIcon(int size) {
        BufferedImage placeholder = new BufferedImage(Math.max(1, size), Math.max(1, size), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, size, size);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, size/4)); 
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString("?", (size - fm.stringWidth("?")) / 2, (size - fm.getHeight()) / 2 + fm.getAscent());
        g2d.drawRect(0,0,size-1,size-1);
        g2d.dispose();
        return new ImageIcon(placeholder);
    }

    private void loadCustomFont(String fontPath) { 
        try {
            InputStream is = getClass().getResourceAsStream(fontPath);
            if (is == null) {
                System.err.println("Font not found in classpath: " + fontPath);
                customFont = new Font("Arial", Font.PLAIN, 18); 
                return;
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close(); 
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            System.err.println("Failed to load custom font: " + fontPath);
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 18); 
        }
    }

    private JPanel createBackgroundPanel(String imagePath) { 
        return new JPanel() {
            private BufferedImage panelBackgroundImage; 
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (panelBackgroundImage == null) {
                    try {
                        InputStream imgStream = getClass().getResourceAsStream(imagePath);
                        if (imgStream == null) {
                            System.err.println("Background image not found in classpath: " + imagePath);
                        } else {
                            panelBackgroundImage = ImageIO.read(imgStream);
                            imgStream.close(); 
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to load background image: " + imagePath);
                        e.printStackTrace();
                    }
                }
                if (panelBackgroundImage != null) {
                    g.drawImage(panelBackgroundImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(Color.DARK_GRAY); 
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
    }
    
    private void refreshDynamicPanels(){
        movePanel.revalidate();
        movePanel.repaint();
        chatPanel.revalidate();
        chatPanel.repaint();
        if (chatBox != null && chatBox.getLabel() != null) {
            chatBox.getLabel().revalidate();
            chatBox.getLabel().repaint();
        }
        panel.revalidate();
        panel.repaint();
    }
}