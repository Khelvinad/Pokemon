package logic;

public class Potion extends Items{
    private int healAmount;

    public Potion(int healAmount) {
        super("Potion");
        this.healAmount = healAmount;
    }

    @Override
    public void applyEffect(Pokemon target) {
        target.setHealth(target.getHealth() + healAmount);
        if (target.getHealth() > target.getMaxHealth()) {
            target.setHealth(target.getMaxHealth());
        }
    }
}
