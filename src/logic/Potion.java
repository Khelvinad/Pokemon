package logic;

public class Potion extends Items {
    private int healAmount;

    public Potion(int healAmount) {
        super("Potion");
        this.healAmount = healAmount;
    }

    @Override
    public String applyEffect(Pokemon target) {
        System.out.println("[DEBUG Potion.applyEffect] Target: " + target.getName() + 
                           ", Current HP: " + target.getHealth() + 
                           ", Max HP: " + target.getMaxHealth() + 
                           ", Potion Heal Amount: " + this.healAmount);

        int oldHP = target.getHealth();
        
        if (oldHP >= target.getMaxHealth()) {
            System.out.println("[DEBUG Potion.applyEffect] HP is already full or more.");
            return target.getName() + "'s HP is already full."; 
        }

        int newHP = oldHP + this.healAmount;
        if (newHP > target.getMaxHealth()) {
            newHP = target.getMaxHealth();
        }
        target.setHealth(newHP);
        
        int actualHealAmount = newHP - oldHP;

        System.out.println("[DEBUG Potion.applyEffect] Old HP: " + oldHP + 
                        ", Calculated New HP: " + (oldHP + this.healAmount) + 
                        ", Capped New HP: " + newHP + 
                        ", Actual HP After Set: " + target.getHealth() +
                        ", Amount Healed: " + actualHealAmount);
        
        if (actualHealAmount > 0) {
            return target.getName() + " recovered " + actualHealAmount + " HP!";
        } else {
            return target.getName() + " was not healed (HP might be full or heal amount was ineffective).";
        }
    }
}