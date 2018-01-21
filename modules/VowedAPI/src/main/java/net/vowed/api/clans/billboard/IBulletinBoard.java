package net.vowed.api.clans.billboard;

import net.vowed.api.clans.IClan;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by JPaul on 8/18/2016.
 */
public interface IBulletinBoard
{
    File getDataFolder();

    IClan getClan();

    IBoardImage getBoardImage();

    void displayMessages(Player... players);

    Map<String, BulletinMessageTuple> getMessages();

    Collection<String> getMessagesSimple();

    void addMessage(String message, MessagePriority priority);

    void removeMessage(String message);

    long getCreationDate(String bulletinMessage);

    long getTimePassed(String bulletinMessage);

    void save() throws IOException;

    void load();
}
