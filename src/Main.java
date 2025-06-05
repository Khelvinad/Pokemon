import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // JFrame frame = new JFrame("Pokemon Battle");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(1080, 607);
        // frame.setLocationRelativeTo(null);
        // Pokemon[] pokemon = new Pokemon[2];
        // pokemon[0] = Pokedex.getPokemonData("Bulbasaur");
        // pokemon[1] = Pokedex.getPokemonData("Charmander");
        // Inventory playerInventory = new Inventory();
        // playerInventory.addPokemon(pokemon[0]);
        // playerInventory.addPokemon(pokemon[1]);
        // playerInventory.addItem(new Potion(20), 5);
        // playerInventory.addItem(new AttackBoost(20, 1), 5);

        // frame.setVisible(true);

        SwingUtilities.invokeLater(() -> new Frame());
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