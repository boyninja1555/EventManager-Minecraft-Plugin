package com.flappygrant.smp.plugins.eventmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import com.flappygrant.smp.plugins.eventmanager.utils.Responder;
import com.flappygrant.smp.plugins.eventmanager.utils.Responder.ErrorType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class Command_eventmanage implements CommandExecutor, TabCompleter {
    private Plugin pluginInstance;
    private Responder responder;

    // Allows this class to access the plugin's information
    public void setPluginInstance(Plugin plugin) {
        pluginInstance = plugin;
    }

    // Allows this class to access the responder
    public void setResponder(Responder r) {
        responder = r;
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
                if (args.length >= 1) {
                    // If the sender specified a specific property, send it back
                    switch (args[1].toLowerCase()) {
                        case "name":
                            responder.respond(sender, "Event Name: " + ChatColor.GOLD
                                    + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getName());
                            break;
                        case "position":
                            responder.respond(sender, "Event Starting Position: " + ChatColor.GOLD
                                    + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getPosition().x()
                                    + " "
                                    + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getPosition().y()
                                    + " "
                                    + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getPosition().z());
                            break;
                        case "world":
                            responder.respond(sender, "World: " + ChatColor.GOLD
                                    + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getWorld());
                            break;
                    }
                    break;
                }

                responder.respond(sender, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "> Event Details");
                responder.respond(sender, "Name: " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE.toString()
                        + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getName());
                responder.respond(sender,
                        "Starting Position: " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE.toString()
                                + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getPosition().x() + " "
                                + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getPosition().y() + " "
                                + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getPosition().z());
                responder.respond(sender, "World: " + ChatColor.YELLOW.toString() + ChatColor.UNDERLINE.toString()
                        + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getWorld());
                break;
            case "start":
                // Read the argument name, idiot
                responder.respond(sender, "Starting the currently-configured event...");
                com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.start(pluginInstance);
                responder.respond(sender, "Event successfully started! Run " + ChatColor.YELLOW + "/eventmanage stop"
                        + Responder.DEFAULT_RESPONSE_COLOR + " to stop it.");
                break;
            case "stop":
                // Read the argument name, idiot
                responder.respond(sender, "Stopping the current event...");
                com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.stop(pluginInstance);
                responder.respond(sender, "Event successfully stopped! Run " + ChatColor.YELLOW + "/eventmanage start"
                        + Responder.DEFAULT_RESPONSE_COLOR + " to start it again.");
                break;
            case "edit":
                if (args.length == 1) {
                    // Sends the event editor help messages
                    responder.respond(sender, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "> Event Editor");
                    responder.respond(sender, "/eventmanage edit name <event-name (use " + ChatColor.YELLOW + "<s>"
                            + Responder.DEFAULT_RESPONSE_COLOR + " to add a space)>");
                    responder.respond(sender, "/eventmanage edit position <x> <y> <z>");
                    responder.respond(sender, "/eventmanage edit world <world-name>");
                    break;
                }

                String property = args[1].toLowerCase();

                switch (property) {
                    case "name":
                        com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent
                                .setName(args[2].replaceAll("<s>", " "));
                        responder.respond(sender, "Successfully set the current event's name!");
                        break;
                    case "position":
                        com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.setPosition(
                                Float.parseFloat(args[2]), Float.parseFloat(args[3]),
                                Float.parseFloat(args[4]));
                        responder.respond(sender, "Successfully set the current event's joining position!");
                        break;
                    case "world":
                        com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.setWorld(args[2].toLowerCase());
                        responder.respond(sender, "Successfully set the current event's world!");
                        break;
                    default:
                        responder.respondWithErrorMessage(sender,
                                "Unknown event property " + ChatColor.YELLOW + property
                                        + Responder.DEFAULT_ERROR_RESPONSE_COLOR
                                        + "! Use " + ChatColor.YELLOW + "/eventmanage edit"
                                        + Responder.DEFAULT_ERROR_RESPONSE_COLOR
                                        + " for help.");
                        break;
                }
                break;
            case "load":
                if (args.length == 1) {
                    responder.respondWithError(sender, ErrorType.RequiresIdentifier);
                    break;
                }

                String l_identifier = args[1].toLowerCase().replaceAll("\\W+", "");
                int l_success = com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.load(sender, l_identifier);

                if (l_success != 0) {
                    responder.respondWithErrorMessage(sender, "Could not load the event file " + ChatColor.YELLOW
                            + l_identifier + ".yml" + Responder.DEFAULT_ERROR_RESPONSE_COLOR + "!");
                    break;
                }

                responder.respond(sender,
                        "Successfully loaded the event " + ChatColor.YELLOW
                                + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getName()
                                + Responder.DEFAULT_RESPONSE_COLOR + "! Run " + ChatColor.YELLOW + "/eventmanage start"
                                + Responder.DEFAULT_RESPONSE_COLOR + " to start it.");
                break;
            case "save":
                if (args.length == 1) {
                    responder.respondWithError(sender, ErrorType.RequiresIdentifier);
                    break;
                }

                String s_identifier = args[1].toLowerCase().replaceAll("\\W+", "");
                int s_success = com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.save(s_identifier);

                if (s_success != 0) {
                    responder.respondWithErrorMessage(sender, "Could not save the event file " + ChatColor.YELLOW
                            + s_identifier + ".yml" + Responder.DEFAULT_ERROR_RESPONSE_COLOR + "!");
                    break;
                }

                responder.respond(sender,
                        "Successfully saved the event " + ChatColor.YELLOW
                                + com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.getName()
                                + Responder.DEFAULT_RESPONSE_COLOR + "! Run " + ChatColor.YELLOW + "/eventmanage load "
                                + s_identifier
                                + Responder.DEFAULT_RESPONSE_COLOR + " to load it.");
                break;
            default:
                responder.respondWithErrorMessage(sender,
                        "Unknown action " + ChatColor.YELLOW + subCommand + Responder.DEFAULT_ERROR_RESPONSE_COLOR
                                + "! Use " + ChatColor.YELLOW + "/eventmanage" + Responder.DEFAULT_ERROR_RESPONSE_COLOR
                                + " for help.");
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
        responder.respond(sender, "/eventmanage load - Loads an event from a file");
        responder.respond(sender, "/eventmanage save - Saves the current event to a file");
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
            suggestions.add("load");
            suggestions.add("save");

            String input = args[0].toLowerCase();
            suggestions.removeIf(s -> !s.startsWith(input));
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("details")) {
                String instructionsMessage = "Use one of these to look at a specific property";
                suggestions.add(instructionsMessage);
                suggestions.add("name");
                suggestions.add("position");
                suggestions.add("world");

                String input = args[1].toLowerCase();
                suggestions.removeIf(s -> !s.startsWith(input) && !s.equalsIgnoreCase(instructionsMessage));
            } else if (args[0].equalsIgnoreCase("edit")) {
                suggestions.add("name");
                suggestions.add("position");
                suggestions.add("world");

                String input = args[1].toLowerCase();
                suggestions.removeIf(s -> !s.startsWith(input));
            } else if (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("save")) {
                String[] storedEvents = com.flappygrant.smp.plugins.eventmanager.Plugin.currentEvent.listStoredEvents();
                String instructionsMessage = "Below are existing events";
                suggestions.add(instructionsMessage);

                for (String identifier : storedEvents) {
                    suggestions.add(identifier);
                }

                String input = args[1].toLowerCase();
                suggestions.removeIf(s -> !s.startsWith(input) && !s.equalsIgnoreCase(instructionsMessage));
            }
        }

        if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("edit")) {
                if (args[1].equalsIgnoreCase("name")) {
                    String instructionsMessage = "Use \"<s>\" to add a space";
                    suggestions.add(instructionsMessage);
                    suggestions.add("e.g. Spleef<s>Games");
                } else if (args[1].equalsIgnoreCase("position")) {
                    String instructionsMessage = "The position uses three arguments for the XYZ coordinates";
                    suggestions.add(instructionsMessage);
                    suggestions.add("e.g. 0.5 100 0.5");
                } else if (args[1].equalsIgnoreCase("world")) {
                    List<String> worldNames = Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());

                    for (String world : worldNames) {
                        suggestions.add(world);
                    }

                    String input = args[1].toLowerCase();
                    suggestions.removeIf(s -> !s.startsWith(input));
                }
            }
        }

        return suggestions;
    }
}
