package entity;

import game.GamePanel;
import game.KeyHandler;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;


    public Player(GamePanel gp, KeyHandler keyH) { 

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8,16, 32, 32);

        setDefaultValues();
        getPlayerImage();

    }

    public void setDefaultValues() {

        worldX = gp.tileSize * 16;
        worldY = gp.tileSize * 16;
        speed = 4;
        direction = "down";

    }

    public void getPlayerImage() {

        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/Asset/player/up.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Asset/player/up1.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Asset/player/down.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Asset/player/down1.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Asset/player/left.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Asset/player/left1.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Asset/player/right.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Asset/player/right1.png"));

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void update() {

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {

           if (keyH.upPressed == true) {
            direction = "up";
            

        } else if (keyH.downPressed == true) {
            direction = "down";
            

        } else if (keyH.leftPressed == true) {
            direction = "left";
            

        } else if (keyH.rightPressed == true) {
            direction = "right";
           
        }

        collisionOn = false;
        gp.solidCheck.checkTile(this);

        if (collisionOn == false) { 

            switch (direction) {
                case "up": worldY -= speed;
                    break;
                case "down": worldY += speed;
                    break;
                case "left": worldX -= speed;
                    break;
                case "right": worldX += speed;
                    break;
                
                
            }
        }

        spriteCounter++;
        if(spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            }
            else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
            } 
        }
        
    }
    
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                } else if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } else if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                } else if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                } else if (spriteNum == 2) {
                    image = right2;
                }
                break;
            
        }

        int x = screenX;
        int y = screenY;

        if (screenX > worldX) {
            x = worldX;
        }
        if (screenY > worldY) {
            y = worldY;
        }
        int rightOffset = gp.screenWidth - screenX;
            if (rightOffset > gp.worldWidth - worldX) {
                x = gp.screenWidth - (gp.worldWidth - worldX);

            }

            int bottomOffset = gp.screenHeight - screenY;
            if (bottomOffset > gp.worldHeight - worldY) {
                y = gp.screenHeight - (gp.worldHeight - worldY);
            }
        g2.drawImage(image, x, y,gp.tileSize, gp.tileSize, null);
    }
}
