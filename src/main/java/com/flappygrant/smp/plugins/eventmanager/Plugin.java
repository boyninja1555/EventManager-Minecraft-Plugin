package com.flappygrant.smp.plugins.eventmanager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.flappygrant.smp.plugins.eventmanager.commands.*;
import com.flappygrant.smp.plugins.eventmanager.utils.Event;
import com.flappygrant.smp.plugins.eventmanager.utils.Responder;
import com.flappygrant.smp.plugins.eventmanager.utils.listeners.BossBarJoinListener;

public class Plugin extends JavaPlugin {
    public static Event currentEvent;

    private Responder responder;

    @Override
    public void onEnable() {
        responder = new Responder();

        currentEvent = new Event();
        currentEvent.setPluginInstance(this);
        currentEvent.setResponder(responder);

        // Sets up the administrator commands
        Command_eventmanage commandEventmanageExecutor = new Command_eventmanage();
        commandEventmanageExecutor.setPluginInstance(this);
        commandEventmanageExecutor.setResponder(responder);

        getCommand("eventmanage").setExecutor(commandEventmanageExecutor);
        getCommand("eventmanage").setTabCompleter(commandEventmanageExecutor);

        // Sets up player commands
        Command_joinevent commandJoineventExecutor = new Command_joinevent();

        getCommand("joinevent").setExecutor(commandJoineventExecutor);

        // Allows new players too see the event bossbar
        Bukkit.getPluginManager().registerEvents(new BossBarJoinListener(currentEvent), this);
    }

    @Override
    public void onDisable() {
    }
}
