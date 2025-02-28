package com.flappygrant.smp.plugins.eventmanager.utils;

import io.papermc.paper.math.Position;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Event {
    private final String BOSSBAR_MESSAGE_SUFFIX = "! Do \"/joinevent\" to join";

    private Plugin pluginInstance;
    private Responder responder;

    private boolean running = false;
    private String name = "Unnamed Event";
    private Position position = Position.FINE_ZERO;
    private String world = "world";

    private BossBar bossBar;
    private int toggleTaskId = -1;

    // Allows this class to access the plugin's information
    public void setPluginInstance(Plugin plugin) {
        pluginInstance = plugin;
    }

    // Allows this class to access the specified responder
    public void setResponder(Responder r) {
        responder = r;
    }

    public void start(Plugin plugin) {
        if (running)
            return;

        running = true;
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
    }

    public void setName(String newName) {
        name = newName;

        if (bossBar != null) {
            bossBar.setTitle(newName + BOSSBAR_MESSAGE_SUFFIX);
        }
    }

    public void setPosition(float x, float y, float z) {
        position = Position.fine(x, y, z);
    }

    public void setWorld(String worldName) {
        world = worldName;
    }

    // Loads a stored event with it's identifier
    public int load(CommandSender sender, String identifier) {
        File dataFolder = new File(pluginInstance.getDataFolder().getAbsolutePath());

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File eventsFolder = new File(dataFolder.getAbsolutePath());

        if (!eventsFolder.exists()) {
            eventsFolder.mkdir();
        }

        File eventFile = new File(eventsFolder, identifier + ".yml");

        if (!eventFile.exists()) {
            responder.respondWithErrorMessage(sender,
                    "The event file " + ChatColor.YELLOW + identifier + ".yml" + Responder.DEFAULT_RESPONSE_COLOR
                            + " doesn't exist! Try saving the current event information with " + ChatColor.YELLOW
                            + "/eventmanage save " + identifier + Responder.DEFAULT_RESPONSE_COLOR + ".");
            return 1;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(eventFile);
        Event newEventData = Event.deserialize(config.getConfigurationSection("event"));
        setName(newEventData.getName());
        setPosition((float) newEventData.getPosition().x(), (float) newEventData.getPosition().y(),
                (float) newEventData.getPosition().z());
        setWorld(newEventData.getWorld());
        return 0;
    }

    // Lists the saved events' identifiers as strings
    public String[] listStoredEvents() {
        File dataFolder = new File(pluginInstance.getDataFolder().getAbsolutePath());

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File eventsFolder = new File(dataFolder.getAbsolutePath());

        if (!eventsFolder.exists()) {
            eventsFolder.mkdir();
        }

        File[] storedEventFiles = eventsFolder.listFiles(new FilenameFilter() {
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".yml");
            }
        });
        String[] storedEvents = new String[storedEventFiles.length];

        for (int i = 0; i < storedEventFiles.length; i++) {
            String filename = storedEventFiles[i].getName();
            storedEvents[i] = filename.substring(0, storedEventFiles[i].getName().length() - 4);
        }
        return storedEvents;
    }

    // Saves (or overrides) an event with it's identifier
    public int save(String identifier) {
        File dataFolder = new File(pluginInstance.getDataFolder().getAbsolutePath());

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File eventsFolder = new File(dataFolder.getAbsolutePath());

        if (!eventsFolder.exists()) {
            eventsFolder.mkdir();
        }

        File eventFile = new File(eventsFolder, identifier + ".yml");

        if (eventFile.exists()) {
            FileWriter writer;

            try {
                writer = new FileWriter(eventFile, false);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                return 1;
            }
        }

        YamlConfiguration config = new YamlConfiguration();
        config.set("event", serialize());

        try {
            config.save(eventFile);
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);

        Map<String, Float> positionMap = new HashMap<>();
        positionMap.put("x", (float) position.x());
        positionMap.put("y", (float) position.y());
        positionMap.put("z", (float) position.z());

        map.put("position", positionMap);
        map.put("world", world);
        return map;
    }

    public static Event deserialize(ConfigurationSection section) {
        String name = section.getString("name");
        String world = section.getString("world");

        ConfigurationSection posSection = section.getConfigurationSection("position");
        float x = (float) posSection.getDouble("x");
        float y = (float) posSection.getDouble("y");
        float z = (float) posSection.getDouble("z");

        Event parsedEventData = new Event();
        parsedEventData.setName(name);
        parsedEventData.setPosition(x, y, z);
        parsedEventData.setWorld(world);
        return parsedEventData;
    }

    public boolean isRunning() {
        return running;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public String getWorld() {
        return world;
    }
}
