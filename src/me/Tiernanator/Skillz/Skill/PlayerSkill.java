package me.Tiernanator.Skillz.Skill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Tiernanator.Utilities.Colours.MessageColourer;
import me.Tiernanator.Utilities.Items.ItemUtility;
import me.Tiernanator.Utilities.Menu.Menu;
import me.Tiernanator.Utilities.Menu.MenuAction;
import me.Tiernanator.Utilities.Menu.MenuEntry;

// A skill that the player can achieve
public class PlayerSkill  extends Skill {

	// the player the skill is associated with
	private Player player;
	// the actual skill tree
	private Skill skill;
	// the level that the player has for that skill
	private int level;
	// the progress the player has between levels (lke a percent)
	private double progress;
	// a list of all unlocked perks for the skill
	private List<PlayerPerk> playerPerks;

	//innitialiser
	public PlayerSkill(Player player, Skill skill, int level, double progress, List<PlayerPerk> playerPerks) {
		
		super(skill.getName(), skill.getPerks());
		
		this.player = player;
		this.skill = skill;
		this.level = level;
		this.progress = progress;
		this.playerPerks = playerPerks;
		
	}

	//getters

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

	//TODO make it return the player skill based off of the provided player => need to keep track via a HashMap
	public static PlayerSkill getPlayerSkill(Player player) {
		
		return null;
		
	}

	//Create a menu object base skill tree for the players
	public Menu generateTree(Player player) {

		//Create a new list for all the entries in the menu
		List<MenuEntry> menuEntries = new ArrayList<MenuEntry>();
		//Get all the perks for the player
		List<PlayerPerk> allPlayerPerks = getPlayerPerks();
		//If there is no skill tree to show then don't show it
		if(allPlayerPerks == null || allPlayerPerks.isEmpty()) {
			return null;
		}
		//Iterate over the perks in the skill tree
		for (PlayerPerk playerPerk : allPlayerPerks) {

			//Get the parent of the current perk
			PlayerPerk parentPlayerPerk = playerPerk.getParent();
			//Get whether this perk is unlocked or not
			boolean unlocked = playerPerk.isUnlocked();
			// Assume the parent is unlocked
			boolean parentUnlocked = true;
			//If there is no parent, then the perk isn't modified by the parent status
			if(parentPlayerPerk != null) {
				//otherwise the get whether the parent is unlocked or not
				parentUnlocked = parentPlayerPerk.isUnlocked();
			}
			//Set two empty objects
			ItemStack item = null;
			MenuEntry menuEntry = null;
			//If both the parent and current perk are unlocked
			if(parentUnlocked && unlocked) {
				//The item to represent the perk will be a diamond
				item = new ItemStack(Material.DIAMOND);
				//Set the item lore to be the perk description
				ItemUtility.addLore(item, playerPerk.getDescription());
				//Create a menu entry with this item, making it don nothing when clicked
				menuEntry = new MenuEntry(playerPerk.getName(), item, MenuAction.NOTHING, null);
				//If the parent is unlocked but the perk isn't
			} else if(parentUnlocked && !unlocked) {
				//Create a coal item for this perk
				item = new ItemStack(Material.COAL);
				//Set the description as before
				ItemUtility.addLore(item, playerPerk.getDescription());
				menuEntry = new MenuEntry(playerPerk.getName(), item, MenuAction.NOTHING, null);
				//If the parent isnt unlocked, then this perk can't be unlocked yet
			} else if(!parentUnlocked && !unlocked) {
				//Create a gunpowder item for this entry
				item = new ItemStack(Material.GUNPOWDER);
				//Create a name without any colouring
				String name = MessageColourer.removeColours(playerPerk.getName());
				//Create the description without any colouring
				String description = MessageColourer.removeColours(playerPerk.getDescription());
				//Add the lore with the MAGIC colour to garble the text
				ItemUtility.addLore(item, ChatColor.RESET + "" + ChatColor.MAGIC + description);
				//Create the menu entry with garbled text for the name as well
				menuEntry = new MenuEntry(ChatColor.MAGIC + name, item, MenuAction.NOTHING, null);
				//if the parent isnt unlocked but this perk is, it's clearly an error, so use the barrier
				//item to signify that
			} else {
				//Create item, lore and entry as before
				item = new ItemStack(Material.BARRIER);
				ItemUtility.addLore(item, playerPerk.getDescription());
				menuEntry = new MenuEntry(playerPerk.getName(), item, MenuAction.NOTHING, null); 
			}
			//Add a lore entry to the items based off of their cost
			ItemUtility.addLore(item, ChatColor.WHITE + "Cost: " + playerPerk.getPointsCost());
			//If the perk has a parent, add it's name to the items lore
			if(parentPlayerPerk != null) {
				ItemUtility.addLore(item, "Parent: " + parentPlayerPerk.getName());
			}

			//Add the menu entry to the menu
			menuEntries.add(menuEntry);

		}

		//create the menu with 54 entries (a double chest)
		Menu menu = new Menu(getName(), menuEntries, 54);
		return menu;
	}

	//Generates the skill tree for the player
	public void openTree(Player player) {

		Menu menu = generateTree(player);
		//display the menu
		menu.makeMenu(player);

	}

	//Static function to make player skills from text files
	public static List<PlayerSkill> generatePlayerSkills(Player player) {

		//Create a list of all player skills to add
		List<PlayerSkill> playerSkills = new ArrayList<PlayerSkill>();
		//iterate over all the existing skills
		for(Skill skill : Skill.getAllSkills()) {

			//Create an array of player perks
			List<PlayerPerk> playerPerks = new ArrayList<PlayerPerk>();
			//Iterate over perks for the skill
			for(Perk perk : skill.getPerks()) {

				//Set teh parentt to be null by default
				PlayerPerk parent = null;
				//If the perk has a parent, set ut
				if(perk.getParentPerk() != null) {
					
					parent = PlayerPerk.getPlayerPerk(perk.getParentPerk());
					
				}
				//Create a new instance of PlayerPerk for the perk
				PlayerPerk playerPerk = new PlayerPerk(player, perk, false, parent);
				// add the perk to the list of all perks for the skill
				playerPerks.add(playerPerk);
				
			}
			//Create a new player skill instance for the skill
			PlayerSkill playerSkill = new PlayerSkill(player, skill, 1, 0.0, playerPerks);
			//Add the player skill to the list of skills
			playerSkills.add(playerSkill);
			
		}
		//Return the list of all player skills
		return playerSkills;
	}
	
}
