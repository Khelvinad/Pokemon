package logic;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private String name;
    private Type type;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private List<Move> moves;
    private String imagePathF;
    private String imagePathB;
    private int attackBoost = 0;
    private int attackBoostDuration = 0;

    public Pokemon(String name, Type type, int health, int attack, int defense) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.moves = new ArrayList<>();
        this.maxHealth = health;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttack() {
        return attack + attackBoost;
    }

    public int getDefense() {
        return defense;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public String getImagePathF() {
        return imagePathF;
    }
    public void setImagePathF(String imagePathF) {
        this.imagePathF = imagePathF;
    }

    public String getImagePathB() {
        return imagePathB;
    }
    public void setImagePathB(String imagePathB) {
        this.imagePathB = imagePathB;
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public String attack(Pokemon target, Move move) {
        int damage = Battle.calculateDamage(this, move, target); //
        String attackMessage = this.name + " used " + move.getName() + " on " + target.getName() + " for " + damage + " damage!";
        String faintMessage = target.takeDamage(damage); //
        return attackMessage + (faintMessage.isEmpty() ? "" : "\n" + faintMessage);
    }

    public String takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        if (this.health <= 0) {
            return this.name + " fainted!";
        }
        return ""; 
    }

    public boolean isFainted() {
        return health <= 0;
    }

    public void boostAttack(int amount, int duration) {
        attackBoost += amount;
        attackBoostDuration = duration;
        System.out.println(name + " mendapatkan buff attack + " + amount + " selama " + duration + " turn. Total Attack sekarang: " + getAttack());
    }

    public void updateBuffs() {
        if (attackBoostDuration > 0) {
            attackBoostDuration--;
            if (attackBoostDuration == 0) {
                System.out.println(name + " buff attack telah habis!");
                attackBoost = 0;
            }
        }
    }
}