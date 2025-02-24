package com.flappygrant.smp.plugins.eventmanager.utils;

import org.bukkit.entity.Player;

import io.papermc.paper.math.Position;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

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

        if (current == null || !current.isRunning()) {
            return "NO EVENT";
        }

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
