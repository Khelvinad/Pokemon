package logic;

import java.util.Random;

public class Battle {
    private Pokemon pokemon1; //player
    private Pokemon pokemon2; //enemy
    private Random random = new Random();

    public Battle(Pokemon pokemon1, Pokemon pokemon2) {
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
    }

    public void setPlayerPokemon(Pokemon playerPokemon) {
        this.pokemon1 = playerPokemon;
    }

    public Pokemon getPlayerPokemon() {
        return this.pokemon1;
    }

    public Pokemon getEnemyPokemon() {
        return this.pokemon2;
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

        double actionRoll = random.nextDouble();

        if (pokemon2.getMoves().isEmpty()) { 
            return pokemon2.getName() + " has no moves!"; 
        }
        
        if (actionRoll < 0.15) { 
            int boostAmount = 10; 
            int duration = 1;   
            pokemon2.boostAttack(boostAmount, duration); 
            return pokemon2.getName() + " used an Attack Boost! Its attack!";
        }
        else if (actionRoll < 0.30 && pokemon2.getHealth() < pokemon2.getMaxHealth() * 0.5 && pokemon2.getHealth() > 0) {
            int healAmount = 20; 
            int oldHP = pokemon2.getHealth();
            pokemon2.setHealth(Math.min(pokemon2.getMaxHealth(), pokemon2.getHealth() + healAmount)); 
            return pokemon2.getName() + " healed itself for " + (pokemon2.getHealth() - oldHP) + " HP!";
        }
        else {
            Move enemyMove = pokemon2.getMoves().get(random.nextInt(pokemon2.getMoves().size())); 
            return pokemon2.attack(pokemon1, enemyMove); 
        }
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

    public void start() {
        System.out.println("A battle begins between " + pokemon1.getName() + " and " + pokemon2.getName() + "!");
    }

    public static int calculateDamage(Pokemon attacker, Move move, Pokemon defender) {
        int attackerAttack = attacker.getAttack();
        int defenderDefense = defender.getDefense();
        double effectiveness = getTypeEffectiveness(move.getType(), defender.getType());
        if (defenderDefense == 0) defenderDefense = 1;

        int damage = (int) ((move.getPower() * attackerAttack / defenderDefense) * effectiveness);
        return Math.max(0, damage);
    }

    private static double[][] typeChart = {
        // ATTACKER ↓ | DEFENDER →   FI    WA    GR    NO    EL    PO    GD    PS    DA    RO    GH
        /* FIRE */    {  0.8, 0.5,  2.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  1.0  },
        /* WATER */   {  2.0, 0.5,  0.5,  1.0,  1.0,  1.0,  2.0,  1.0,  1.0,  2.0,  1.0  },
        /* GRASS */   {  0.5, 2.0,  0.5,  1.0,  1.0,  0.5,  2.0,  1.0,  1.0,  2.0,  1.0  },
        /* NORMAL */  {  1.0, 1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  0.0  },
        /* ELECTRIC */{  1.0, 2.0,  0.5,  1.0,  0.5,  1.0,  0.0,  1.0,  1.0,  1.0,  1.0  },
        /* POISON */  {  1.0, 1.0,  2.0,  1.0,  1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5  },
        /* GROUND */  {  2.0, 1.0,  0.5,  1.0,  2.0,  2.0,  1.0,  1.0,  1.0,  2.0,  1.0  },
        /* PSYCHIC */ {  1.0, 1.0,  1.0,  1.0,  1.0,  2.0,  1.0,  0.5,  0.0,  1.0,  1.0  },
        /* DARK */    {  1.0, 1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  2.0,  0.5,  1.0,  0.5  },
        /* ROCK */    {  2.0, 1.0,  1.0,  1.0,  1.0,  1.0,  0.5,  1.0,  1.0,  1.0,  1.0  },
        /* GHOST */   {  1.0, 1.0,  1.0,  0.0,  1.0,  1.0,  1.0,  2.0,  0.5,  1.0,  2.0  }
    };

    private static double getTypeEffectiveness(Type attackType, Type defendType) {
        int attackIndex = attackType.ordinal();
        int defendIndex = defendType.ordinal();

        if (attackIndex >= 0 && attackIndex < typeChart.length &&
            defendIndex >= 0 && defendIndex < typeChart[attackIndex].length) {
            return typeChart[attackIndex][defendIndex];
        } else {
            System.out.println("Type effectiveness data not found for " + attackType + " attacking " + defendType + ". Ordinals: " + attackIndex + ", " + defendIndex + ". Returning 1.0 (normal effectiveness)."); //
            return 1.0;
        }
    }
}