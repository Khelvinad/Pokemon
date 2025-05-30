import game.GamePanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
import logic.*;
import tile.ChatBox;

class StartButtonAction extends MenuButtonAction {
    ImageIcon bulbaF;
    ImageIcon charmenderF;
    ImageIcon squirtleF;
    private Pokemon[] pokemons = new Pokemon[3];

    public StartButtonAction(Frame frameApp) {
        super(frameApp);
        pokemons[0] = new Pokemon("Bulbasaur", Type.GRASS, 45, 49, 49);
        pokemons[0].addMove(new Move("Vine Whip", Type.GRASS, 45));
        pokemons[0].addMove(new Move("Tackle", Type.NORMAL, 40));
        pokemons[1] = new Pokemon("Charmander", Type.FIRE, 39, 52, 43);
        pokemons[1].addMove(new Move("Ember", Type.FIRE, 40));
        pokemons[1].addMove(new Move("Scratch", Type.NORMAL, 40));
        pokemons[2] = new Pokemon("Squirtle", Type.WATER, 44, 48, 65);
        pokemons[2].addMove(new Move("Water Gun", Type.WATER, 40));
        pokemons[2].addMove(new Move("Tackle", Type.NORMAL, 40));
        bulbaF = loadPokemon("/Asset/Pokemon/bulba.png", 200);
        charmenderF = loadPokemon("/Asset/Pokemon/charmender.png", 200);
        squirtleF = loadPokemon("/Asset/Pokemon/squirtle.png", 200);
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
        ChatBox chatBox = ChatBox.createChatBox("src/Asset/chatBox.png","", 20, 650, 200, 30, 40);
        chatPanel.add(chatBox.label);
        chatBox.label.setOpaque(true);
        chatBox.label.setBackground(Color.WHITE);
        chatBox.textArea.setForeground(Color.BLACK);
        String fullText = "Choose your POKETHOL";
        int delay = 70;

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

        JPanel pokemonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 150, 0));
        pokemonPanel.setOpaque(false);
        ImageIcon bulbaHalf = setImageOpacity(bulbaF, 0.65f);
        ImageIcon charHalf = setImageOpacity(charmenderF, 0.65f);
        ImageIcon squirtHalf = setImageOpacity(squirtleF, 0.65f);

        JLabel bulba = new JLabel(bulbaHalf);
        bulba.setPreferredSize(new Dimension(100, 100));
        JLabel charmender = new JLabel(charHalf);
        charmender.setPreferredSize(new Dimension(100, 100));
        JLabel squirtle = new JLabel(squirtHalf);
        squirtle.setPreferredSize(new Dimension(100, 100));

        pokemonPanel.add(bulba);
        pokemonPanel.add(charmender);
        pokemonPanel.add(squirtle);

        bulba.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bulba.setIcon(bulbaF);
                charmender.setIcon(charHalf);
                squirtle.setIcon(squirtHalf);
                
                bulba.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(255, 255, 204, 255)));
                charmender.setBorder(null);
                squirtle.setBorder(null);
            }
        });

        charmender.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                charmender.setIcon(charmenderF);
                bulba.setIcon(bulbaHalf);
                squirtle.setIcon(squirtHalf);
                
                charmender.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(255, 255, 204, 255)));
                bulba.setBorder(null);
                squirtle.setBorder(null);
            }
        });

        squirtle.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                squirtle.setIcon(squirtleF);
                bulba.setIcon(bulbaHalf);
                charmender.setIcon(charHalf);
                
                squirtle.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, new Color(255, 255, 204, 255)));
                bulba.setBorder(null);
                charmender.setBorder(null);
            }
        });


        conButton.addActionListener(e -> {
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
        ImageIcon rawIcon = new ImageIcon(getClass().getResource(path));
        Image scaledImage = rawIcon.getImage().getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private ImageIcon setImageOpacity(ImageIcon icon, float alpha) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(icon.getImage(), 0, 0, null);
        g2d.dispose();

        return new ImageIcon(bufferedImage);
    }


}
