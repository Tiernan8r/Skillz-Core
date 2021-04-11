package me.Tiernanator.Skillz.Events;

import me.Tiernanator.Skillz.SkillzMain;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Utilities.Players.PlayerTime;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInventoryInteract implements Listener {

	@SuppressWarnings("unused")
	private static SkillzMain plugin;

	private ChatColor warning = Colour.WARNING.getColour();
	private ChatColor highlight = Colour.HIGHLIGHT.getColour();
	
	public PlayerInventoryInteract(SkillzMain main) {
		plugin = main;
	}

	@EventHandler
	public void onPlayerPickPocketAttempt(PlayerInteractAtEntityEvent event) {

		if (event.isCancelled()) {
			return;
		}
		
		Player thief = event.getPlayer();
		Entity entity = event.getRightClicked();
		
		if(!(entity instanceof Player)) {
			return;
		}
		Player robbed = (Player) entity;
		
		if(!thief.isSneaking()) {
			return;
		}
		
		//get the time intervals between clicks, and set the previous time to now
//		long currentTime =  System.currentTimeMillis() / 1000L; 
		long currentTime =  System.currentTimeMillis(); 
			
		long previousTime = 0;
		PlayerTime playerTime = new PlayerTime(thief);
		if(playerTime.hasPlayerTime()) {
			previousTime = playerTime.getPreviousPlayerTime();
		}
		playerTime.addPlayerTime(currentTime);
		//determine the difference, if it is less than 4, don't call the event
		long timeDifference = currentTime - previousTime;
		
		if((timeDifference / 4.0) < 0.25) {
			return;
		}
		
		double chance = Math.random() * 100;
		
		if(chance > 5) {
			thief.sendMessage(warning + "You have been caught pickpocketing.");
			robbed.sendMessage(highlight + thief.getName() + warning + " attempted to pick your pocket!");
			return;
		}
		
		Inventory inventory = robbed.getInventory();
		thief.openInventory(inventory);
		
	}
	
}
