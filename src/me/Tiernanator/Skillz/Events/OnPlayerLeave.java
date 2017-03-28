package me.Tiernanator.Skillz.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Tiernanator.Skillz.Main;
import me.Tiernanator.Skillz.Skill.Level;

public class OnPlayerLeave implements Listener {

	@SuppressWarnings("unused")
	private static Main plugin;

	public OnPlayerLeave(Main main) {
		plugin = main;
	}

	@EventHandler
	public void onPlayerPickPocketAttempt(PlayerQuitEvent event) {

		Player player = event.getPlayer();
		Level level = Level.getLevel(player);
		level.save();
	}
	
}
