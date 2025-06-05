package entity;

import game.GamePanel;
import game.KeyHandler;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import logic.*;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;
    private String name = "Player"; // Default name
    private Inventory inventory;
    private Pokemon activePokemon;

    public final int screenX;
    public final int screenY;

    // Constructor used by GamePanel
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
        this.solidArea = new Rectangle(8, 16, 32, 32); // Solid area relative to player's top-left
        this.inventory = new Inventory(); // Each player gets a new inventory

        setDefaultValues(); // gp is available, so tileSizes will be correct
        getPlayerImage();
    }

    // Constructor for placeholder or data-holding player, NOT directly for GamePanel game world
    // This constructor might be less used if StartButtonAction configures GamePanel's player.
    public Player(String name, Inventory inventory, Pokemon activePokemon) {
        this.name = name;
        this.inventory = inventory;
        this.activePokemon = activePokemon;
        
        this.gp = null; // GamePanel context is not available here
        this.keyH = null;

        // screenX and screenY are final. They should ideally be set with GamePanel context.
        // If this Player instance is NOT the one used in GamePanel, these are just placeholders.
        this.screenX = 0; // Placeholder
        this.screenY = 0; // Placeholder
        
        this.solidArea = new Rectangle(8, 16, 32, 32);
        
        // Set default world positions that do not rely on gp.tileSize
        // These would be overridden if this Player's data is transferred to GamePanel's player.
        this.worldX = 16 * 38; // Example starting X (tile 16, assuming default 38px tile size)
        this.worldY = 16 * 38; // Example starting Y
        this.speed = 4;
        this.direction = "down";

        getPlayerImage();
    }

    public void setPlayerName(String name) { this.name = name; }
    public String getPlayerName() { return this.name; }

    public Inventory getInventory() {return inventory;}
    public void setInventory(Inventory inventory) {this.inventory = inventory;}

    public Pokemon getActivePokemon() { return activePokemon; }
    public void setActivePokemon(Pokemon activePokemon) {this.activePokemon = activePokemon;}
    
    public List<Pokemon> getPokemonParty() {
        return this.inventory != null ? this.inventory.getPokemons() : null;
    }

    public void setDefaultValues() {
        if (gp != null) { // Use GamePanel context if available
            worldX = gp.tileSize * 16; // Default starting col
            worldY = gp.tileSize * 16; // Default starting row
        } else {
            // Fallback if gp is not set (e.g. during initial data-only setup)
            // Assuming a common tile size if gp is not available (e.g. 19 original * 2 scale = 38)
            worldX = 16 * (19 * 2); 
            worldY = 16 * (19 * 2);
        }
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = loadImage("/Asset/player/up.png");
            up2 = loadImage("/Asset/player/up1.png");
            down1 = loadImage("/Asset/player/down.png");
            down2 = loadImage("/Asset/player/down1.png");
            left1 = loadImage("/Asset/player/left.png");
            left2 = loadImage("/Asset/player/left1.png");
            right1 = loadImage("/Asset/player/right.png");
            right2 = loadImage("/Asset/player/right1.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(path);
        if (stream == null) {
            throw new IOException("Cannot find resource: " + path);
        }
        return ImageIO.read(stream);
    }

    public void update() {
        if (keyH == null || gp == null) return; // Cannot update without KeyHandler or GamePanel

        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            collisionOn = false;
            gp.solidCheck.checkTile(this);

            if (!collisionOn) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            spriteCounter++;
            if(spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        if (gp == null) return; // Cannot draw without GamePanel context

        BufferedImage image = null;
        switch (direction) {
            case "up": image = (spriteNum == 1) ? up1 : up2; break;
            case "down": image = (spriteNum == 1) ? down1 : down2; break;
            case "left": image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }

        // Calculate screen position based on world position and camera
        int currentScreenX = screenX;
        int currentScreenY = screenY;

        // If player is near edge of world, camera stops scrolling
        if (worldX < screenX) {
            currentScreenX = worldX;
        }
        if (worldY < screenY) {
            currentScreenY = worldY;
        }
        int rightOffset = gp.screenWidth - screenX;
        if (rightOffset > gp.worldWidth - worldX) {
            currentScreenX = gp.screenWidth - (gp.worldWidth - worldX);
        }
        int bottomOffset = gp.screenHeight - screenY;
        if (bottomOffset > gp.worldHeight - worldY) {
            currentScreenY = gp.screenHeight - (gp.worldHeight - worldY);
        }
        
        if (image != null) {
            g2.drawImage(image, currentScreenX, currentScreenY, gp.tileSize, gp.tileSize, null);    
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(currentScreenX, currentScreenY, gp.tileSize, gp.tileSize);
        }
    }
}