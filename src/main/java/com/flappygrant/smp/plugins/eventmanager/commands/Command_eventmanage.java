package com.flappygrant.smp.plugins.eventmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;

import com.flappygrant.smp.plugins.eventmanager.utils.Event;
import com.flappygrant.smp.plugins.eventmanager.utils.Responder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class Command_eventmanage implements CommandExecutor, TabCompleter {
    private Plugin pluginInstance;
    private Event currentEvent;
    Responder responder = new Responder();

    // Allows this class to access the plugin's information
    public void setPluginInstance(Plugin plugin) {
        this.pluginInstance = plugin;
    }

    // Allows this class to access the current event
    public void setCurrentEvent(Event event) {
        this.currentEvent = event;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Rules out senders who are not administrators
        if (!sender.hasPermission("eventmanager.admin")) {
            responder.respondWithError(sender, Responder.ErrorType.NoPermission);
            return false;
        }

        // Do you really need help?
        // You should've read the documentation!
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "details":
                // Sends the current event's details back to the sender
                // An event doesn't need to be active!
                responder.respond(sender, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "> Event Details");
                responder.respond(sender, "Name: " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE.toString()
                        + currentEvent.getName());
                responder.respond(sender,
                        "Starting Position: " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE.toString()
                                + currentEvent.getPosition().x() + " "
                                + currentEvent.getPosition().y() + " "
                                + currentEvent.getPosition().z());
                responder.respond(sender, "World: " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE.toString()
                        + currentEvent.getWorld());
                break;
            case "start":
                // Read the argument name, idiot
                responder.respond(sender, "Starting the currently-configured event...");
                currentEvent.start(pluginInstance);
                responder.respond(sender, "Event successfully started!");
                break;
            case "stop":
                // Read the argument name, idiot
                responder.respond(sender, "Stopping the current event...");
                currentEvent.stop(pluginInstance);
                responder.respond(sender, "Event successfully stopped!");
                break;
            case "edit":
                if (args.length == 1) {
                    // Sends the event editor help messages
                    responder.respond(sender, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "> Event Editor");
                    responder.respond(sender, "/eventmanage edit name <event-name (use " + ChatColor.YELLOW + "<s>"
                            + ChatColor.AQUA + " to add a space)>");
                    responder.respond(sender, "/eventmanage edit position <x> <y> <z>");
                    responder.respond(sender, "/eventmanage edit world <world-name>");
                    break;
                }

                String property = args[1].toLowerCase();

                switch (property) {
                    case "name":
                        currentEvent.setName(args[2].replaceAll("<s>", " "));
                        responder.respond(sender, "Successfully set the current event's name!");
                        break;
                    case "position":
                        currentEvent.setPosition(Float.parseFloat(args[2]), Float.parseFloat(args[3]),
                                Float.parseFloat(args[4]));
                        responder.respond(sender, "Successfully set the current event's joining position!");
                        break;
                    case "world":
                        currentEvent.setWorld(args[2].toLowerCase());
                        responder.respond(sender, "Successfully set the current event's world!");
                        break;
                    default:
                        responder.respondWithErrorMessage(sender,
                                "Unknown event property \"" + property + "\"! Use \"/eventmanage edit\" for help.");
                        break;
                }
                break;
            default:
                responder.respondWithErrorMessage(sender,
                        "Unknown action \"" + subCommand + "\"! Use \"/eventmanage\" for help.");
                break;
        }
        return true;
    }

    private void showHelp(CommandSender sender) {
        // Sends a help menu back to the sender
        responder.respond(sender, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "> Commands Help");
        responder.respond(sender, "/eventmanage details - Tells you the current event's information/details");
        responder.respond(sender, "/eventmanage start - Starts the currently-configured event");
        responder.respond(sender, "/eventmanage stop - Stops the started event");
        responder.respond(sender,
                "/eventmanage edit - Lets you configure an/the-current event");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Read the code, idiot
        List<String> suggestions = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return suggestions;
        }

        if (args.length == 1) {
            suggestions.add("details");
            suggestions.add("start");
            suggestions.add("stop");
            suggestions.add("edit");

            String input = args[0].toLowerCase();
            suggestions.removeIf(s -> !s.startsWith(input));
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
            suggestions.add("name");
            suggestions.add("position");
            suggestions.add("world");

            String input = args[1].toLowerCase();
            suggestions.removeIf(s -> !s.startsWith(input));
        }

        return suggestions;
    }
}
