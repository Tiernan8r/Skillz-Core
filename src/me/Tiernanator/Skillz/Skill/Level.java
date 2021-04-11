/**
 * 
 */
package me.Tiernanator.Skillz.Skill;

import me.Tiernanator.Skillz.SkillzMain;
import me.Tiernanator.Utilities.Colours.MessageColourer;
import me.Tiernanator.Utilities.File.ConfigAccessor;
import me.Tiernanator.Utilities.MetaData.MetaData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tiernan
 *
 */
public class Level {

	private static SkillzMain plugin;
	public static void setPlugin(SkillzMain main) {
		plugin = main;
	}
	
	private static List<Level> allPlayerLevels = new ArrayList<Level>();
	
	private static List<Level> getAllPlayerLevels() {
		return allPlayerLevels;
	}
	
	private static void setAllPlayerLevels(List<Level> allPlayersLevels) {
		
		if(allPlayersLevels == null || allPlayersLevels.isEmpty()) {
			return;
		}
		allPlayerLevels = allPlayersLevels;
		
	}
	
	private static void addLevel(Level level) {
		
		List<Level> allPlayerLevels = new ArrayList<Level>();
		allPlayerLevels = getAllPlayerLevels();
		if(!allPlayerLevels.contains(level)) {
			allPlayerLevels.add(level);
		}
		setAllPlayerLevels(allPlayerLevels);
	}
	
	public static Level getLevel(Player player) {
		
		Level level = (Level) MetaData.getMetadata(player, "Level", plugin);
		if(level != null) {
			return level;
		}
		
		List<Level> allPlayerLevels = getAllPlayerLevels();
		if(allPlayerLevels == null || allPlayerLevels.isEmpty()) {
			return null;
		}
		for(Level playerLevel : allPlayerLevels) {
			Player levelPlayer = playerLevel.getPlayer();
			if(levelPlayer.equals(player)) {
				return playerLevel;
			}
		}
		return null;
	}
	
	private Player player;
	private int level;
	private double levelProgress;
	private int perkPoints;
	private List<PlayerSkill> allPlayerSkills = new ArrayList<PlayerSkill>();
	
	public Level(Player player, int level, double levelProgress, int perkPoints, List<PlayerSkill> playerSkills) {

		this.player = player;
		
		if(levelProgress > 1) {
			levelProgress = 0;
			level++;
		} else if(levelProgress < 0) {
			levelProgress = 0;
		}
		
		this.level = level;
		this.levelProgress = levelProgress;
		this.perkPoints = perkPoints;
		this.allPlayerSkills = playerSkills;
		
		addLevel(this);
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public List<PlayerSkill> getPlayerSkills() {
		return this.allPlayerSkills;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public double getLevelProgress() {
		return this.levelProgress;
	}
	
	public int getPerkPoints() {
		return this.perkPoints;
	}
	
	public void addSkill(PlayerSkill playerSkill) {
		
		List<PlayerSkill> allPlayerSkills = new ArrayList<PlayerSkill>();
		allPlayerSkills = getPlayerSkills();
		if(!allPlayerSkills.contains(playerSkill)) {
			allPlayerSkills.add(playerSkill);
		}
		this.allPlayerSkills = allPlayerSkills;
		
	}
	
	public void save() {
		
		Player player = getPlayer();
		String playerUUID = player.getUniqueId().toString();
		ConfigAccessor levelAccessor = new ConfigAccessor(plugin, playerUUID + ".yml", "levels");
		int level = getLevel();
		double levelProgress = getLevelProgress();
		int perkPoints = getPerkPoints();
		
		levelAccessor.getConfig().set("Main.Level", level);
		levelAccessor.getConfig().set("Main.Progress", levelProgress);
		levelAccessor.getConfig().set("Main.Perks Points", perkPoints);
		List<PlayerSkill> allSkills = getPlayerSkills();
		if(allSkills == null || allSkills.isEmpty()) {
			return;
		}
		List<String> skillNames = new ArrayList<String>();
		for(PlayerSkill playerSkill : allSkills) {
			
			Skill skill = playerSkill.getSkill();
			String skillName = skill.getName();
			skillName = MessageColourer.removeColours(skillName);
			skillNames.add(skillName);
			int skillLevel = playerSkill.getLevel();
			double skillLevelProgress = playerSkill.getLevelProgress();
			levelAccessor.getConfig().set("Skills." + skillName + ".Level", skillLevel);
			levelAccessor.getConfig().set("Skills." + skillName + ".Progress", skillLevelProgress);
			
			List<PlayerPerk> allPlayerPerks = playerSkill.getPlayerPerks();
			if(allPlayerPerks == null || allPlayerPerks.isEmpty()) {
				continue;
			}
			List<String> perkNames = new ArrayList<String>();
			for(PlayerPerk playerPerk : allPlayerPerks) {
				
				Perk perk = playerPerk.getPerk();
				String perkName = perk.getName();
				perkNames.add(perkName);
				boolean isUnlocked = playerPerk.isUnlocked();
				levelAccessor.getConfig().set("Skills." + skillName + "." + perkName + ".Unlocked", isUnlocked);
				
			}
			levelAccessor.getConfig().set("Skills." + skillName + ".Perks", perkNames);
			
		}
		levelAccessor.getConfig().set("Main.Skills", skillNames);
		levelAccessor.saveConfig();
		
	}
	
	public static Level getLevelFromFile(Player player) {
		
		String playerUUID = player.getUniqueId().toString();
		
		ConfigAccessor levelAccessor = new ConfigAccessor(plugin, playerUUID + ".yml", "levels");
		int levels = levelAccessor.getConfig().getInt("Main.Level");
		double levelProgress = levelAccessor.getConfig().getDouble("Main.Progress");
		int perkPoints = levelAccessor.getConfig().getInt("Main.Perk Points");
		List<PlayerSkill> playerSkills = new ArrayList<PlayerSkill>();
		List<String> skillNames = levelAccessor.getConfig().getStringList("Main.Skills");
		for(String skillName : skillNames) {
			
			Skill skill = Skill.getSkill(skillName);
			PlayerSkill playerSkill = PlayerSkill.getPlayerSkill(player); //This has to be functional...
			if(skill != null && !playerSkills.contains(skill)) {
				playerSkills.add(playerSkill);
			}
			
		}
		
		Level level = new Level(player, levels, levelProgress, perkPoints, playerSkills);
		return level;
	}
	
	
}
