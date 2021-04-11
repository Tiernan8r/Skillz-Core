package me.Tiernanator.Skillz.Events;

import me.Tiernanator.Skillz.Skill.Level;
import me.Tiernanator.Skillz.SkillzMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {

	@SuppressWarnings("unused")
	private static SkillzMain plugin;

	public OnPlayerLeave(SkillzMain main) {
		plugin = main;
	}

	@EventHandler
	public void onPlayerPickPocketAttempt(PlayerQuitEvent event) {

		Player player = event.getPlayer();
		Level level = Level.getLevel(player);
		level.save();
	}
	
}
