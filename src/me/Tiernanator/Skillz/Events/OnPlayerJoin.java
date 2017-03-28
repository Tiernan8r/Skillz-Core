package me.Tiernanator.Skillz.Events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.Tiernanator.Skillz.Main;
import me.Tiernanator.Skillz.Skill.Level;
import me.Tiernanator.Skillz.Skill.PlayerSkill;
import me.Tiernanator.Utilities.MetaData.MetaData;

public class OnPlayerJoin implements Listener {

	private static Main plugin;

	public OnPlayerJoin(Main main) {
		plugin = main;
	}

	@EventHandler
	public void onPlayerPickPocketAttempt(PlayerJoinEvent event) {

		Player player = event.getPlayer();
		Level level = null;
		if(!player.hasPlayedBefore()) {
			
			List<PlayerSkill> skills = PlayerSkill.generatePlayerSkills(player);
			level = new Level(player, 0, 0, 0, skills);
			level.save();
			
		} else {
			level = Level.getLevelFromFile(player);
		}
		MetaData.setMetadata(player, "Level", level, plugin);
	}
	
}
