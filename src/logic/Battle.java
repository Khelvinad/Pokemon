package logic;

import java.util.Random;

public class Battle {
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private Random random = new Random();

    public Battle(Pokemon pokemon1, Pokemon pokemon2) {
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
    }
    
    public String executePlayerTurn(Move chosenMove) {
        if (pokemon1.isFainted() || pokemon2.isFainted()) {
            return "Battle is already over.";
        }
        pokemon1.updateBuffs();
        pokemon2.updateBuffs();
        return pokemon1.attack(pokemon2, chosenMove);
    }

    public String executeEnemyTurn() {
        if (pokemon1.isFainted() || pokemon2.isFainted()) {
            return "Battle is already over.";
        }
        pokemon1.updateBuffs();
        pokemon2.updateBuffs();
        if (pokemon2.getMoves().isEmpty()) {
            return pokemon2.getName() + " has no moves!";
        }
        Move enemyMove = pokemon2.getMoves().get(random.nextInt(pokemon2.getMoves().size())); //
        return pokemon2.attack(pokemon1, enemyMove);
    }

    public boolean isBattleOver() {
        return pokemon1.isFainted() || pokemon2.isFainted();
    }

    public String getBattleResult() {
        if (pokemon1.isFainted()) {
            return pokemon2.getName() + " wins!";
        } else if (pokemon2.isFainted()) {
            return pokemon1.getName() + " wins!";
        }
        return "The battle continues.";
    }


    public void start() { //
        System.out.println("A battle begins between " + pokemon1.getName() + " and " + pokemon2.getName() + "!");
    }


    public static int calculateDamage(Pokemon attacker, Move move, Pokemon defender) {
        int attackerAttack = attacker.getAttack();
        int defenderDefense = defender.getDefense();
        double effectiveness = getTypeEffectiveness(move.getType(), defender.getType());
        if (defenderDefense == 0) defenderDefense = 1;

        int damage = (int) ((move.getPower() * attackerAttack / defenderDefense) * effectiveness);
        return Math.max(0, damage); //
    }

    private static double[][] typeChart = { //
            //                      FIRE   WATER  GRASS  NORMAL ELECTRIC
            /* FIRE */         {  1.0,   0.5,   2.0,   1.0,   1.0  }, //
            /* WATER */        {  0.5,   1.0,   0.5,   1.0,   2.0  }, // Modifikasi agar sesuai dengan type effectiveness umum (Water vs Electric)
            /* GRASS */        {  2.0,   0.5,   1.0,   1.0,   0.5  }, // Modifikasi (Grass vs Electric)
            /* NORMAL */       {  1.0,   1.0,   1.0,   1.0,   1.0  }, //
            /* ELECTRIC */     {  1.0,   1.0,   1.0,   1.0,   0.5  }  // Modifikasi (Electric vs Electric = 0.5)
            // FIRE vs WATER = 0.5, FIRE vs GRASS = 2.0
            // WATER vs FIRE = 2.0, WATER vs GRASS = 0.5, WATER vs ELECTRIC = 2.0 (Electric is strong against Water)
            // GRASS vs FIRE = 0.5, GRASS vs WATER = 2.0, GRASS vs ELECTRIC = 0.5
            // ELECTRIC vs WATER = 2.0, ELECTRIC vs GRASS = 0.5, ELECTRIC vs ELECTRIC = 0.5
    };

    private static double getTypeEffectiveness(Type attackType, Type defendType) {
        int attackIndex = attackType.ordinal(); //
        int defendIndex = defendType.ordinal(); //

        if (attackIndex < typeChart.length && defendIndex < typeChart[attackIndex].length) { //
            return typeChart[attackIndex][defendIndex]; //
        } else {
            System.out.println("Type effectiveness data not found for " + attackType + " attacking " + defendType + ". Returning 1.0 (normal effectiveness)."); //
            return 1.0; //
        }
    }
}