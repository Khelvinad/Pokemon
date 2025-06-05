import entity.Player;
import game.GamePanel;
import ingamebattle.battlePane;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
import logic.*;
import tile.ChatBox;

class StartButtonAction extends MenuButtonAction {
    final ImageIcon bulbasaurGIF;
    final ImageIcon charmanderGIF;
    final ImageIcon squirtleGIF;

    final ImageIcon bulbasaurPNG;
    final ImageIcon charmanderPNG;
    final ImageIcon squirtlePNG;
    
    private Timer typingTimer;
    private Pokemon[] pokemons = new Pokemon[3]; 
    private Pokemon selectedPokemon = null;    
    
    private Player gamePlayerEntity; 
    
    private String inputPlayerName; 

    private JPanel nameInputPanel;
    private JTextField nameTextField;
    private ChatBox nameInputChatBox;


    public StartButtonAction(Frame frameApp) {
        super(frameApp);

        pokemons[0] = Pokedex.getPokemonData("Bulbasaur");
        pokemons[1] = Pokedex.getPokemonData("Charmander");
        pokemons[2] = Pokedex.getPokemonData("Squirtle");

        ImageIcon tempBulbaGIF = null;
        try {
            java.net.URL url = getClass().getResource("/Asset/Pokemon/bulbaGIF.gif"); 
            if (url != null) tempBulbaGIF = new ImageIcon(url);
            else System.err.println("Error: bulbaGIF.gif not found at classpath root: /Asset/Pokemon/bulbaGIF.gif");
        } catch (Exception e) { System.err.println("Exception loading bulbaGIF: " + e.getMessage());}
        this.bulbasaurGIF = tempBulbaGIF; 

        ImageIcon tempCharmenderGIF = null;
        try {
            java.net.URL url = getClass().getResource("/Asset/Pokemon/charmenderGIF.gif"); 
            if (url != null) tempCharmenderGIF = new ImageIcon(url);
            else System.err.println("Error: charmenderGIF.gif not found at classpath root: /Asset/Pokemon/charmenderGIF.gif");
        } catch (Exception e) { System.err.println("Exception loading charmenderGIF: " +e.getMessage());}
        this.charmanderGIF = tempCharmenderGIF; 
        
        ImageIcon tempSquirtleGIF = null;
        try {
            java.net.URL url = getClass().getResource("/Asset/Pokemon/squirtleGIF.gif"); 
            if (url != null) tempSquirtleGIF = new ImageIcon(url);
            else System.err.println("Error: squirtleGIF.gif not found at classpath root: /Asset/Pokemon/squirtleGIF.gif");
        } catch (Exception e) { System.err.println("Exception loading squirtleGIF: " +e.getMessage());}
        this.squirtleGIF = tempSquirtleGIF; 

        this.bulbasaurPNG = loadPokemon("/Asset/Pokemon/bulba.png", 200); 
        this.charmanderPNG = loadPokemon("/Asset/Pokemon/charmender.png", 200); 
        this.squirtlePNG = loadPokemon("/Asset/Pokemon/squirtle.png", 200); 

        loadCustomFont("src/Asset/Pixellari.ttf"); 
    }

    @Override
    public void execute() {
        showNameInputScreen();
    }

    private void showNameInputScreen() {
        frame.getContentPane().removeAll();

        nameInputPanel = createBackgroundPanel("src/Asset/Bg2.jpg"); 
        nameInputPanel.setLayout(new BorderLayout());

        JPanel topNavPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topNavPanel.setOpaque(false);
        JButton backButton = createBackButton(); 
        styleButton(backButton, new Color(180, 100, 80));
        topNavPanel.add(backButton);
        nameInputPanel.add(topNavPanel, BorderLayout.NORTH);


        JPanel centerContentPanel = new JPanel();
        centerContentPanel.setOpaque(false);
        centerContentPanel.setLayout(new BoxLayout(centerContentPanel, BoxLayout.Y_AXIS));
        centerContentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));


        nameInputChatBox = ChatBox.createChatBox("/Asset/chatBox.png", "", 20, 600, 150, 30, 30); 
        styleChatBox(nameInputChatBox, Color.BLACK, new Color(253, 253, 243)); 
        typingEffect(nameInputChatBox.getTextArea(), "Welcome, Trainer! sinten namimu?", 30); 
        
        JPanel chatBoxWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        chatBoxWrapper.setOpaque(false);
        chatBoxWrapper.add(nameInputChatBox.getLabel());
        centerContentPanel.add(chatBoxWrapper);
        centerContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        nameTextField = new JTextField(20); 
        styleTextField(nameTextField);
        centerContentPanel.add(nameTextField);
        centerContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton confirmNameButton = new JButton("Confirm Name");
        styleButton(confirmNameButton, new Color(80, 130, 70)); 
        confirmNameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmNameButton.addActionListener(e -> {
            inputPlayerName = nameTextField.getText();
            if (inputPlayerName == null || inputPlayerName.trim().isEmpty()) {
                typingEffect(nameInputChatBox.getTextArea(), "Oops! Your name cannot be empty. Please try again.", 20);
                nameTextField.requestFocusInWindow();
                return;
            }
            inputPlayerName = inputPlayerName.trim();
            showPokemonSelectionScreen();
        });
        centerContentPanel.add(confirmNameButton);
        
        nameInputPanel.add(centerContentPanel, BorderLayout.CENTER);

        frame.setContentPane(nameInputPanel);
        frame.revalidate();
        frame.repaint();
        nameTextField.requestFocusInWindow();
    }

    private void showPokemonSelectionScreen() {
        frame.getContentPane().removeAll(); 

        JPanel backgroundPanel = createBackgroundPanel("src/Asset/Bg2.jpg"); 
        backgroundPanel.setLayout(new BorderLayout()); 

        JPanel startPanel = new JPanel(new BorderLayout()); 
        startPanel.setOpaque(false); 

        JPanel topPanel = new JPanel(new BorderLayout()); 
        topPanel.setOpaque(false); 

        JButton backToNameButton = new JButton("Back"); 
        styleButton(backToNameButton, new Color(180,100,80));
        backToNameButton.addActionListener(e -> showNameInputScreen()); 

        JButton conButton = new JButton("Continue"); 
        styleButton(conButton, new Color(80,130,70));


        topPanel.add(conButton, BorderLayout.EAST); 
        topPanel.add(backToNameButton, BorderLayout.WEST); 

        JPanel mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); 
        mainPanel.setOpaque(false); 

        JPanel chatPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        chatPanel.setOpaque(false); 
        ChatBox pokemonChatBox = ChatBox.createChatBox("/Asset/chatBox.png","", 20, 650, 200, 30, 40); 
        styleChatBox(pokemonChatBox, Color.BLACK, new Color(253, 253, 243)); 
        chatPanel.add(pokemonChatBox.getLabel()); 
        String fullText = "Alright, " + inputPlayerName + "! Now, choose your first POKETHOL!"; 
        typingEffect(pokemonChatBox.getTextArea(), fullText, 30); 

        JPanel pokemonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 150, 0)); 
        pokemonPanel.setOpaque(false); 

        final ImageIcon bulbaHalf = (bulbasaurPNG != null) ? setImageOpacity(bulbasaurPNG, 0.65f) : null; 
        final ImageIcon charHalf = (charmanderPNG != null) ? setImageOpacity(charmanderPNG, 0.65f) : null; 
        final ImageIcon squirtHalf = (squirtlePNG != null) ? setImageOpacity(squirtlePNG, 0.65f) : null; 
        
        JLabel bulba = new JLabel(bulbaHalf != null ? bulbaHalf : new ImageIcon()); 
        bulba.setPreferredSize(new Dimension(100, 110)); 
        JLabel charmender = new JLabel(charHalf != null ? charHalf : new ImageIcon()); 
        charmender.setPreferredSize(new Dimension(100, 110)); 
        JLabel squirtle = new JLabel(squirtHalf != null ? squirtHalf : new ImageIcon()); 
        squirtle.setPreferredSize(new Dimension(100, 110)); 

        pokemonPanel.add(bulba); 
        pokemonPanel.add(charmender); 
        pokemonPanel.add(squirtle); 

        bulba.addMouseListener(new java.awt.event.MouseAdapter() { 
            @Override
            public void mouseClicked(MouseEvent e) { 
                if (bulbasaurGIF != null) bulba.setIcon(bulbasaurGIF); 
                if (charHalf != null) charmender.setIcon(charHalf); else if (charmanderPNG != null) charmender.setIcon(charmanderPNG); 
                if (squirtHalf != null) squirtle.setIcon(squirtHalf); else if (squirtlePNG != null) squirtle.setIcon(squirtlePNG); 
                
                bulba.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(255, 255, 204, 255))); 
                charmender.setBorder(null); 
                squirtle.setBorder(null); 
                selectedPokemon = pokemons[0]; 

                if (selectedPokemon != null){ 
                    sb(pokemonChatBox); 
                }                
            }
        });

        charmender.addMouseListener(new java.awt.event.MouseAdapter() { 
            @Override
            public void mouseClicked(MouseEvent e) { 
                if (charmanderGIF != null) charmender.setIcon(charmanderGIF); 
                if (bulbaHalf != null) bulba.setIcon(bulbaHalf); else if (bulbasaurPNG != null) bulba.setIcon(bulbasaurPNG); 
                if (squirtHalf != null) squirtle.setIcon(squirtHalf); else if (squirtlePNG != null) squirtle.setIcon(squirtlePNG); 
                
                charmender.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(255, 255, 204, 255))); 
                bulba.setBorder(null); 
                squirtle.setBorder(null); 
                selectedPokemon = pokemons[1]; 
                if (selectedPokemon != null){ 
                    sb(pokemonChatBox); 
                }
            }
        });

        squirtle.addMouseListener(new java.awt.event.MouseAdapter() { 
            @Override
            public void mouseClicked(MouseEvent e) { 
                if (squirtleGIF != null) squirtle.setIcon(squirtleGIF); 
                if (bulbaHalf != null) bulba.setIcon(bulbaHalf); else if (bulbasaurPNG != null) bulba.setIcon(bulbasaurPNG); 
                if (charHalf != null) charmender.setIcon(charHalf); else if (charmanderPNG != null) charmender.setIcon(charmanderPNG); 
                
                squirtle.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(255, 255, 204, 255))); 
                bulba.setBorder(null); 
                charmender.setBorder(null); 
                selectedPokemon = pokemons[2]; 
                if (selectedPokemon != null){ 
                    sb(pokemonChatBox); 
                }
            }
        });

        conButton.addActionListener(e -> {
            if (selectedPokemon == null) {
                typingEffect(pokemonChatBox.getTextArea(), "Please select a Pokemon first, " + inputPlayerName + "!", 20);
                return;
            }

            // This player instance is for data collection
            Inventory startingInventory = new Inventory();
            startingInventory.addPokemon(this.selectedPokemon);
            // Add any initial items
            startingInventory.addItem(new Potion(20), 3); // Example
            startingInventory.addItem(new AttackBoost(5, 1), 2); // Example

            // Correct enemy Pokemon name if it was a typo
            Pokemon enemyPokemon;
            if (this.selectedPokemon.getName().equals("Bulbasaur")) {
                enemyPokemon = Pokedex.getPokemonData("Squirtle");
            } else if (this.selectedPokemon.getName().equals("Charmander")) {
                enemyPokemon = Pokedex.getPokemonData("Bulbasaur");
            } else { // Squirtle
                enemyPokemon = Pokedex.getPokemonData("Charmander"); // Was "Charmender"
            }

            // ActionListener for what happens AFTER the initial battle
            ActionListener afterInitialBattleHandler = event -> {
                // This is called when the battlePane signals completion (e.g., win, loss, or a specific run)
                // Now, transition to GamePanel
                frame.getContentPane().removeAll(); // Clear the battle pane

                GamePanel gamePanel = new GamePanel(); // GamePanel creates its OWN Player instance

                // Configure GamePanel's player with the collected data
                gamePanel.player.setPlayerName(this.inputPlayerName);
                gamePanel.player.setInventory(startingInventory); // The inventory with the chosen Pokemon and items
                gamePanel.player.setActivePokemon(this.selectedPokemon);
                // Player's worldX, worldY are set by its constructor via setDefaultValues using gp.tileSize

                DataHandler.saveGame(gamePanel.player, "map01.txt"); // Save the initial game state

                frame.add(gamePanel);
                frame.pack(); // Adjust frame to GamePanel's preferred size
                frame.setContentPane(gamePanel); // Set gamePanel as the new content
                frame.setLocationRelativeTo(null); // Re-center frame
                frame.setVisible(true);

                gamePanel.startGameThread();
                gamePanel.requestFocusInWindow(); // Crucial for keyboard input to GamePanel

                frame.revalidate();
                frame.repaint();
            };

            ActionListener runAttemptHandlerForInitialBattle = runEvent -> {
                System.out.println("Run attempted from initial battle. Returning to name input screen.");
                showNameInputScreen(); 
            };

            battlePane battleScreen = new battlePane(
                this.selectedPokemon, 
                enemyPokemon, 
                startingInventory, 
                runAttemptHandlerForInitialBattle,  
                afterInitialBattleHandler          
            );
            frame.getContentPane().removeAll(); 
            frame.setContentPane(battleScreen.getPanel()); 
            frame.revalidate();
            frame.repaint();
        });

        mainPanel.add(chatPanel); 
        mainPanel.add(pokemonPanel); 

        startPanel.add(topPanel, BorderLayout.NORTH); 
        startPanel.add(mainPanel, BorderLayout.CENTER); 
        backgroundPanel.add(startPanel); 

        frame.setContentPane(backgroundPanel); 
        frame.revalidate(); 
        frame.repaint(); 
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(customFont.deriveFont(Font.PLAIN, 22f));
        textField.setBackground(new Color(50, 50, 70)); 
        textField.setForeground(Color.WHITE); 
        textField.setCaretColor(Color.WHITE);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 80, 50), 2), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10) 
        ));
        textField.setMaximumSize(new Dimension(300, 50)); 
        textField.setPreferredSize(new Dimension(250, 45)); 
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(customFont.deriveFont(Font.BOLD, 20f));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2), 
            BorderFactory.createLineBorder(bgColor.brighter().brighter(), 2) 
        ));
        button.setPreferredSize(new Dimension(180, 50)); 
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
    
    private void styleChatBox(ChatBox chatBox, Color textColor, Color bgColor) {
        JLabel label = chatBox.getLabel();
        JTextArea textArea = chatBox.getTextArea();

        label.setOpaque(true); 
        label.setBackground(bgColor); 
        
        textArea.setForeground(textColor);
        textArea.setFont(customFont.deriveFont(Font.PLAIN, 18f)); 
        textArea.setOpaque(false); 
        
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, textArea);
        if (scrollPane != null) {
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
        }
    }


    private ImageIcon loadPokemon(String path, int scale) { 
        java.net.URL imgUrl = getClass().getResource(path);
        if (imgUrl == null) {
            System.err.println("Error loading image: " + path + " not found at classpath root.");
            return new ImageIcon(new BufferedImage(scale, scale, BufferedImage.TYPE_INT_ARGB)); 
        }
        ImageIcon rawIcon = new ImageIcon(imgUrl); 
        Image scaledImage = rawIcon.getImage().getScaledInstance(scale, scale, Image.SCALE_SMOOTH); 
        return new ImageIcon(scaledImage); 
    }

    private ImageIcon setImageOpacity(ImageIcon icon, float alpha) { 
        int w = icon.getIconWidth(); 
        int h = icon.getIconHeight(); 
        int validW = Math.max(1, w); 
        int validH = Math.max(1, h); 

        BufferedImage bufferedImage = new BufferedImage(validW, validH, BufferedImage.TYPE_INT_ARGB); 
        Graphics2D g2d = bufferedImage.createGraphics(); 

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); 
        g2d.drawImage(icon.getImage(), 0, 0, validW, validH, null); 
        g2d.dispose(); 

        return new ImageIcon(bufferedImage); 
    }

    private void typingEffect(JTextArea textArea, String fullText, int delay) { 
        if (typingTimer != null && typingTimer.isRunning()) { 
            typingTimer.stop(); 
        }
        textArea.setText(""); 
        final int[] charIndex = {0}; 

        if (delay <= 0) { 
            textArea.setText(fullText); 
            return;
        }

        typingTimer = new Timer(delay, new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                if (charIndex[0] < fullText.length()) { 
                    textArea.append(String.valueOf(fullText.charAt(charIndex[0]))); 
                    charIndex[0]++; 
                } else {
                    if (typingTimer != null) {  
                        typingTimer.stop(); 
                    }
                }
            }
        });
        typingTimer.start(); 
    }

    private ChatBox sb(ChatBox chatBox) { 
        StringBuilder sb = new StringBuilder(); 
        sb.append("You MILEH ").append(selectedPokemon.getName()).append("!\n\n"); 
        sb.append("Type: ").append(selectedPokemon.getType().toString()).append("\n"); 
        sb.append("Health: ").append(selectedPokemon.getHealth()).append("/").append(selectedPokemon.getMaxHealth()).append("\n"); 
        sb.append("Attack: ").append(selectedPokemon.getAttack()).append("\n"); 
        sb.append("Defense: ").append(selectedPokemon.getDefense()).append("\n"); 
        sb.append("Moves: "); 
        for (Move move : selectedPokemon.getMoves()) { 
            sb.append(move.getName()).append(" (").append(move.getType().toString()).append(") "); 
        }
        chatBox.getTextArea().setText(sb.toString()); 
        return chatBox; 
    }
}
