package logic;

import java.util.List;
import java.util.Random;

/**
 * Kelas ini mengelola logika inti dari sistem gacha.
 * Mengambil data Pokemon dari Pokedex untuk memberikan hasil acak.
 */
public class GachaSystem {

    private final Random random;

    public GachaSystem() {
        this.random = new Random();
    }

    /**
     * Menarik satu Pokemon acak dari daftar yang tersedia di Pokedex.
     * @return Objek Pokemon yang baru, atau null jika tidak ada Pokemon.
     */
    public Pokemon pullSinglePokemon() {
        // Mengambil semua nama Pokemon yang terdaftar di Pokedex
        List<String> allPokemonNames = Pokedex.getAllPokemonNames();

        if (allPokemonNames == null || allPokemonNames.isEmpty()) {
            System.err.println("GachaSystem Error: Pokedex tidak memiliki data Pokemon.");
            return null;
        }

        // Pilih nama Pokemon secara acak
        int randomIndex = random.nextInt(allPokemonNames.size());
        String randomPokemonName = allPokemonNames.get(randomIndex);

        // Buat instance baru dari Pokemon yang terpilih
        Pokemon obtainedPokemon = Pokedex.getPokemonData(randomPokemonName);

        return obtainedPokemon;
    }
}