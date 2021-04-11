package me.Tiernanator.Skillz.Events;

import me.Tiernanator.Skillz.Skill.Level;
import me.Tiernanator.Skillz.Skill.PlayerSkill;
import me.Tiernanator.Skillz.SkillzMain;
import me.Tiernanator.Utilities.MetaData.MetaData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class OnPlayerJoin implements Listener {

	private static SkillzMain plugin;

	public OnPlayerJoin(SkillzMain main) {
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
