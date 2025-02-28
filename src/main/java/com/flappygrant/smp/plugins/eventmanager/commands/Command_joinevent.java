package com.flappygrant.smp.plugins.eventmanager.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.flappygrant.smp.plugins.eventmanager.Plugin;
import com.flappygrant.smp.plugins.eventmanager.utils.Responder;
import io.papermc.paper.math.Position;

@SuppressWarnings("deprecation")
public class Command_joinevent implements CommandExecutor {
    Responder responder = new Responder();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Rules out people who are not players
        if (!sender.hasPermission("eventmanager.player")) {
            responder.respondWithError(sender, Responder.ErrorType.NoPermission);
            return false;
        }

        // Come on, how does a console join an event?
        if (!(sender instanceof Player)) {
            responder.respondWithError(sender, Responder.ErrorType.NotPlayer);
            return false;
        }

        // Stops people from joining the event if it is not active
        // This is important if an administrator is still configuring the event
        if (!Plugin.currentEvent.isRunning()) {
            responder.respondWithErrorMessage(sender,
                    "No event is active right now! Please try again later when one is announced.");
            return false;
        }

        // We know the sender is a player, so we start the actual command
        Player player = (Player) sender;
        World eventWorld = Bukkit.getWorld(Plugin.currentEvent.getWorld());

        // Did the administrator really misspell the world name?
        // I hardly think that's possible!
        // Still, this is neccessary for convenience
        if (eventWorld == null) {
            responder.respondWithErrorMessage(sender,
                    "The event world " + ChatColor.YELLOW + Plugin.currentEvent.getWorld()
                            + Responder.DEFAULT_ERROR_RESPONSE_COLOR + " is not loaded!");
            return false;
        }

        Position pos = Plugin.currentEvent.getPosition();
        Location targetLocation = new Location(eventWorld, pos.x(), pos.y(), pos.z());

        // Man, it took this long to actually do any teleporting?
        // Plugin coding is hard!
        player.teleport(targetLocation);
        responder.respond(sender, "Successfully joined the event!");
        return true;
    }
}
