package me.Tiernanator.Skillz;

import me.Tiernanator.Skillz.Commands.Commander;
import me.Tiernanator.Skillz.Commands.Skills;
import me.Tiernanator.Skillz.Events.OnPlayerJoin;
import me.Tiernanator.Skillz.Events.PlayerInventoryInteract;
import me.Tiernanator.Skillz.Skill.Level;
import me.Tiernanator.Skillz.Skill.Perk;
import me.Tiernanator.Skillz.Skill.Skill;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillzMain extends JavaPlugin {

	//Gets called when the plugin loads
    @Override
    public void onEnable() {
    	//Add the custom events to the handler
        registerEvents();
        //Add the custom commands to the handler
        registerCommands();
        //Sets the plugin variable for various classes to refer to this class instance
        setPlugin();
        //Sets up perks and skills that already exist
        initialise();

        //Iterate over all players already online and pretend that they just joined the server
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(onlinePlayer, "");
            getServer().getPluginManager().callEvent(playerJoinEvent);
        }

    }

    @Override
    public void onDisable() {

    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerInventoryInteract(this), this);
        pm.registerEvents(new OnPlayerJoin(this), this);
    }

    private void registerCommands() {
        getCommand("test").setExecutor(new Commander(this));
        getCommand("skills").setExecutor(new Skills(this));
    }

    private void setPlugin() {
        Perk.setPlugin(this);
        Skill.setPlugin(this);
        Level.setPlugin(this);
    }

    private void initialise() {

        Perk.loadPerksFromFile();
        Skill.initialiseSkillsFromFile();

    }
}
