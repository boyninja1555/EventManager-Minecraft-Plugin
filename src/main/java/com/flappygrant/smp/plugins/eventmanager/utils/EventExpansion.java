package com.flappygrant.smp.plugins.eventmanager.utils;

import org.bukkit.entity.Player;

import io.papermc.paper.math.Position;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

// WARNING: The expansion didn't work when last tested!
// TODO: Make the PlaceholderAPI expansion actually work
public class EventExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "current_event";
    }

    @Override
    public String getAuthor() {
        return "boyninja15_";
    }

    @Override
    public String getVersion() {
        return "1.1-SNAPSHOT";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        Event current = Event.getCurrentEvent();

        // Shows the text "NO EVENT" when the event is of
        if (current == null || !current.isRunning()) {
            return "NO EVENT";
        }

        // Handles different placeholders
        if (params.equalsIgnoreCase("name")) {
            return current.getName();
        } else if (params.equalsIgnoreCase("pos")) {
            Position pos = current.getPosition();
            return pos.x() + " " + pos.y() + " " + pos.z();
        } else if (params.equalsIgnoreCase("world")) {
            return current.getWorld();
        }

        return null;
    }
}
