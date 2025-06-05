package tile;

import game.GamePanel;
import logic.DataHandler;
import logic.Pokedex;
import logic.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_24;
    public int commandNum = 0;
    public boolean enterPressed = false;

    private enum MenuState {
        MAIN,
        PARTY_LIST,
        INVENTORY_LIST,
        POKEDEX_LIST,
        GACHA_SCREEN,
        SAVE_NOTIFICATION
    }
    private MenuState currentMenuState = MenuState.MAIN;
    private String notificationMessage = "";
    private Pokemon gachaPokemonResult = null;

    public UI(GamePanel gp) {
        this.gp = gp;
        arial_24 = new Font("Arial", Font.PLAIN, 24);
    }

    public void update() {
        if (!enterPressed) {
            return;
        }
        if (currentMenuState == MenuState.MAIN) {
            handleMainMenuSelection();
        }
        enterPressed = false;
    }

    private void handleMainMenuSelection() {
        switch (commandNum) {
            case 0: currentMenuState = MenuState.PARTY_LIST; break;
            case 1: currentMenuState = MenuState.INVENTORY_LIST; break;
            case 2: currentMenuState = MenuState.POKEDEX_LIST; break;
            case 3:
                currentMenuState = MenuState.GACHA_SCREEN;
                performGacha();
                break;
            case 4:
                // Menggunakan gp.currentMap yang sudah ada di GamePanel Anda
                DataHandler.saveGame(gp.player, gp.currentMap);
                notificationMessage = "Game Saved!";
                gachaPokemonResult = null;
                currentMenuState = MenuState.SAVE_NOTIFICATION;
                break;
            case 5:
                // =================================================================
                // PERBAIKAN: Menggunakan GameState.OVERWORLD sesuai definisi di GamePanel
                // =================================================================
                gp.setGameState(GamePanel.GameState.OVERWORLD);
                backToMainMenu();
                break;
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_24);
        g2.setColor(Color.WHITE);

        if (gp.getGameState() == GamePanel.GameState.MENU) {
            switch (currentMenuState) {
                case MAIN: drawMainMenu(); break;
                case PARTY_LIST: drawPartyScreen(); break;
                case INVENTORY_LIST: drawInventoryScreen(); break;
                case POKEDEX_LIST: drawPokedexScreen(); break;
                case GACHA_SCREEN: drawGachaResultScreen(); break;
                case SAVE_NOTIFICATION: drawNotificationScreen(); break;
            }
        }
    }

    public boolean isAtMainMenu() {
        return currentMenuState == MenuState.MAIN;
    }

    public void backToMainMenu() {
        this.currentMenuState = MenuState.MAIN;
        this.commandNum = 0;
        this.gachaPokemonResult = null;
        this.notificationMessage = "";
    }

    private void drawMainMenu() {
        int frameX = gp.tileSize * 2;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28f));
        int textX = frameX + 40;
        int textY = frameY + gp.tileSize;

        String[] menuOptions = {"Party", "Inventory", "Pokedex", "Gacha", "Save", "Back"};
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == 4) textY += gp.tileSize * 0.5;
            g2.drawString(menuOptions[i], textX, textY);
            if (commandNum == i) g2.drawString(">", textX - 25, textY);
            textY += gp.tileSize;
        }
    }

    private void drawPartyScreen() {
        drawListScreen("Your Pokemon Party", gp.player.getInventory().getPokemons().stream().map(p -> p.getName() + " (HP: " + p.getHealth() + "/" + p.getMaxHealth() + ")").toList());
    }

    private void drawInventoryScreen() {
        drawListScreen("Your Items", gp.player.getInventory().getItemNames().stream().map(name -> name + " x" + gp.player.getInventory().getItemQuantity(name)).toList());
    }

    private void drawPokedexScreen() {
        drawListScreen("Pokedex", Pokedex.getAllPokemonNames());
    }

    private void drawGachaResultScreen() {
        int frameX = gp.screenWidth / 2 - (gp.tileSize * 4);
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 8;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(g2.getFont().deriveFont(22f));
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        g2.drawString(notificationMessage, textX, textY);

        if (gachaPokemonResult != null && gachaPokemonResult.getImagePathF() != null) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(gachaPokemonResult.getImagePathF()));
                g2.drawImage(icon.getImage(), frameX + frameWidth / 2 - 75, textY + 20, 150, 150, null);
            } catch (Exception e) {
                g2.drawString("Image not found", frameX + frameWidth / 2 - 75, textY + 90);
            }
        }
        g2.drawString("Press M to go back", frameX + 20, frameY + frameHeight - 30);
    }

    private void drawNotificationScreen() {
        int frameX = gp.screenWidth / 2 - (gp.tileSize * 4);
        int frameY = gp.tileSize * 4;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 3;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(g2.getFont().deriveFont(22f));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(notificationMessage);
        int textX = frameX + (frameWidth - textWidth) / 2;
        int textY = frameY + (frameHeight - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(notificationMessage, textX, textY);
    }

    private void performGacha() {
        if (gp.player.getInventory().getPokemonCount() >= 6) {
            notificationMessage = "Party Pokemon sudah penuh!";
            gachaPokemonResult = null;
        } else {
            Pokemon p = gp.gachaSystem.pullSinglePokemon();
            gp.player.getInventory().addPokemon(p);
            notificationMessage = "Selamat! Anda mendapatkan:";
            gachaPokemonResult = p;
        }
    }

    private void drawListScreen(String title, List<String> items) {
        int frameX = gp.tileSize * 2;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 12;
        int frameHeight = gp.tileSize * 12;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28f));
        g2.drawString(title, frameX + 20, frameY + 40);
        g2.setFont(arial_24);

        int textX = frameX + 30;
        int textY = frameY + gp.tileSize + 40;
        for (String item : items) {
            g2.drawString(item, textX, textY);
            textY += 40;
        }
        g2.drawString("Press M to go back", frameX + 20, frameY + frameHeight - 30);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }
}