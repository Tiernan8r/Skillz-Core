package me.Tiernanator.Skillz.Skill;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.Colours.MessageColourer;
import me.Tiernanator.File.ConfigAccessor;
import me.Tiernanator.Skillz.Main;

public class Perk {

	private static Main plugin;
	public static void setPlugin(Main main) {
		plugin = main;
	}

	private static List<Perk> allPerks = new ArrayList<Perk>();

	public static List<Perk> getAllPerks() {
		return allPerks;
	}

	public static Perk getPerk(String perkName) {

		List<Perk> allPerks = getAllPerks();
		if (allPerks == null || allPerks.isEmpty()) {
			return null;
		}

		for (Perk perk : allPerks) {

			String name = perk.getName();
			name = MessageColourer.removeColours(name);
			if (perkName.equalsIgnoreCase(name)) {
				return perk;
			}

		}
		return null;
	}

	private static void setAllPerks(List<Perk> newPerks) {
		if (newPerks == null) {
			return;
		}
		allPerks = newPerks;
	}

	public static void addPerk(Perk perk) {

		List<Perk> allPerks = new ArrayList<Perk>();
		allPerks = getAllPerks();
		if (perk != null && !allPerks.contains(perk)) {
			allPerks.add(perk);
		}
		setAllPerks(allPerks);

	}

	private Perk parentPerk;
	private int pointsCost;
	private String name;
	private String description;

	public Perk(Perk parentPerk, int pointsCost, String name,
			String description) {

		this.parentPerk = parentPerk;
		this.pointsCost = pointsCost;
		name = MessageColourer.parseMessage(name, ChatColor.WHITE);
		this.name = name;
		description = MessageColourer.parseMessage(description,
				ChatColor.WHITE);
		this.description = description;

	}

	public Perk getParentPerk() {
		return this.parentPerk;
	}

	public int getPointsCost() {
		return this.pointsCost;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public static void loadPerksFromFile() {

		for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {

			File files = new File(pl.getDataFolder() + "/skills");
			File[] childrenFiles = files.listFiles();
			if (childrenFiles == null) {
				continue;
			}
			for (File file : childrenFiles) {
				String fileName = file.getName();
				if (!fileName.contains(".yml")) {
					continue;
				}
				String folderName = "\\" + file.getParentFile().getName();

				ConfigAccessor perkAcessor = new ConfigAccessor((JavaPlugin) pl,
						fileName, folderName);

				List<String> perkNames = perkAcessor.getConfig()
						.getStringList("Skill.Perks");
				for (String perkName : perkNames) {

					String perksName = perkAcessor.getConfig()
							.getString("Perks." + perkName + ".Name");
					String description = perkAcessor.getConfig()
							.getString("Perks." + perkName + ".Description");
					int cost = perkAcessor.getConfig()
							.getInt("Perks." + perkName + ".Cost");
					String parentName = perkAcessor.getConfig()
							.getString("Perks." + perkName + ".Parent");
					if (parentName.equalsIgnoreCase("")
							|| parentName.equalsIgnoreCase("null")) {
						parentName = null;
					}
					Perk parentPerk = null;
					if (parentName != null) {
						parentPerk = Perk.getPerk(parentName);
					}
					Perk perk = new Perk(parentPerk, cost, perksName,
							description);
					addPerk(perk);
				}
			}
		}
		
	}

}
