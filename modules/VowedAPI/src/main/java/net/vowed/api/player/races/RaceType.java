package net.vowed.api.player.races;

import net.vowed.api.player.races.Gender;
import net.vowed.api.player.races.Skin;
import org.bukkit.ChatColor;

/**
 * Created by JPaul on 11/21/2015.
 */
public enum RaceType
{
    DWARF("Dwarf", ChatColor.DARK_RED, Skin.DWARF_MALE, Skin.DWARF_FEMALE),
    ELF("Elf", ChatColor.DARK_GREEN, Skin.ELF_MALE, Skin.ELF_FEMALE),
    HUMAN("Human", ChatColor.DARK_AQUA, Skin.HUMAN_MALE, Skin.HUMAN_FEMALE);

    private String name;
    private ChatColor chatColour;
    private Skin male;
    private Skin female;

    RaceType(String name, ChatColor chatColour, Skin male, Skin female)
    {
        this.name = name;
        this.chatColour = chatColour;
        this.male = male;
        this.female = female;
    }

    public String getName()
    {
        return name;
    }

    public ChatColor getChatColour()
    {
        return chatColour;
    }

    public Skin getSkin(Gender gender)
    {
        if (gender == Gender.MALE)
        {
            return getMaleSkin();
        }
        else
        {
            return getFemaleSkin();
        }
    }

    public Skin getMaleSkin()
    {
        return male;
    }

    public Skin getFemaleSkin()
    {
        return female;
    }
}
