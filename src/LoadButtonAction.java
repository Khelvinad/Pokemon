import java.awt.*;
import javax.swing.*;

class LoadButtonAction extends MenuButtonAction {
    public LoadButtonAction(Frame frameApp) {
        super(frameApp);
    }

    @Override
    public void execute() {
        JPanel loadPanel = new JPanel(new BorderLayout());
        loadPanel.setBackground(Color.LIGHT_GRAY);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        JButton backButton = createBackButton();
        topPanel.add(backButton);

        loadPanel.add(topPanel, BorderLayout.NORTH);

        frame.setContentPane(loadPanel);
        frame.revalidate();
        frame.repaint();
    }
}
