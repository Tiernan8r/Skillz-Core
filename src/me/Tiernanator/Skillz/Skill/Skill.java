/**
 *
 */
package me.Tiernanator.Skillz.Skill;

import me.Tiernanator.Skillz.SkillzMain;
import me.Tiernanator.Utilities.Colours.MessageColourer;
import me.Tiernanator.Utilities.File.ConfigAccessor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tiernan
 *
 */
//The overarching class that describes a skill:
    // Layout is similar to Skyrim perk trees where the Skill defined is the tree and the perks are the
    // individual abilities that are unllocked for the skill
public class Skill {

    //region Static Methods
    //Refers to the instance of the JavaPlugin that is created for this plugin
    private static SkillzMain plugin;

    //called by main to set the instance
    public static void setPlugin(SkillzMain main) {
        plugin = main;
    }

    //Save a list of all the created skills
    private static List<Skill> allSkills = new ArrayList<Skill>();

    //function to get all the skills that exist
    public static List<Skill> getAllSkills() {
        return allSkills;
    }

    //Get a specfic skill by name
    public static Skill getSkill(String skillName) {

        //Get a reference to the list of skills
        List<Skill> allSkills = getAllSkills();
        //if there are no pre existing skills, then we have to return null
        if (allSkills == null || allSkills.isEmpty()) {
            return null;
        }

        //FIXME Verify that skills have unique names as a requirement?

        //iterate over all existing skills
        for (Skill skill : allSkills) {

            //get the skill name
            String name = skill.getName();
            //format the skill name to plaintext
            name = MessageColourer.removeColours(name);
            //compare caseless
            if (skillName.equalsIgnoreCase(name)) {
                return skill;
            }

        }
        //if the name wasn't in the list of all skills, it doesn't exist, so return null
        return null;
    }

    //Sets the existing list of skills to the provided one
    private static void setAllSkills(List<Skill> newSkills) {
        //only overwrites the current list if the new list is an initialised object
        if (newSkills == null) {
            return;
        }
        allSkills = newSkills;
    }

    //Appends a skill to the list
    public static void addSkill(Skill skill) {

        //get the current skills
        List<Skill> allSkills = getAllSkills();
        //add the skill if it isn't null and not already in the list
        if (skill != null && !allSkills.contains(skill)) {
            allSkills.add(skill);
        }
        //set the array to be the new one
        setAllSkills(allSkills);

    }
    //endregion

    //Name of the skill
    private String name;
    //Highest level obtainable for the skill
    private int maxLevel;
    //list of all perks associated with the skill
    private List<Perk> allPerks = new ArrayList<Perk>();

    //intialiser for the skill
    public Skill(String name, List<Perk> perks) {

        this.allPerks = perks;
        this.name = name;

        addSkill(this);

    }

    //getter for name
    public String getName() {
        return this.name;
    }

    //getter for the perks list
    public List<Perk> getPerks() {
        return this.allPerks;
    }

    //getter for max level
    public int getMaxLevel() {
        return this.maxLevel;
    }

    //Creates a skill instance from data in files
    public static void initialiseSkillsFromFile() {

        //Iterate over all plugins in use in the server
        for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {

            //get the folder called "skills" in the plugins data folder directory
            File files = new File(pl.getDataFolder() + File.separator + "skills");
            //get the file names of all files in the folder
            File[] childrenFiles = files.listFiles();
            //if there are no skill files for this plugin, move on to the next one
            if (childrenFiles == null) {
                continue;
            }
            //iterate over the files that do exist for this plugin
            for (File file : childrenFiles) {
                //get the name of the file
                String fileName = file.getName();
                //data files need to be in .yml format to be read by the plugin
                if (!fileName.contains(".yml")) {
                    continue;
                }
                //get the plugin folder name
                String folderName = file.getParentFile().getName();

                //Get access to the .yml files for this plugin
                ConfigAccessor skillAccessor = new ConfigAccessor((JavaPlugin) pl, fileName, folderName);

                //If it has no name it isn't a properly formatted skill file
                String skillName = skillAccessor.getConfig().getString("Skill.Name");
                //if the name doens't exist, isn't a proper skill file so skip it
                if (skillName == null) {
                    continue;
                }
                //get all the perks for this skill
                List<String> perkNames = skillAccessor.getConfig().getStringList("Skill.Perks");
                List<Perk> perks = new ArrayList<Perk>();
                //iterate over the names and create the associated perk
                for (String perkName : perkNames) {
                    //The display name of the perk
                    String perksName = skillAccessor.getConfig().getString("Perks." + perkName + ".Name");
                    //A description tag for the perk
                    String description = skillAccessor.getConfig().getString("Perks." + perkName + ".Description");
                    //perk points cost for it
                    int cost = skillAccessor.getConfig().getInt("Perks." + perkName + ".Cost");

                    //The perk it's derived from, so it can only unlocked once the parent is
                    String parentName = skillAccessor.getConfig().getString("Perks." + perkName + ".Parent");
                    //If the parent is undefined, it is the top level perk
                    if (parentName.equalsIgnoreCase("") || parentName.equalsIgnoreCase("null")) {
                        parentName = null;
                    }
                    //Set the parent perk based off of above
                    Perk parentPerk = null;
                    if (parentName != null) {
                        //The assumption is that the parent perk is already defined before the child, which is a
                        // relatively safe bet, otherwise the config file is fairly poorly laid out...
                        parentPerk = Perk.getPerk(parentName);
                    }
                    //Create a perk instance from the above data
                    Perk perk = new Perk(parentPerk, cost, perksName, description);
                    //add the perk instance to the list of all perks for the skill
                    perks.add(perk);
                }
                //Create a skill instance based off of all the perks and the name
                Skill skill = new Skill(skillName, perks);
                //add the skill to all the skills
                addSkill(skill);
            }

        }

    }

}
