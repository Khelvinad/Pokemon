import java.awt.*;
import javax.swing.*;

class StartButtonAction extends MenuButtonAction {
    ImageIcon bulbaF;
    ImageIcon charmender;
    ImageIcon squirtle;

    public StartButtonAction(Frame frameApp) {
        super(frameApp);
    }

    @Override
    public void execute() {
        frame.getContentPane().removeAll();

        bulbaF = loadPokemon("/Asset/Pokemon/Bulba.png", 100);
        charmender = loadPokemon("/Asset/Pokemon/charmender.png", 50);
        squirtle = loadPokemon("/Asset/Pokemon/squirtle.png", 50);

        JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.setOpaque(true);
        startPanel.setBackground(Color.BLACK);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);

        JButton backButton = createBackButton();
        topPanel.add(backButton);

        JLabel chatBox = createChatBoxLabel(
            "Chose your POKETHOL", 20,
            700, 200, 50, 40
        );
        chatBox.setHorizontalAlignment(SwingConstants.CENTER);
        chatBox.setVerticalAlignment(SwingConstants.CENTER);
        centerPanel.add(chatBox);

        JLabel bulba = new JLabel(bulbaF);
        centerPanel.add(bulba);

        startPanel.add(topPanel, BorderLayout.NORTH);
        startPanel.add(centerPanel);

        frame.setContentPane(startPanel);
        frame.revalidate();
        frame.repaint();
    }

    private ImageIcon loadPokemon( String path, int scale){
        ImageIcon rawIcon = new ImageIcon(getClass().getResource(path));
        Image scaledImage = rawIcon.getImage().getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
