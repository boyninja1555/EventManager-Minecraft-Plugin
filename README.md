# EventManage Minecraft Plugin

EventManage is a PaperMC plugin designed to simplify event management on your Minecraft server. With a robust command system, administrators can easily configure events using the `/eventmanage` command (with sub-commands like `details`, `start`, `stop`, and `edit`), while players can join events with the `/joinevent` command.

**NOTICE:** For now, only one event can be stored at a time, and server restarts will **wipe the stored event** from memory. Later, the plugin will allow you to load event information from a file, and save it back to a file, which will allow for multiple events to be stored, and you wont have to re-type multiple commands to create an event.

**NOTICE #2:** This plugin must run on a PaperMC server! Unless, of course, you're running a software which is a fork of [PaperMC](https://github.com/PaperMC/Paper). **SPIGOT WILL NOT WORK!!!**

## Key Features

- **Boss Bar Display:** Shows the event name (and how to join) on a boss bar that alternates between red and blue every half-second while the event is active.
- **Teleportation:** Seamlessly teleports players to the event's start location in the specified world.
- **Permission-Based Access:** Admin commands (`eventmanager.admin`) are restricted to administrators, and player commands (`eventmanager.player`) are available to regular players.

## Built With

- [PaperMC API](https://papermc.io)
- [Maven](https://maven.apache.org)
- [Visual Studio Code](https://code.visualstudio.com)

Contributions and feedback are welcome!

## How to Use

1. Download the plugin JAR.
2. Move the JAR to your server's `plugins` folder.
3. Restart/start your server.
4. Run `/eventmanage` (if you are an admin) to see a list of available commands.
5. Run `/eventmanage edit` to see a list of commands related to editing the currently-stored event.
6. Run `/eventmanage start` (if you are finished editing the event details) to start the event, display a message on the bossbar, and allow joining the event.
7. When the event ends, run `/eventmanage stop` to remove the bossbar message, and disable joining the event.
