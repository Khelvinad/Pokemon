package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean interactPressed = false;
    private GamePanel gp;

    // Konstruktor ini sudah ada di kode Anda, pastikan masih ada
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }




    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Menggunakan getter untuk keamanan
        if (gp.getGameState() == GamePanel.GameState.OVERWORLD) {
            handleOverworldKeys(code);
        } else if (gp.getGameState() == GamePanel.GameState.MENU) {
            handleMenuStateKeys(code);
        }
    }

    private void handleOverworldKeys(int code) {
        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_F) interactPressed = true;

        if (code == KeyEvent.VK_M) {
            gp.setGameState(GamePanel.GameState.MENU);
        }
    }

    private void handleMenuStateKeys(int code) {
        if (code == KeyEvent.VK_M) {
            if (gp.ui.isAtMainMenu()) {
                // =================================================================
                // PERBAIKAN: Menggunakan GameState.OVERWORLD
                // =================================================================
                gp.setGameState(GamePanel.GameState.OVERWORLD);
            } else {
                gp.ui.backToMainMenu();
            }
        }

        if (gp.ui.isAtMainMenu()) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 5;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 5) gp.ui.commandNum = 0;
            }
        }

        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_F) {
            gp.ui.enterPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_F) interactPressed = false;
    }
}