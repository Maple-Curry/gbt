package wonderland;

import java.util.*;

public class Location {
    private String name;
    private String description;
    private Map<String, Location> exits = new HashMap<>();
    private List<Item> items = new ArrayList<>();
    private List<Character> characters = new ArrayList<>();

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setExit(String direction, Location location) {
        exits.put(direction.toLowerCase(), location);
    }

    public Location getExit(String direction) {
        return exits.get(direction.toLowerCase());
    }

    public void addItem(Item item) { if (item != null) items.add(item); }
    public void removeItem(Item item) { items.remove(item); }
    public List<Item> getItems() { return items; }

    public void addCharacter(Character c) { if (c != null) characters.add(c); }
    public void removeCharacter(Character c) { characters.remove(c); }
    public List<Character> getCharacters() { return characters; }

    public String getDescription() { return description; }
    public String getName() { return name; }
}
