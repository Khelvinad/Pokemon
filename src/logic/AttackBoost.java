package logic;

public class AttackBoost extends Items {
    private int boostAmount;
    private int duration;

    public AttackBoost(int boostAmount, int duration) {
        super("Attack Boost");
        this.boostAmount = boostAmount;
        this.duration = duration;
    }

    @Override
    public String applyEffect(Pokemon target) {
        target.boostAttack(boostAmount, duration);
        return target.getName() + "attack by " + boostAmount + " for " + duration + " turns!";
    }
}