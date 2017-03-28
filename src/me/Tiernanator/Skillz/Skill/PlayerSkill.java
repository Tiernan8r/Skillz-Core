package me.Tiernanator.Skillz.Skill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Tiernanator.Colours.MessageColourer;
import me.Tiernanator.Menu.Menu;
import me.Tiernanator.Menu.MenuAction;
import me.Tiernanator.Menu.MenuEntry;
import me.Tiernanator.Utilities.Items.Item;

public class PlayerSkill  extends Skill {
	
	private Player player;
	private Skill skill;
	private int level;
	private double progress;
	private List<PlayerPerk> playerPerks;
	
	public PlayerSkill(Player player, Skill skill, int level, double progress, List<PlayerPerk> playerPerks) {
		
		super(skill.getName(), skill.getPerks());
		
		this.player = player;
		this.skill = skill;
		this.level = level;
		this.progress = progress;
		this.playerPerks = playerPerks;
		
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Skill getSkill() {
		return this.skill;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public double getLevelProgress() {
		return this.progress;
	}
	
	public List<PlayerPerk> getPlayerPerks() {
		return this.playerPerks;
	}
	
	public static PlayerSkill getPlayerSkill(Player player) {
		
//		Skill skill = Skill.
		
		return null;
		
	}
	
	public Menu generateTree(Player player) {

		List<MenuEntry> menuEntries = new ArrayList<MenuEntry>();
		List<PlayerPerk> allPlayerPerks = getPlayerPerks();
		if(allPlayerPerks == null || allPlayerPerks.isEmpty()) {
			return null;
		}
		for (PlayerPerk playerPerk : allPlayerPerks) {

			PlayerPerk parentPlayerPerk = playerPerk.getParent();
			boolean unlocked = playerPerk.isUnlocked();
			boolean parentUnlocked = true;
			if(parentPlayerPerk != null) {
				parentUnlocked = parentPlayerPerk.isUnlocked();
			}
			ItemStack item = null;
			MenuEntry menuEntry = null;
			if(parentUnlocked && unlocked) {
				item = new ItemStack(Material.DIAMOND);
				Item.addLore(item, playerPerk.getDescription());
				menuEntry = new MenuEntry(playerPerk.getName(), item, MenuAction.NOTHING, null); 
			} else if(parentUnlocked && !unlocked) {
				item = new ItemStack(Material.COAL);
				Item.addLore(item, playerPerk.getDescription());
				menuEntry = new MenuEntry(playerPerk.getName(), item, MenuAction.NOTHING, null); 
			} else if(!parentUnlocked && !unlocked) {
				item = new ItemStack(Material.SULPHUR);
				String name = MessageColourer.removeColours(playerPerk.getName());
				String description = MessageColourer.removeColours(playerPerk.getDescription());
				Item.addLore(item, ChatColor.RESET + "" + ChatColor.MAGIC + description);
				menuEntry = new MenuEntry(ChatColor.MAGIC + name, item, MenuAction.NOTHING, null); 
			} else {
				item = new ItemStack(Material.BARRIER);
				Item.addLore(item, playerPerk.getDescription());
				menuEntry = new MenuEntry(playerPerk.getName(), item, MenuAction.NOTHING, null); 
			}
			Item.addLore(item, ChatColor.WHITE + "Cost: " + playerPerk.getPointsCost());
			if(parentPlayerPerk != null) {
				Item.addLore(item, "Parent: " + parentPlayerPerk.getName());
			}
			
			menuEntries.add(menuEntry);

		}

		Menu menu = new Menu(getName(), menuEntries, 54);
		return menu;
	}

	public void openTree(Player player) {

		Menu menu = generateTree(player);
		menu.makeMenu(player);

	}
	
	public static List<PlayerSkill> generatePlayerSkills(Player player) {
		
		List<PlayerSkill> playerSkills = new ArrayList<PlayerSkill>();
		List<Skill> allSkills = Skill.getAllSkills();
		for(Skill skill : allSkills) {
			
			List<PlayerPerk> playerPerks = new ArrayList<PlayerPerk>();
			List<Perk> allPerks = skill.getPerks();
			for(Perk perk : allPerks) {
				
				PlayerPerk parent = null;
				if(perk.getParentPerk() != null) {
					
					parent = PlayerPerk.getPlayerPerk(perk.getParentPerk());
					
				}
				
				PlayerPerk playerPerk = new PlayerPerk(player, perk, false, parent);
				playerPerks.add(playerPerk);
				
			}
			
			PlayerSkill playerSkill = new PlayerSkill(player, skill, 1, 0.0, playerPerks);
			playerSkills.add(playerSkill);
			
		}
		
		
		return playerSkills;
	}
	
}
