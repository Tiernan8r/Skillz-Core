package me.Tiernanator.Skillz.Skill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.Tiernanator.Packets.Packet;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class Achievement {

	private String achievementName;
	private String mainTextColour;
	private String achievementColour;
	private String descriptionTitle;
	private String descriptionTitleColour;
	private List<String> lines = new ArrayList<String>();
	private String hoverColour;
	private String descriptionColour;
	private String isBold = "false";
	private String isItalic = "false";
	private String isUnderlined = "false";
	private String isStruckThrough = "false";
	private String isObfuscated = "false";
	
	public Achievement(String name, ChatColor mainTextColour, ChatColor achievementColour, String descriptionTitle, ChatColor descriptionTitleColour, List<String> lines, ChatColor hoverColour, boolean isBold, boolean isItalic, boolean isUnderlined, boolean isStruckThrough, boolean isObfuscated) {
		
		this.achievementName = name;
		this.mainTextColour = Packet.getJsonColour(mainTextColour);
		this.achievementColour = "§" + achievementColour.getChar();
		this.descriptionTitle = descriptionTitle;
		this.descriptionTitleColour = "§" + descriptionTitleColour.getChar();
		this.lines = lines;
		this.hoverColour = Packet.getJsonColour(hoverColour);
		this.descriptionColour = "§" + hoverColour.getChar();
		this.isBold = Boolean.toString(isBold);
		this.isItalic = Boolean.toString(isItalic);
		this.isUnderlined = Boolean.toString(isUnderlined);
		this.isStruckThrough = Boolean.toString(isStruckThrough);
		this.isObfuscated = Boolean.toString(isObfuscated);
		
	}

	public Achievement(String name, List<String> lines) {
		this(name, ChatColor.WHITE, ChatColor.GREEN, "Achievement", ChatColor.ITALIC, lines, ChatColor.WHITE, false, false, false, false, false);
	}
	
	public String getName() {
		return this.achievementName;
	}
	
	private String getTextColour() {
		return this.mainTextColour;
	}
	
	private String getAchievementColour() {
		return this.achievementColour;
	}
	
	private String getDescriptionTitle() {
		return this.descriptionTitle;
	}
	
	private String getDescriptionTitleColour() {
		return this.descriptionTitleColour;
	}
	
	public List<String> getLines() {
		return this.lines;
	}
	
	private String getHoverColour() {
		return this.hoverColour;
	}
	
	private String getDescriptionColour() {
		return this.descriptionColour;
	}
	
	private String isBold() {
		return this.isBold;
	}
	
	private String isItalic() {
		return this.isItalic;
	}
	
	private String isUnderlined() {
		return this.isUnderlined;
	}
	
	private String isStruckThrough() {
		return this.isStruckThrough;
	}
	
	private String isObfuscated() {
		return this.isObfuscated;
	}
	
	public void sendAchievementMessage(Player achiever) {
		
		String playerName = achiever.getName();
		String achievementName = getName();
		String achievementColour = getAchievementColour();
		String mainText = playerName + " has just earned the achievement " + achievementColour + "[" + achievementName + "]";
		
		List<String> lines = getLines();
		String descriptionColour = getDescriptionColour();
		String descriptionTitle = getDescriptionTitle();
		String titleColour = getDescriptionTitleColour();
		String hoverText = achievementColour + achievementName + " \n" + titleColour + descriptionTitle + "\n§R";
		hoverText += descriptionColour;
		for(int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			hoverText += line;
			if(i < (lines.size() - 1)) {
				hoverText += "\n" + descriptionColour;
			}
		}
//		String hoverText = "§AWHATEVER \n§OAchievement \n§RDo something or other to achieve something or something...";
		String hoverColour = getHoverColour();
		String jsonText = "{\"text\":\"" + mainText + "\"" + "," + "\"clickEvent\"" + 
		":{\"action\":" + "\"" + "open_url" + "\",\"value\":" + "\"" + "http://google.com" + "\"}," +
		"\"hoverEvent\":{\"" + "action\":" + "\"show_text\"" + ",\"value\"" + ":{" +
		"\"text\"" + ":" + "\"" + "\"" + "," + "\"" + "extra" + "\"" + ":" + 
		"[{\"text\"" + ":\"" + hoverText + "\"" + ",\"" + 
		"color" + "\":\"" + hoverColour + "\"}]}}}";
		String textColour = getTextColour();
		String isBold = isBold();
		String isItalic = isItalic();
		String isUnderlined = isUnderlined();
		String isStruckThrough = isStruckThrough();
		String isObfuscated = isObfuscated();
//		"{\"text\":\"Click for Info:\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://google.com\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Clickety Click\",\"color\":\"aqua\"}]}}}"
		jsonText = "[\"\",{\"text\":\"" + mainText + "\",\"color\":\"" + textColour + "\",\"bold\":" + isBold + ",\"italic\":" + isItalic + ",\"underlined\":" + isUnderlined + ",\"strikethrough\":" + isStruckThrough + ",\"obfuscated\":" + isObfuscated + ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + hoverText + "\",\"color\":\"" + hoverColour + "\"}]}}}]";
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(jsonText));
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			Packet.sendPacket(player, packet);
		}
		
	}
	
}
