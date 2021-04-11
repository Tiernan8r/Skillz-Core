package me.Tiernanator.Skillz.Commands;

import me.Tiernanator.Skillz.Skill.Achievement;
import me.Tiernanator.Skillz.Skill.Level;
import me.Tiernanator.Skillz.Skill.PlayerPerk;
import me.Tiernanator.Skillz.Skill.PlayerSkill;
import me.Tiernanator.Skillz.SkillzMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Commander implements CommandExecutor {

	@SuppressWarnings("unused")
	private static SkillzMain plugin;

	public Commander(SkillzMain main) {
		plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		
		if(!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;

		Level level = Level.getLevel(player);
		if(level == null) {
			player.sendMessage("NO LEVEL !!!");
			return false;
		}
		
		List<PlayerSkill> allPlayerSkills = level.getPlayerSkills();
		for(PlayerSkill playerSkill : allPlayerSkills) {
			
			player.sendMessage(playerSkill.getName() + ":" + playerSkill.getLevel() + "/" + playerSkill.getMaxLevel());
			List<PlayerPerk> playerPerks = playerSkill.getPlayerPerks();
			for(PlayerPerk playerPerk : playerPerks) {
				
				player.sendMessage(" - " + playerPerk.getName() + ": " + playerPerk.getDescription() + "(" + playerPerk.isUnlocked() + " &" + playerPerk.getPointsCost() +  ")");
				
			}
			
			
		}
		
//		List<Perk> perks = new ArrayList<Perk>();
//		Perk p = new Perk(null, 0, "Core", "Main", player, true);
//		perks.add(p);
//		for(int i = 0; i < 45; i++) {
//			Perk parent = null;
//			if(i < (perks.size() + 1)) {
//				parent = perks.get(i);
//			}
//			boolean unlocked = true;
//			if(parent != null) {
//				if(parent.isUnlocked()) {
//					double random = Math.round(Math.random() * 100);
//					if(random > 1) {
//						unlocked = true;
//					} else {
//						unlocked = false;
//					}
//				} else {
//					unlocked = false;
//				}
//			}
//			
////			Perk perk = new Perk(parent, i, "&A&LTEST" + i, "&6a test perk", player, unlocked);
////			Perk perk = new Perk(parent, i, "&A&LTEST" + i, "&6a test perk", player, unlocked);
//
//			Perk perk = new Perk(parent, i, "&A&LTEST" + i, "&6a test perk");
//			perks.add(perk);
//		}
//		
//		Skill skill = new Skill("Test", perks);
//		skill.openTree(player);
		
		String name = "WHATEVER";
		List<String> lines = Arrays.asList("Do something or other", "to achieve something or", "something...");
		Achievement achievement = new Achievement(name, lines);
		achievement.sendAchievementMessage(player);
		
		return false;
	}

}
