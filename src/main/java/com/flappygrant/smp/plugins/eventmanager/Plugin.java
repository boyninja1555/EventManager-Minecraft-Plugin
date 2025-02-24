package com.flappygrant.smp.plugins.eventmanager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.flappygrant.smp.plugins.eventmanager.commands.*;
import com.flappygrant.smp.plugins.eventmanager.utils.Event;
import com.flappygrant.smp.plugins.eventmanager.utils.EventExpansion;
import com.flappygrant.smp.plugins.eventmanager.utils.listeners.BossBarJoinListener;

public class Plugin extends JavaPlugin {
    private Event currentEvent;

    @Override
    public void onEnable() {
        currentEvent = new Event();

        Command_eventmanage commandEventmanageExecutor = new Command_eventmanage();
        commandEventmanageExecutor.setPluginInstance(this);
        commandEventmanageExecutor.setCurrentEvent(currentEvent);

        getCommand("eventmanage").setExecutor(commandEventmanageExecutor);
        getCommand("eventmanage").setTabCompleter(commandEventmanageExecutor);

        Command_joinevent commandJoineventExecutor = new Command_joinevent();
        commandJoineventExecutor.setCurrentEvent(currentEvent);

        getCommand("joinevent").setExecutor(commandJoineventExecutor);

        Bukkit.getPluginManager().registerEvents(new BossBarJoinListener(currentEvent), this);

        if (new EventExpansion().register()) {
            getLogger().info("EventExpansion registered successfully!");
        } else {
            getLogger().severe("Failed to register EventExpansion!");
        }
    }

    @Override
    public void onDisable() {
    }
}
