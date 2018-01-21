package net.vowed.api.clans.billboard;

import org.bukkit.ChatColor;

/**
 * Created by JPaul on 8/18/2016.
 */
public enum MessagePriority
{
    URGENT('!', ChatColor.RED),
    MINOR('*', ChatColor.DARK_GRAY);

    public char icon;
    public ChatColor colour;

    MessagePriority(char icon, ChatColor colour)
    {
        this.icon = icon;
        this.colour = colour;
    }
}
