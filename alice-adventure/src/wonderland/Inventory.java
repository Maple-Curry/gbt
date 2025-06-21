package wonderland;

import java.util.*;

public class Inventory {
    private List<Item> items = new ArrayList<>();

    public void add(Item item) { if (item != null) items.add(item); }
    public void remove(Item item) { items.remove(item); }
    public boolean has(String name) {
        return items.stream().anyMatch(i -> i.getName().equalsIgnoreCase(name));
    }
    public Item find(String name) {
        return items.stream().filter(i -> i.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    public List<Item> list() { return items; }
}
