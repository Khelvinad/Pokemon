package logic;

public abstract class Items {
    private String name;

    public Items(String name) {
        this.name = name;
    }  

    public String getName() {
        return name;
    }

    public abstract String applyEffect(Pokemon target);

}
