import java.awt.*;
import javax.swing.*;

class StartButtonAction extends MenuButtonAction {
    public StartButtonAction(Frame frameApp) {
        super(frameApp);
    }

    @Override
    public void execute() {
        frame.getContentPane().removeAll();

        JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.setBackground(Color.CYAN);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        JButton backButton = createBackButton();
        topPanel.add(backButton);

        startPanel.add(topPanel, BorderLayout.NORTH);

        frame.setContentPane(startPanel);
        frame.revalidate();
        frame.repaint();
    }
}
