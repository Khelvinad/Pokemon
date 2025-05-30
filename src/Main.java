import ingamebattle.battlePane;
import javax.swing.JFrame;
import logic.Move;
import logic.Pokemon;
import logic.Type;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pokemon Battle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 607);
        frame.setLocationRelativeTo(null);
        Pokemon playerPokemon = new Pokemon("Pikachu", Type.ELECTRIC, 59, 55, 40);
        Pokemon enemyPokemon = new Pokemon("Charmander", Type.FIRE, 90, 45, 35);
        playerPokemon.setImagePathB("/Asset/Pokemon/squirtleBack.png");
        playerPokemon.setImagePathF("/Asset/Pokemon/squirtle.png");
        enemyPokemon.setImagePathB("/Asset/Pokemon/charmenderBack.png");
        enemyPokemon.setImagePathF("/Asset/Pokemon/charmender.png");

        playerPokemon.addMove(new Move("Thunderbolt", Type.ELECTRIC, 40));
        playerPokemon.addMove(new Move("Quick Attack", Type.NORMAL, 20));
        playerPokemon.addMove(new Move("Water Gun", Type.WATER, 30));
        enemyPokemon.addMove(new Move("Ember", Type.FIRE, 30));
        enemyPokemon.addMove(new Move("Scratch", Type.NORMAL, 15));

        battlePane battle = new battlePane(playerPokemon, enemyPokemon);
        frame.add(battle.getPanel());
        frame.setVisible(true);

        //SwingUtilities.invokeLater(() -> new Frame());
        // Pokemon pikachu = new Pokemon("Pikachu", Type.ELECTRIC, 35, 55, 40);
        // pikachu.addMove(new Move("Thunderbolt", Type.ELECTRIC, 90));
        // pikachu.addMove(new Move("Quick Attack", Type.NORMAL, 40));

        // Pokemon bulbasaur = new Pokemon("Bulbasaur", Type.GRASS, 45, 49, 49);
        // bulbasaur.addMove(new Move("Vine Whip", Type.GRASS, 45));
        // bulbasaur.addMove(new Move("Tackle", Type.NORMAL, 40));

        // Battle battle = new Battle(pikachu, bulbasaur);
        // battle.start();
    }
}