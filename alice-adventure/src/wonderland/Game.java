package wonderland;

import java.io.*;
import java.util.*;

public class Game {
    private Map<String, Location> locations = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();
    private Map<String, Character> characters = new HashMap<>();
    private Inventory inventory = new Inventory();
    private Control control;
    private Location current;
    private Location safeRoom;

    public static void main(String[] args) throws Exception {
        Game g = new Game();
        g.loadData();
        g.run();
    }

    private void loadData() throws Exception {
        loadItems();
        loadCharacters();
        loadLocations();
        this.control = new Control(new File("alice-adventure/data/actions.txt"));
    }

    private void loadItems() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("alice-adventure/data/items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    items.put(parts[0].trim().toLowerCase(), new Item(parts[0].trim(), parts[1].trim()));
                }
            }
        }
    }

    private void loadCharacters() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("alice-adventure/data/characters.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    characters.put(parts[0].trim().toLowerCase(), new Character(parts[0].trim(), parts[1].trim()));
                }
            }
        }
    }

    private void loadLocations() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("alice-adventure/data/locations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    Location loc = new Location(parts[0].trim(), parts[1].trim());
                    locations.put(parts[0].trim().toLowerCase(), loc);
                }
            }
        }
        // Setup connections and items/characters manually
        Location rabbitHole = locations.get("rabbit hole entrance");
        Location hallway = locations.get("hallway");
        Location garden = locations.get("garden");
        Location teaParty = locations.get("tea party");
        Location court = locations.get("queens court");
        safeRoom = garden;
        current = rabbitHole;

        rabbitHole.setExit("down", hallway);

        hallway.setExit("up", rabbitHole);
        hallway.setExit("north", garden);
        hallway.setExit("east", teaParty);
        hallway.setExit("west", court);

        garden.setExit("south", hallway);
        teaParty.setExit("west", hallway);
        court.setExit("east", hallway);

        garden.addItem(items.get("rose"));
        hallway.addItem(items.get("key"));
        teaParty.addItem(items.get("teacup"));
        court.addItem(items.get("tart"));

        rabbitHole.addCharacter(characters.get("white rabbit"));
        teaParty.addCharacter(characters.get("mad hatter"));
        teaParty.addCharacter(characters.get("march hare"));
        garden.addCharacter(characters.get("cheshire cat"));
        court.addCharacter(characters.get("red queen"));
    }

    private void run() throws Exception {
        System.out.println("Welcome to Wonderland Adventure!");
        System.out.println("Your goal is to gather interesting items and return to the garden safely.");
        Scanner scan = new Scanner(System.in);
        boolean playing = true;
        describe(current);
        while (playing) {
            System.out.print("> ");
            String input = scan.nextLine();
            playing = control.handle(input, this);
        }
        scan.close();
        System.out.println("Goodbye!");
    }

    public Location getCurrent() { return current; }
    public void setCurrent(Location loc) { current = loc; describe(loc); }
    public Inventory getInventory() { return inventory; }
    public Location getSafeRoom() { return safeRoom; }

    public void describe(Location loc) {
        System.out.println();
        System.out.println(loc.getDescription());
        if (!loc.getItems().isEmpty()) {
            System.out.print("You see: ");
            for (Item i : loc.getItems()) {
                System.out.print(i.getName() + " ");
            }
            System.out.println();
        }
        if (!loc.getCharacters().isEmpty()) {
            System.out.print("Characters here: ");
            for (Character c : loc.getCharacters()) {
                System.out.print(c.getName() + " ");
            }
            System.out.println();
        }
    }
}
