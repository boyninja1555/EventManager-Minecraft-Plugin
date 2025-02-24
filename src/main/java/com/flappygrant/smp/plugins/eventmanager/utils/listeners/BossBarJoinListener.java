package com.flappygrant.smp.plugins.eventmanager.utils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.flappygrant.smp.plugins.eventmanager.utils.Event;

public class BossBarJoinListener implements Listener {
    private final Event event;

    public BossBarJoinListener(Event event) {
        this.event = event;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();

        if (event.isRunning() && event.getBossBar() != null) {
            event.getBossBar().addPlayer(player);
        }
    }
}