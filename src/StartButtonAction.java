import game.GamePanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
import logic.Pokedex;
import logic.Pokemon;
import logic.Type;
import logic.Move;
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

    public StartButtonAction(Frame frameApp) {
        super(frameApp);

        pokemons[0] = Pokedex.getPokemonData("Bulbasaur");
        pokemons[1] = Pokedex.getPokemonData("Charmander");
        pokemons[2] = Pokedex.getPokemonData("Squirtle");

        ImageIcon tempBulbaGIF = null;
        try {
            java.net.URL url = getClass().getResource("/Asset/Pokemon/bulbaGIF.gif");
            tempBulbaGIF = new ImageIcon(url);
        } catch (Exception e) { System.err.println(e.getMessage());}
        this.bulbasaurGIF = tempBulbaGIF;

        ImageIcon tempCharmenderGIF = null;
        try {
            java.net.URL url = getClass().getResource("/Asset/Pokemon/charmenderGIF.gif");
            tempCharmenderGIF = new ImageIcon(url);
        } catch (Exception e) { System.err.println(e.getMessage());}
        this.charmanderGIF = tempCharmenderGIF;
        
        ImageIcon tempSquirtleGIF = null;
        try {
            java.net.URL url = getClass().getResource("/Asset/Pokemon/squirtleGIF.gif");
            tempSquirtleGIF = new ImageIcon(url);
        } catch (Exception e) { System.err.println(e.getMessage());}
        this.squirtleGIF = tempSquirtleGIF;

        this.bulbasaurPNG = loadPokemon("/Asset/Pokemon/bulba.png", 200);
        this.charmanderPNG = loadPokemon("/Asset/Pokemon/charmender.png", 200);
        this.squirtlePNG = loadPokemon("/Asset/Pokemon/squirtle.png", 200);

        loadCustomFont("src/Asset/Pixellari.ttf");
    }

    @Override
    public void execute() {
        frame.getContentPane().removeAll();

        JPanel backgroundPanel = createBackgroundPanel("src/Asset/Bg2.jpg");
        backgroundPanel.setLayout(new BorderLayout());

        JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JButton backButton = createBackButton();
        backButton.setFont(customFont.deriveFont(Font.PLAIN, 25f));
        backButton.setPreferredSize(new Dimension(150, 50));

        JButton conButton = new JButton("Continue");
        conButton.setFont(customFont.deriveFont(Font.BOLD, 25f));
        conButton.setPreferredSize(new Dimension(150, 50));

        topPanel.add(conButton, BorderLayout.EAST);
        topPanel.add(backButton, BorderLayout.WEST);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        JPanel chatPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        chatPanel.setOpaque(false);
        ChatBox chatBox = ChatBox.createChatBox("/Asset/chatBox.png","", 20, 650, 200, 30, 40);
        chatBox.getLabel().setOpaque(true);
        chatBox.getLabel().setBackground(Color.WHITE);
        chatPanel.add(chatBox.getLabel());
        chatBox.getTextArea().setForeground(Color.BLACK);
        String fullText = "Choose your POKETHOL";
        typingEffect(chatBox.getTextArea(), fullText, 30);

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
            }
        });


        conButton.addActionListener(e -> {
            if (selectedPokemon == null) {
                chatBox.getTextArea().setText("Please select a Pokemon first!");
                return;
            }

            frame.getContentPane().removeAll();
            GamePanel gamePanel = new GamePanel(); 
            
            frame.add(gamePanel);
            frame.pack();

            frame.setContentPane(gamePanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            gamePanel.startGameThread();
            gamePanel.requestFocusInWindow();

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

    private ImageIcon loadPokemon(String path, int scale) {
        java.net.URL imgUrl = getClass().getResource(path);
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


}