/**
 * 
 */
package me.Tiernanator.Skillz.Skill;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.Colours.MessageColourer;
import me.Tiernanator.File.ConfigAccessor;
import me.Tiernanator.Skillz.SkillzMain;

/**
 * @author Tiernan
 *
 */
public class Skill {
	
	private static SkillzMain plugin;
	public static void setPlugin(SkillzMain main) {
		plugin = main;
	}

	private static List<Skill> allSkills = new ArrayList<Skill>();

	public static List<Skill> getAllSkills() {
		return allSkills;
	}
	
	public static Skill getSkill(String skillName) {
		
		List<Skill> allSkills = getAllSkills();
		if(allSkills == null || allSkills.isEmpty()) {
			return null;
		}
		
		for(Skill skill : allSkills) {
			
			String name = skill.getName();
			name = MessageColourer.removeColours(name);
			if(skillName.equalsIgnoreCase(name)) {
				return skill;
			}
			
		}
		return null;
	}

	private static void setAllSkills(List<Skill> newSkills) {
		if (newSkills == null) {
			return;
		}
		allSkills = newSkills;
	}

	public static void addSkill(Skill skill) {

		List<Skill> allSkills = new ArrayList<Skill>();
		allSkills = getAllSkills();
		if (skill != null && !allSkills.contains(skill)) {
			allSkills.add(skill);
		}
		setAllSkills(allSkills);

	}

	private String name;
	private int highestLevel;
	private List<Perk> allPerks = new ArrayList<Perk>();

	public Skill(String name, List<Perk> perks) {

		this.allPerks = perks;
		this.name = name;

		addSkill(this);

	}

	public String getName() {
		return this.name;
	}

	public List<Perk> getPerks() {
		return this.allPerks;
	}
	
	public int getTotalLevels() {
		return this.highestLevel;
	}
	
	public static void initialiseSkillsFromFile() {
		
		for(Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
			
			File files = new File(pl.getDataFolder() + File.separator + "skills");
			File[] childrenFiles = files.listFiles();
			if(childrenFiles == null) {
				continue;
			}
			for(File file : childrenFiles) {
				String fileName = file.getName();
				if(!fileName.contains(".yml")) {
					continue;
				}
				String folderName = file.getParentFile().getName();
				
				ConfigAccessor skillAccessor = new ConfigAccessor((JavaPlugin) pl, fileName, folderName);
				
				//If it has no name it isn't a properly formatted skill file
				String skillName = skillAccessor.getConfig().getString("Skill.Name");
				if(skillName == null) {
					continue;
				}
				List<String> perkNames = skillAccessor.getConfig().getStringList("Skill.Perks");
				List<Perk> perks = new ArrayList<Perk>();
				for(String perkName : perkNames) {
					
					String perksName = skillAccessor.getConfig().getString("Perks." + perkName + ".Name");
					String description = skillAccessor.getConfig().getString("Perks." + perkName + ".Description");
					int cost = skillAccessor.getConfig().getInt("Perks." + perkName + ".Cost");
					String parentName = skillAccessor.getConfig().getString("Perks." + perkName + ".Parent");
					if(parentName.equalsIgnoreCase("") || parentName.equalsIgnoreCase("null")) {
						parentName = null;
					}
					Perk parentPerk = null;
					if(parentName != null) {
						 parentPerk = Perk.getPerk(parentName);
					}
					Perk perk = new Perk(parentPerk, cost, perksName, description);
					perks.add(perk);
				}
				Skill skill = new Skill(skillName, perks);
				addSkill(skill);
			}
			
		}
		
	}
	
}
