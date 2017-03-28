package me.Tiernanator.Skillz.Skill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class PlayerPerk extends Perk {
	
	private static List<PlayerPerk> allPlayerPerks = new ArrayList<PlayerPerk>();

	private static List<PlayerPerk> getAllPlayerPerks() {
		return allPlayerPerks;
	}
	
//	private static PlayerPerk getPlayerPerk(String playerPerkName) {
//		
//		List<PlayerPerk> allPlayerPerks = getAllPlayerPerks();
//		if(allPlayerPerks == null || allPlayerPerks.isEmpty()) {
//			return null;
//		}
//		
//		for(PlayerPerk playerPerk : allPlayerPerks) {
//			
//			String name = playerPerk.getName();
//			name = MessageColourer.removeColours(name);
//			if(playerPerkName.equalsIgnoreCase(name)) {
//				return playerPerk;
//			}
//			
//		}
//		return null;
//	}

	private static void setAllPlayerPerks(List<PlayerPerk> newPlayerPerks) {
		if (newPlayerPerks == null) {
			return;
		}
		allPlayerPerks = newPlayerPerks;
	}

	private static void addPlayerPerk(PlayerPerk playerPerk) {

		List<PlayerPerk> allPlayerPerks = new ArrayList<PlayerPerk>();
		allPlayerPerks = getAllPlayerPerks();
		if (playerPerk != null && !allPlayerPerks.contains(playerPerk)) {
			allPlayerPerks.add(playerPerk);
		}
		setAllPlayerPerks(allPlayerPerks);

	}
	
	private Player player;
	private Perk perk;
	private boolean unlocked;
	private PlayerPerk parent;
	
	public PlayerPerk(Player player, Perk perk, boolean isUnlocked, PlayerPerk parent) {
		
		super(perk.getParentPerk(), perk.getPointsCost(), perk.getName(), perk.getDescription());
		
		this.player = player;
		this.perk = perk;
		this.unlocked = isUnlocked;
		this.parent = parent;
		
		addPlayerPerk(this);
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Perk getPerk() {
		return this.perk;
	}
	
	public boolean isUnlocked() {
		return this.unlocked;
	}
	
	public PlayerPerk getParent() {
		return this.parent;
	}
	
	public static PlayerPerk getPlayerPerk(Perk perk) {
		
		for(PlayerPerk playerPerk : getAllPlayerPerks()) {
			
			Perk thisPerk = playerPerk.getPerk();
			if(perk.equals(thisPerk)) {
				return playerPerk;
			}
			
		}
		
		return null;
		
	}
	
}
