import game.GamePanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;

class StartButtonAction extends MenuButtonAction {
    ImageIcon bulbaF;
    ImageIcon charmenderF;
    ImageIcon squirtleF;

    public StartButtonAction(Frame frameApp) {
        super(frameApp);
    }

    @Override
    public void execute() {
        frame.getContentPane().removeAll();

        JPanel backgroundPanel = createBackgroundPanel("src/Asset/Bg2.jpg");
        backgroundPanel.setLayout(new BorderLayout());

        loadCustomFont("src/Asset/Pixellari.ttf");

        bulbaF = loadPokemon("/Asset/Pokemon/Bulba.png", 200);
        charmenderF = loadPokemon("/Asset/Pokemon/charmender.png", 200);
        squirtleF = loadPokemon("/Asset/Pokemon/squirtle.png", 200);

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
        ChatBox chatBox = createChatBox("", 20, 650, 200, 30, 40);
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
        JLabel bulba = new JLabel(bulbaF);
        bulba.setPreferredSize(new Dimension(100, 100));
        JLabel charmender = new JLabel(charmenderF);
        charmender.setPreferredSize(new Dimension(100, 100));
        JLabel squirtle = new JLabel(squirtleF);
        squirtle.setPreferredSize(new Dimension(100, 100));

        pokemonPanel.add(bulba);
        pokemonPanel.add(charmender);
        pokemonPanel.add(squirtle);

        bulba.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bulba.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                bulba.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0, 255, 0, 180)));
            }
        });

        charmender.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        charmender.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                charmender.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0, 255, 0, 180)));
            }
        });

        squirtle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        squirtle.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                squirtle.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0, 255, 0, 180)));
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

}
