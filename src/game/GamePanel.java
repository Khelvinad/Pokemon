package game;

import entity.Player;
import game.GamePanel.GameState;
import ingamebattle.battlePane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import logic.DataHandler;
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

    public enum GameState{PLAYING, IN_BATTLE, PAUSED};
    public GameState gameState = GameState.PLAYING;
    private Random random = new Random();
    
    //FPS
    int FPS = 60;
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public SolidCheck solidCheck = new SolidCheck(this);
    public Player player = new Player(this,keyH);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
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
            }
        }
    }

    public void update() {
        if (gameState != GameState.PLAYING) return;
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        tileM.draw(g2);
        player.draw(g2);
        g2.dispose();
    }

    public void startRandomBattle() {
        if (gameState == GameState.IN_BATTLE) return;

        gameState = GameState.IN_BATTLE;
        System.out.println("Starting random battle...");

        List<String> allPokemonNames = Pokedex.getAllPokemonNames();
        if (allPokemonNames.isEmpty()) {
            System.err.println("Pokedex is empty!");
            gameState = GameState.PLAYING;
            return;
        }
        String randomPokemonName = allPokemonNames.get(random.nextInt(allPokemonNames.size()));
        Pokemon wildPokemon = Pokedex.getPokemonData(randomPokemonName);

        if (wildPokemon == null) {
            System.err.println("Could not load wild Pokemon: " + randomPokemonName);
            gameState = GameState.PLAYING;
            return;
        }
        Pokemon playerActivePokemon = player.getActivePokemon();
        if (playerActivePokemon == null || playerActivePokemon.isFainted()) {
            playerActivePokemon = player.getPokemonParty().stream()
                                    .filter(p -> p != null && !p.isFainted())
                                    .findFirst().orElse(null);
            if (playerActivePokemon == null) {
                System.err.println("Player has no healthy Pokemon!");
                gameState = GameState.PLAYING;
                return;
            }
            player.setActivePokemon(playerActivePokemon);
        }

        ActionListener afterBattleHandler = event -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.getContentPane().removeAll();
                topFrame.setContentPane(this);
                topFrame.pack();
                topFrame.setLocationRelativeTo(null);
                topFrame.setVisible(true);
                this.requestFocusInWindow();
            }
            gameState = GameState.PLAYING;
            String currentMapId = "map01.txt";
            DataHandler.saveGame(player, currentMapId);
        };
        ActionListener runAttemptHandlerForWildBattle = runEvent -> {
            System.out.println("Player ran from wild Pokemon!");
            if (afterBattleHandler != null) {
                afterBattleHandler.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "ran_successfully"));
            }
        };

        battlePane battleScreen = new battlePane(
            playerActivePokemon,
            wildPokemon,
            player.getInventory(),
            runAttemptHandlerForWildBattle,
            afterBattleHandler
        );

        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            topFrame.getContentPane().removeAll();
            topFrame.setContentPane(battleScreen.getPanel());
            topFrame.pack();
            topFrame.setLocationRelativeTo(null);
            battleScreen.getPanel().requestFocusInWindow();
        } else {
            System.err.println("Could not get top-level frame for battle pane.");
            gameState = GameState.PLAYING;
        }
    }

}
