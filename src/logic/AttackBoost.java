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
    public void applyEffect(Pokemon target) {
        target.boostAttack(boostAmount, duration);
    }
}

