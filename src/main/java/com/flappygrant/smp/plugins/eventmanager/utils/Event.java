package com.flappygrant.smp.plugins.eventmanager.utils;

import io.papermc.paper.math.Position;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Event {
    private final String BOSSBAR_MESSAGE_SUFFIX = "! Do \"/joinevent\" to join";

    private boolean running = false;
    private String name = "Unnamed Event";
    private Position position = Position.FINE_ZERO;
    private String world = "world";

    private BossBar bossBar;
    private int toggleTaskId = -1;

    private static Event currentEvent;

    public static Event getCurrentEvent() {
        return currentEvent;
    }

    public static void clearCurrentEvent() {
        currentEvent = null;
    }

    public void start(Plugin plugin) {
        if (running)
            return;

        running = true;
        currentEvent = this;
        bossBar = Bukkit.createBossBar(name + BOSSBAR_MESSAGE_SUFFIX, BarColor.RED, BarStyle.SOLID);

        // Adds the bossbar to all online players
        // An event listener handles new players
        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }

        toggleTaskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (bossBar == null)
                return;
            if (bossBar.getColor() == BarColor.RED) {
                bossBar.setColor(BarColor.BLUE);
            } else {
                bossBar.setColor(BarColor.RED);
            }
        }, 0L, 10L).getTaskId();
    }

    public void stop(Plugin plugin) {
        if (!running)
            return;

        running = false;

        if (toggleTaskId != -1) {
            Bukkit.getScheduler().cancelTask(toggleTaskId);
            toggleTaskId = -1;
        }

        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }

        currentEvent = null;
    }

    public void setName(String newName) {
        this.name = newName;

        if (bossBar != null) {
            bossBar.setTitle(newName + BOSSBAR_MESSAGE_SUFFIX);
        }
    }

    public void setPosition(float x, float y, float z) {
        this.position = Position.fine(x, y, z);
    }

    public void setWorld(String worldName) {
        this.world = worldName;
    }

    public boolean isRunning() {
        return this.running;
    }

    public BossBar getBossBar() {
        return this.bossBar;
    }

    public String getName() {
        return this.name;
    }

    public Position getPosition() {
        return this.position;
    }

    public String getWorld() {
        return this.world;
    }
}
