import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Frame());
    //     Pokemon pikachu = new Pokemon("Pikachu", Type.ELECTRIC, 35, 55, 40);
    //     pikachu.addMove(new Move("Thunderbolt", Type.ELECTRIC, 90));
    //     pikachu.addMove(new Move("Quick Attack", Type.NORMAL, 40));

    //     Pokemon bulbasaur = new Pokemon("Bulbasaur", Type.GRASS, 45, 49, 49);
    //     bulbasaur.addMove(new Move("Vine Whip", Type.GRASS, 45));
    //     bulbasaur.addMove(new Move("Tackle", Type.NORMAL, 40));

    //     Battle battle = new Battle(pikachu, bulbasaur);
    //     battle.start();
    }
}