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
        NoPermission
    };

    private Map<ErrorType, String> errorTypeToMessageMap = new HashMap<ErrorType, String>() {
        {
            put(ErrorType.NotPlayer, "You must be a player to do this!");
            put(ErrorType.NoPermission, "You do not have permission to do this!");
        }
    };
    private String messagePrefix = ChatColor.DARK_GRAY
            + "["
            + ChatColor.YELLOW
            + "EventManager"
            + ChatColor.DARK_GRAY
            + "]";

    public void respondToPlayer(CommandSender sender, String message) {
        if (!(sender instanceof Player)) {
            this.respondWithError(sender, ErrorType.NotPlayer);
            return;
        }

        sender.sendMessage(this.messagePrefix + " " + ChatColor.AQUA + message);
    }

    public void respond(CommandSender sender, String message) {
        sender.sendMessage(this.messagePrefix + " " + ChatColor.AQUA + message);
    }

    public void respondWithError(CommandSender sender, ErrorType errorType) {
        sender.sendMessage(this.messagePrefix + " " + ChatColor.RED + errorTypeToMessageMap.get(errorType));
    }

    public void respondWithErrorMessage(CommandSender sender, String errorMessage) {
        sender.sendMessage(this.messagePrefix + " " + ChatColor.RED + errorMessage);
    }
}
