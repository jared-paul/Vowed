package net.vowed.clans.bulletinboard;

import com.google.common.collect.Maps;
import net.vowed.api.clans.IClan;
import net.vowed.api.clans.billboard.BulletinMessageTuple;
import net.vowed.api.clans.billboard.IBoardImage;
import net.vowed.api.clans.billboard.IBulletinBoard;
import net.vowed.api.clans.billboard.MessagePriority;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import net.vowed.clans.bulletinboard.image.BoardImage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created by JPaul on 8/18/2016.
 */
public class BulletinBoard implements IBulletinBoard
{
    private File dataFolder;
    private BoardImage boardImage;
    private IClan clan;
    private Map<String, BulletinMessageTuple> messages = Maps.newHashMap();

    public BulletinBoard(IClan clan)
    {
        this.clan = clan;
        this.dataFolder = new File(clan.getDataFolder() + "\\bulletinboard");

        try
        {
            initializeStorage();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        this.boardImage = new BoardImage(this);
    }

    private void initializeStorage() throws IOException
    {
        dataFolder.mkdirs();
    }

    @Override
    public File getDataFolder()
    {
        return dataFolder;
    }

    @Override
    public IClan getClan()
    {
        return clan;
    }

    @Override
    public IBoardImage getBoardImage()
    {
        return boardImage;
    }

    @Override
    public void displayMessages(Player... players)
    {
        for (Map.Entry<String, BulletinMessageTuple> messageEntry : messages.entrySet())
        {
            String message = messageEntry.getKey();
            BulletinMessageTuple messageInfo = messageEntry.getValue();
            MessagePriority priority = messageInfo.getPriority();
            long creationDate = messageInfo.getCreationDate();
            Date dateObject = new Date(creationDate);

            String date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);

            for (Player player : players)
            {
                player.sendMessage(priority.colour + "" + + priority.icon + " " + message + " | " + ChatColor.GRAY + date);
            }
        }
    }

    @Override
    public Map<String, BulletinMessageTuple> getMessages()
    {
        return messages;
    }

    @Override
    public Collection<String> getMessagesSimple()
    {
        return messages.keySet();
    }

    @Override
    public void addMessage(String message, MessagePriority priority)
    {
        messages.put(message, new BulletinMessageTuple(System.currentTimeMillis(), priority));
        boardImage.update();
    }

    @Override
    public void removeMessage(String message)
    {
        messages.remove(message);
        boardImage.update();
    }

    @Override
    public long getCreationDate(String bulletinMessage)
    {
        return messages.get(bulletinMessage).getCreationDate();
    }

    @Override
    public long getTimePassed(String bulletinMessage)
    {
        long creationDate = getCreationDate(bulletinMessage);

        return System.currentTimeMillis() - creationDate;
    }

    @Override
    public void save() throws IOException
    {
        boardImage.save();

        for (Map.Entry<String, BulletinMessageTuple> messageEntry : messages.entrySet())
        {
            String message = messageEntry.getKey();
            BulletinMessageTuple messageInfo = messageEntry.getValue();
            MessagePriority priority = messageInfo.getPriority();
            long creationDate = messageInfo.getCreationDate();
            Date dateObject = new Date(creationDate);

            String date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);

            Vowed.getSQLStorage().updateQuery("board_messages",
                    new String[]{"clan_uuid", "message", "priority", "date"},
                    new String[]{clan.getUUID().toString(), message, Character.toString(priority.icon), date},
                    new String[]{clan.getUUID().toString(), message, Character.toString(priority.icon), date},
                    new SQLStorage.Callback<Integer>()
                    {
                        @Override
                        public void onSuccess(Integer integer) throws SQLException
                        {

                        }

                        @Override
                        public void onFailure(Throwable cause)
                        {

                        }
                    }
            );
        }
    }

    @Override
    public void load()
    {
        boardImage.load();
    }
}
