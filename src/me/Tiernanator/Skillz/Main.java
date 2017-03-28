package me.Tiernanator.Skillz;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.Skillz.Commands.Commander;
import me.Tiernanator.Skillz.Commands.Skills;
import me.Tiernanator.Skillz.Events.OnPlayerJoin;
import me.Tiernanator.Skillz.Events.PlayerInventoryInteract;
import me.Tiernanator.Skillz.Skill.Level;
import me.Tiernanator.Skillz.Skill.Perk;
import me.Tiernanator.Skillz.Skill.Skill;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		registerEvents();
		registerCommands();
		setPlugin();
		initialise();
		
		for(Player onlinePlayer : getServer().getOnlinePlayers()) {
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
