package logic;

import entity.Player;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandler {

    private static final String SAVE_FILE_PATH = "savegame.txt";

    public static void saveGame(Player player, String currentMapId) {
        if (player == null) {
            System.err.println("Cannot save game: Player object is null.");
            return;
        }
        if (player.getInventory() == null) {
            System.err.println("Cannot save game: Player inventory is null.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE_PATH))) {

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            writer.write("SAVE_TIMESTAMP:" + sdf.format(new Date()) + "\n");

            writer.write("PLAYER_NAME:" + player.getPlayerName() + "\n");
            writer.write("PLAYER_WORLD_X:" + player.worldX + "\n");
            writer.write("PLAYER_WORLD_Y:" + player.worldY + "\n");
            writer.write("CURRENT_MAP_ID:" + currentMapId + "\n");

            List<Pokemon> party = player.getInventory().getPokemons();
            writer.write("PARTY_SIZE:" + (party == null ? 0 : party.size()) + "\n");

            if (party != null && !party.isEmpty()) {
                for (int i = 0; i < party.size(); i++) {
                    Pokemon p = party.get(i);
                    String prefix = "POKEMON_" + (i + 1) + "_";
                    writer.write(prefix + "NAME:" + p.getName() + "\n");
                    writer.write(prefix + "CURRENT_HP:" + p.getHealth() + "\n");
                    writer.write(prefix + "MAX_HP:" + p.getMaxHealth() + "\n");
                    writer.write(prefix + "ATTACK:" + p.getAttack() + "\n");
                    writer.write(prefix + "DEFENSE:" + p.getDefense() + "\n");
                }
                Pokemon activeP = player.getActivePokemon();
                int activeIndex = -1;
                if (activeP != null) {
                    activeIndex = party.indexOf(activeP);
                }
                writer.write("ACTIVE_POKEMON_INDEX:" + activeIndex + "\n");
            } else {
                writer.write("ACTIVE_POKEMON_INDEX:-1\n");
            }

            Inventory inventory = player.getInventory();
            for (String itemName : inventory.getItemNames()) {
                int quantity = inventory.getItemQuantity(itemName);
                if (quantity > 0) {
                    writer.write("INVENTORY_ITEM_" + itemName.replace(" ", "_") + ":" + quantity + "\n");
                }
            }
            System.out.println("Game saved successfully to " + SAVE_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Map<String, String> peekSaveFileInfo() {
        File saveFile = new File(SAVE_FILE_PATH);
        if (!saveFile.exists() || !saveFile.canRead() || saveFile.length() == 0) {
            return null;
        }

        Map<String, String> info = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE_PATH))) {
            String line;
            int linesRead = 0;
            String firstPokemonName = null;
            String partySizeStr = null;

            while ((line = reader.readLine()) != null && linesRead < 10) {
                if (line.startsWith("PLAYER_NAME:")) {
                    info.put("PLAYER_NAME", line.substring("PLAYER_NAME:".length()));
                } else if (line.startsWith("SAVE_TIMESTAMP:")) {
                    info.put("SAVE_TIMESTAMP", line.substring("SAVE_TIMESTAMP:".length()));
                } else if (line.startsWith("POKEMON_1_NAME:")) {
                    firstPokemonName = line.substring("POKEMON_1_NAME:".length());
                } else if (line.startsWith("PARTY_SIZE:")) {
                    partySizeStr = line.substring("PARTY_SIZE:".length());
                }
                linesRead++;
            }

            if (firstPokemonName != null && partySizeStr != null) {
                info.put("POKEMON_INFO", firstPokemonName + " & " + (Integer.parseInt(partySizeStr) - 1) + " others");
            } else if (firstPokemonName != null) {
                info.put("POKEMON_INFO", firstPokemonName);
            }

            return info.isEmpty() ? null : info;
        } catch (IOException e) {
            System.err.println("Error peeking save file info: " + e.getMessage());
            return null;
        }
    }

    public static Map<String, String> loadGameData() {
        Map<String, String> loadedData = new HashMap<>();
        File saveFile = new File(SAVE_FILE_PATH);

        if (!saveFile.exists()) {
            System.out.println("No save file found at " + SAVE_FILE_PATH);
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    loadedData.put(parts[0], parts[1]);
                }
            }
            System.out.println("Game data loaded successfully from " + SAVE_FILE_PATH);
            return loadedData;
        } catch (FileNotFoundException e) {
            System.err.println("Save file not found: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Error loading game data: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteSaveFile() {
        File saveFile = new File(SAVE_FILE_PATH);
        if (saveFile.exists()) {
            if (saveFile.delete()) {
                System.out.println("Save file deleted successfully.");
                return true;
            } else {
                System.err.println("Failed to delete the save file.");
                return false;
            }
        } else {
            System.out.println("No save file to delete.");
            return true;
        }
    }
}