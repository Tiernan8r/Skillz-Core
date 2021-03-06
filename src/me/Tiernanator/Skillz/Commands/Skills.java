package me.Tiernanator.Skillz.Commands;

import me.Tiernanator.Skillz.Skill.Level;
import me.Tiernanator.Skillz.Skill.PlayerSkill;
import me.Tiernanator.Skillz.SkillzMain;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.Menu.Menu;
import me.Tiernanator.Utilities.Menu.MenuAction;
import me.Tiernanator.Utilities.Menu.MenuEntry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Skills implements CommandExecutor {

	@SuppressWarnings("unused")
	private static SkillzMain plugin;

	private ChatColor warning = Colour.ALTERNATE_WARNING.getColour();
	private ChatColor bad = Colour.ALTERNATE_BAD.getColour();
	
	public Skills(SkillzMain main) {
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
