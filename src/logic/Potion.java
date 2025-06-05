package logic;

public class Potion extends Items {
    private int healAmount;

    public Potion(int healAmount) {
        super("Potion");
        this.healAmount = healAmount;
    }

    @Override
    public String applyEffect(Pokemon target) {
        int oldHP = target.getHealth();
        
        if (oldHP >= target.getMaxHealth()) {
            return target.getName() + "'s HP is already full."; 
        }

        int newHP = oldHP + this.healAmount;
        if (newHP > target.getMaxHealth()) {
            newHP = target.getMaxHealth();
        }
        target.setHealth(newHP);
        int actualHealAmount = newHP - oldHP;
        if (actualHealAmount > 0) {
            return target.getName() + " recovered " + actualHealAmount + " HP!";
        } else {
            return target.getName() + " was not healed (HP might be full or heal amount was ineffective).";
        }
    }
}