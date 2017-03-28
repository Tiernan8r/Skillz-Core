package me.Tiernanator.Skillz.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Menu.Menu;
import me.Tiernanator.Menu.MenuAction;
import me.Tiernanator.Menu.MenuEntry;
import me.Tiernanator.Skillz.Main;
import me.Tiernanator.Skillz.Skill.Level;
import me.Tiernanator.Skillz.Skill.PlayerSkill;

public class Skills implements CommandExecutor {

	@SuppressWarnings("unused")
	private static Main plugin;

	private ChatColor warning = Colour.ALTERNATE_WARNING.getColour();
	private ChatColor bad = Colour.ALTERNATE_BAD.getColour();
	
	public Skills(Main main) {
		plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(warning + "You cannot use this command");
			return false;
		}
		Player player = (Player) sender;

		Level level = Level.getLevel(player);
		List<PlayerSkill> allSkills = level.getPlayerSkills();
		
		if(allSkills == null) {
			player.sendMessage(bad + "There are no skillz...");
			return false;
		}
		List<MenuEntry> menuEntries = new ArrayList<MenuEntry>();
		
		for(PlayerSkill skill : allSkills) {
			Menu perkTree = skill.generateTree(player);
			String entryName = skill.getName();
			ItemStack entryItem = new ItemStack(Material.SLIME_BALL);
			MenuEntry skillEntry = new MenuEntry(entryName, entryItem, MenuAction.OPEN, perkTree);
			menuEntries.add(skillEntry);
		}
		Menu menu = new Menu("Skills:", menuEntries);
		menu.makeMenu(player);
		
		return false;
	}

}
