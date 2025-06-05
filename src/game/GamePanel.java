package game;

import entity.Player;
import ingamebattle.battlePane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import logic.Pokedex;
import logic.Pokemon;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 19;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 28;
    public final int maxScreenRow = 15;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    //worldmap
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize* maxWorldRow;
    
    //FPS
    int FPS = 60;
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(); // KeyHandler instance
    Thread gameThread;
    public SolidCheck solidCheck = new SolidCheck(this);
    public Player player = new Player(this,keyH);

    // Logika Random Encounter
    private Random random = new Random();
    public static final int GRASS_TILE_ID = 2;
    private int delay = 10;

    private String gameState = "OVERWORLD"; 
    private battlePane currentBattlePane; 

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH); // Menambahkan KeyListener ke GamePanel
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0/FPS; 
        double nextDrawTime = System.nanoTime() + drawInterval; 
        while(gameThread != null) { 
            update(); 
            repaint(); 
            
            try {
                double remainingTime = nextDrawTime - System.nanoTime(); 
                remainingTime = remainingTime/1000000; 
                if (remainingTime < 0) { 
                    remainingTime = 0; 
                }
                Thread.sleep((long) remainingTime); 
                nextDrawTime += drawInterval; 
            } catch (InterruptedException e) {
                e.printStackTrace(); 
                Thread.currentThread().interrupt(); 
            }
        }
    }

    public void update() {
        if (gameState.equals("OVERWORLD")) {
            boolean playerActuallyMoved = player.update(); 
            if (playerActuallyMoved) { 
                checkRandomEncounter(); 
            }
        }
    }

    private void checkRandomEncounter() {
        if (delay == 0){
            int playerCol = player.worldX / tileSize;
            int playerRow = player.worldY / tileSize;
            int encounterRoll = random.nextInt(10) + 1;

            if (playerCol >= 0 && playerCol < maxWorldCol && playerRow >= 0 && playerRow < maxWorldRow) { 
                int tileType = tileM.mapTileNum[playerCol][playerRow]; 
                if (tileType == GRASS_TILE_ID) { 
                    if (encounterRoll == 2) { 
                        if (player.getActivePokemon() != null && !player.getActivePokemon().isFainted()) { 
                            initiateRandomBattle();
                        } else {
                            System.out.println("Tidak bisa memulai battle, Pokemon utama pingsan atau tidak ada.");
                        }
                    }
                }
            }
            delay = 20;
        }else{
            delay -= 1;
        }
    }

    public void initiateRandomBattle() {
        if (gameState.equals("BATTLE")) return; 

        System.out.println("Pertarungan acak dimulai!");
        this.gameState = "BATTLE";

        List<String> allPokemonNames = Pokedex.getAllPokemonNames();
        Pokemon enemyPokemon = null;
        if (!allPokemonNames.isEmpty()) { 
            String randomEnemyName = allPokemonNames.get(random.nextInt(allPokemonNames.size())); 
            enemyPokemon = Pokedex.getPokemonData(randomEnemyName); 
            int attempts = 0; 
            while ((enemyPokemon == null || (player.getActivePokemon() != null && enemyPokemon.getName().equals(player.getActivePokemon().getName()))) && attempts < allPokemonNames.size() * 2) { 
                randomEnemyName = allPokemonNames.get(random.nextInt(allPokemonNames.size())); 
                enemyPokemon = Pokedex.getPokemonData(randomEnemyName); 
                attempts++;
            }
        }
        
        if (enemyPokemon == null) {
             enemyPokemon = Pokedex.getPokemonData("Magikarp"); 
             if (enemyPokemon == null) { 
                 System.err.println("Fallback Pokemon Magikarp tidak ditemukan di Pokedex!");
                 this.gameState = "OVERWORLD"; 
                 return;
             }
        }

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame == null) {
            System.err.println("Tidak bisa mendapatkan JFrame untuk beralih ke battle!");
            this.gameState = "OVERWORLD"; 
            return;
        }

        ActionListener battleEndAction = event -> {
            System.out.println("Battle selesai, kembali ke overworld.");
            frame.setContentPane(this); 
            this.gameState = "OVERWORLD";
            this.currentBattlePane = null; 
            frame.revalidate();
            frame.repaint();
            this.requestFocusInWindow(); // Pastikan GamePanel mendapatkan fokus lagi
            keyH.resetKeyStates();     // Reset status tombol
        };

        ActionListener runAttemptListener = runEvent -> {
            System.out.println("Pemain mencoba lari dari pertarungan acak.");
            frame.setContentPane(this); 
            this.gameState = "OVERWORLD";
            this.currentBattlePane = null;
            frame.revalidate();
            frame.repaint();
            this.requestFocusInWindow(); // Pastikan GamePanel mendapatkan fokus lagi
            keyH.resetKeyStates();     // Reset status tombol
        };
        
        if (player.getActivePokemon() == null) { 
             System.err.println("Player tidak memiliki active Pokemon. Tidak bisa memulai battle.");
             this.gameState = "OVERWORLD";
             return;
        }

        currentBattlePane = new battlePane(
            player.getActivePokemon(), 
            enemyPokemon,
            player.getInventory(), 
            runAttemptListener,
            battleEndAction
        ); 

        frame.setContentPane(currentBattlePane.getPanel()); 
        frame.revalidate();
        frame.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D)g; 

        if (gameState.equals("OVERWORLD")) {
            tileM.draw(g2);
            player.draw(g2);
        } else if (gameState.equals("BATTLE") && currentBattlePane != null) {
            // Tidak perlu melakukan apa-apa di sini karena battlePane sudah menjadi contentPane
        }
        g2.dispose(); 
    }

    public String getGameState() {
        return gameState;
    }
}