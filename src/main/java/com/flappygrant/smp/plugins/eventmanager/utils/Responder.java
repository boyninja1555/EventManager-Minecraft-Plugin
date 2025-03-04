package com.flappygrant.smp.plugins.eventmanager.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class Responder {
    public static enum ErrorType {
        NotPlayer,
        NoPermission,
        RequiresIdentifier
    };

    // Provides a way to convert the error type to a sendable message
    private Map<ErrorType, String> errorTypeToMessageMap = new HashMap<ErrorType, String>() {
        {
            put(ErrorType.NotPlayer, "You must be a player to do this!");
            put(ErrorType.NoPermission, "You do not have permission to do this!");
            put(ErrorType.RequiresIdentifier, "You must input an identifier next! (e.g.) coolevent");
        }
    };

    private String messagePrefix = ChatColor.DARK_GRAY
            + "["
            + ChatColor.YELLOW
            + "EventManager"
            + ChatColor.DARK_GRAY
            + "]";
    
    public static final String DEFAULT_RESPONSE_COLOR = ChatColor.AQUA.toString();
    public static final String DEFAULT_ERROR_RESPONSE_COLOR = ChatColor.RED.toString();

    // Player-specific responding
    // An extension on the normal responding behavior but for if the message is meant only for players
    public void respondToPlayer(CommandSender sender, String message) {
        if (!(sender instanceof Player)) {
            respondWithError(sender, ErrorType.NotPlayer);
            return;
        }

        sender.sendMessage(messagePrefix + " " + DEFAULT_RESPONSE_COLOR + message);
    }

    // Responds with a message
    // Formatted for the plugin
    public void respond(CommandSender sender, String message) {
        sender.sendMessage(messagePrefix + " " + DEFAULT_RESPONSE_COLOR + message);
    }

    // Responds as an error
    // Only accepts error types
    public void respondWithError(CommandSender sender, ErrorType errorType) {
        sender.sendMessage(messagePrefix + " " + DEFAULT_ERROR_RESPONSE_COLOR + errorTypeToMessageMap.get(errorType));
    }

    // Responds as a specific error message
    // Only accepts a string
    public void respondWithErrorMessage(CommandSender sender, String errorMessage) {
        sender.sendMessage(messagePrefix + " " + DEFAULT_ERROR_RESPONSE_COLOR + errorMessage);
    }
}
