package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pokedex {

    private static final Map<String, Pokemon> POKEMON_DATA = new HashMap<>();

    static {
        Pokemon bulbasaur = new Pokemon("Bulbasaur", Type.GRASS, 45, 49, 49);
        bulbasaur.addMove(new Move("Tackle", Type.NORMAL, 20));
        bulbasaur.addMove(new Move("Vine Whip", Type.GRASS, 25));
        bulbasaur.setImagePathF("/Asset/Pokemon/bulba.png");
        bulbasaur.setImagePathB("/Asset/Pokemon/bulbaBack.png");
        POKEMON_DATA.put("Bulbasaur", bulbasaur);

        Pokemon charmander = new Pokemon("Charmander", Type.FIRE, 39, 52, 43);
        charmander.addMove(new Move("Scratch", Type.NORMAL, 20));
        charmander.addMove(new Move("Ember", Type.FIRE, 20));
        charmander.setImagePathF("/Asset/Pokemon/charmender.png");
        charmander.setImagePathB("/Asset/Pokemon/charmenderBack.png");
        POKEMON_DATA.put("Charmander", charmander);

        Pokemon squirtle = new Pokemon("Squirtle", Type.WATER, 44, 48, 65);
        squirtle.addMove(new Move("Tackle", Type.NORMAL, 20));
        squirtle.addMove(new Move("Water Gun", Type.WATER, 20));
        squirtle.setImagePathF("/Asset/Pokemon/squirtle.png");
        squirtle.setImagePathB("/Asset/Pokemon/squirtleBack.png");
        POKEMON_DATA.put("Squirtle", squirtle);

        Pokemon pikachu = new Pokemon("Pikachu", Type.ELECTRIC, 59, 55, 40);
        pikachu.addMove(new Move("Thunderbolt", Type.ELECTRIC, 40));
        pikachu.addMove(new Move("Quick Attack", Type.NORMAL, 20));
        pikachu.setImagePathF("/Asset/Pokemon/pikachu.png");
        pikachu.setImagePathB("/Asset/Pokemon/pikachuBack.png");
        POKEMON_DATA.put("Pikachu", pikachu);

        Pokemon cubone = new Pokemon("Cubone", Type.GROUND, 50, 50, 95);
        cubone.addMove(new Move("Bone Club", Type.GROUND, 65));
        cubone.addMove(new Move("Headbutt", Type.NORMAL, 70));
        cubone.setImagePathF("/Asset/Pokemon/cubone.png");
        cubone.setImagePathB("/Asset/Pokemon/cuboneBack.png");
        POKEMON_DATA.put("Cubone", cubone);

        Pokemon haunter = new Pokemon("Haunter", Type.GHOST, 45, 50, 45);
        haunter.addMove(new Move("Lick", Type.GHOST, 30));
        haunter.addMove(new Move("Shadow Ball", Type.GHOST, 80));
        haunter.setImagePathF("/Asset/Pokemon/haunter.png");
        haunter.setImagePathB("/Asset/Pokemon/haunterBack.png");
        POKEMON_DATA.put("Haunter", haunter);

        Pokemon magikarp = new Pokemon("Magikarp", Type.WATER, 20, 10, 55);
        magikarp.addMove(new Move("Splash", Type.NORMAL, 0));
        magikarp.addMove(new Move("Tackle", Type.NORMAL, 40));
        magikarp.setImagePathF("/Asset/Pokemon/magikarp.png");
        magikarp.setImagePathB("/Asset/Pokemon/magikarpBack.png");
        POKEMON_DATA.put("Magikarp", magikarp);

        Pokemon meowth = new Pokemon("Meowth", Type.NORMAL, 40, 45, 35);
        meowth.addMove(new Move("Scratch", Type.NORMAL, 40));
        meowth.addMove(new Move("Bite", Type.DARK, 60));
        meowth.setImagePathF("/Asset/Pokemon/mewoth.png");
        meowth.setImagePathB("/Asset/Pokemon/mewothBack.png");
        POKEMON_DATA.put("Meowth", meowth);

        Pokemon mewtwo = new Pokemon("Mewtwo", Type.PSYCHIC, 106, 110, 90);
        mewtwo.addMove(new Move("Confusion", Type.PSYCHIC, 50));
        mewtwo.addMove(new Move("Psychic", Type.PSYCHIC, 90));
        mewtwo.setImagePathF("/Asset/Pokemon/mewtwo.png");
        mewtwo.setImagePathB("/Asset/Pokemon/mewtwoBack.png");
        POKEMON_DATA.put("Mewtwo", mewtwo);

        Pokemon mrMime = new Pokemon("Mr. Mime", Type.PSYCHIC, 40, 45, 65);
        mrMime.addMove(new Move("Confusion", Type.PSYCHIC, 50));
        mrMime.addMove(new Move("Psybeam", Type.PSYCHIC, 65));
        mrMime.setImagePathF("/Asset/Pokemon/mr-mime.png");
        mrMime.setImagePathB("/Asset/Pokemon/mr-mimeBack.png");
        POKEMON_DATA.put("Mr. Mime", mrMime);
        
        Pokemon snorlax = new Pokemon("Snorlax", Type.NORMAL, 160, 110, 65);
        snorlax.addMove(new Move("Tackle", Type.NORMAL, 40));
        snorlax.addMove(new Move("Body Slam", Type.NORMAL, 85));
        snorlax.addMove(new Move("Hyper Beam", Type.NORMAL, 150));
        snorlax.setImagePathF("/Asset/Pokemon/snorlax.png");
        snorlax.setImagePathB("/Asset/Pokemon/snorlaxBack.png");
        POKEMON_DATA.put("Snorlax", snorlax);
    }

    public static Pokemon getPokemonData(String name) {
        Pokemon template = POKEMON_DATA.get(name);
        if (template == null) {
            return null;
        }
        Pokemon instance = new Pokemon(template.getName(), template.getType(), template.getMaxHealth(), template.getAttack(), template.getDefense());
        instance.setHealth(template.getMaxHealth());
        for (Move move : template.getMoves()) {
            instance.addMove(new Move(move.getName(), move.getType(), move.getPower()));
        }
        instance.setImagePathF(template.getImagePathF());
        instance.setImagePathB(template.getImagePathB());
        return instance;
    }

    public static List<String> getAllPokemonNames() {
        return new ArrayList<>(POKEMON_DATA.keySet());
    }
}