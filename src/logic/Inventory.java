package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private List<Pokemon> pokemons;
    private static final int MAX_POKEMONS = 6;

    private Map<String, Items> itemMap; 
    private Map<String, Integer> itemQuantities;

    public Inventory() {
        this.pokemons = new ArrayList<>();
        this.itemMap = new HashMap<>();
        this.itemQuantities = new HashMap<>();
    }

    public boolean addPokemon(Pokemon pokemon) {
        if (this.pokemons.size() < MAX_POKEMONS) {
            this.pokemons.add(pokemon);
            return true;
        }
        return false; 
    }

    public List<Pokemon> getPokemons() {
        return this.pokemons;
    }
    
    public Pokemon getPokemon(int index) {
        if (index >= 0 && index < pokemons.size()) {
            return pokemons.get(index);
        }
        return null;
    }
    
    public int getPokemonCount() {
        return this.pokemons.size();
    }

    public void addItem(Items item, int quantity) {
        if (item == null || quantity <= 0) return;
        String itemName = item.getName();
        itemMap.putIfAbsent(itemName, item); 
        itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + quantity);
    }

    public boolean removeItem(String itemName, int quantity) {
        if (!itemMap.containsKey(itemName) || quantity <= 0) return false;
        int currentQuantity = itemQuantities.getOrDefault(itemName, 0);
        if (currentQuantity < quantity) {
            return false; 
        }
        itemQuantities.put(itemName, currentQuantity - quantity);
        return true;
    }
    
    public Items getItem(String itemName) {
        return itemMap.get(itemName);
    }

    public int getItemQuantity(String itemName) {
        return itemQuantities.getOrDefault(itemName, 0);
    }

    public List<String> getItemNames() {
        return new ArrayList<>(itemMap.keySet()); 
    }

    public String useItem(String itemName, Pokemon target) {
        Items item = getItem(itemName);
        if (item == null) {
            return "Item " + itemName + " not found in inventory.";
        }
        if (getItemQuantity(itemName) <= 0) {
             return itemName + " not available (quantity is 0).";
        }

        if (item instanceof Potion) {
            if (target.getHealth() >= target.getMaxHealth()) {
                return target.getName() + "'s HP is already full!";
            }
        }

        String effectMessage = item.applyEffect(target);
        removeItem(itemName, 1); 

        return effectMessage;
    }
}