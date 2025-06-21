package wonderland;

import java.io.*;
import java.util.*;

public class Control {
    private Set<String> actions = new HashSet<>();

    public Control(File actionsFile) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(actionsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                actions.add(line.trim().toLowerCase());
            }
        }
    }

    public boolean handle(String input, Game game) {
        String clean = input.trim().toLowerCase();
        if (clean.isEmpty()) return true;
        String[] parts = clean.split(" ");
        String cmd = parts[0];

        if (!actions.contains(cmd)) {
            // allow direction shortcuts
            if ("n".equals(cmd)) cmd = "north";
            else if ("s".equals(cmd)) cmd = "south";
            else if ("e".equals(cmd)) cmd = "east";
            else if ("w".equals(cmd)) cmd = "west";
        }

        switch (cmd) {
            case "go":
                if (parts.length < 2) { System.out.println("Go where?"); break; }
                move(parts[1], game);
                break;
            case "north": case "south": case "east": case "west": case "up": case "down":
                move(cmd, game);
                break;
            case "take":
                if (parts.length < 2) { System.out.println("Take what?"); break; }
                take(parts[1], game);
                break;
            case "drop":
                if (parts.length < 2) { System.out.println("Drop what?"); break; }
                drop(parts[1], game);
                break;
            case "use":
                if (parts.length < 2) { System.out.println("Use what?"); break; }
                use(parts[1], game);
                break;
            case "inventory":
                showInventory(game);
                break;
            case "look":
                game.describe(game.getCurrent());
                break;
            case "quit":
                return false;
            default:
                System.out.println("I don't understand.");
        }
        return true;
    }

    private void move(String dir, Game game) {
        Location next = game.getCurrent().getExit(dir);
        if (next == null) {
            System.out.println("You can't go that way.");
        } else {
            game.setCurrent(next);
            if (next == game.getSafeRoom()) {
                System.out.println("This feels like a safe place to rest.");
            }
        }
    }

    private void take(String itemName, Game game) {
        Item item = null;
        for (Item i : game.getCurrent().getItems()) {
            if (i.getName().equalsIgnoreCase(itemName)) { item = i; break; }
        }
        if (item == null) {
            System.out.println("There is no " + itemName + " here.");
        } else {
            game.getCurrent().removeItem(item);
            game.getInventory().add(item);
            System.out.println("Taken.");
        }
    }

    private void drop(String itemName, Game game) {
        Item item = game.getInventory().find(itemName);
        if (item == null) {
            System.out.println("You don't have that.");
        } else {
            game.getInventory().remove(item);
            game.getCurrent().addItem(item);
            System.out.println("Dropped.");
        }
    }

    private void use(String itemName, Game game) {
        if (!game.getInventory().has(itemName)) {
            System.out.println("You don't have that.");
            return;
        }
        if ("key".equals(itemName.toLowerCase()) && game.getCurrent().getName().equalsIgnoreCase("Hallway")) {
            System.out.println("You unlock a small door and crawl through to the garden.");
            move("north", game);
        } else {
            System.out.println("Nothing happens.");
        }
    }

    private void showInventory(Game game) {
        System.out.println("You are carrying:");
        for (Item i : game.getInventory().list()) {
            System.out.println("- " + i.getName());
        }
    }
}
