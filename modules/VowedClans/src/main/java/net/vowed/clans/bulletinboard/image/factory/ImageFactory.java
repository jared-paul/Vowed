package net.vowed.clans.bulletinboard.image.factory;

import com.google.common.collect.Lists;
import net.vowed.api.clans.IClan;
import net.vowed.api.clans.billboard.BulletinMessageTuple;
import net.vowed.api.clans.billboard.IBulletinBoard;
import net.vowed.api.clans.billboard.MessagePriority;
import net.vowed.api.plugin.Vowed;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by JPaul on 2017-03-20.
 */
public class ImageFactory
{
    private static BufferedImage createDefaultImage()
    {
        int w = 500;
        int h = 1000;
        BufferedImage image = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.setPaint(Color.BLACK);
        graphics.fillRect(0, 0, w, h);

        graphics.dispose();
        return image;
    }

    private static BufferedImage createBulletinBoardImage(IBulletinBoard bulletinBoard)
    {
        BufferedImage image = createDefaultImage();

        Map<String, BulletinMessageTuple> messages = bulletinBoard.getMessages();
        java.util.List<String> messagesSimple = Lists.newArrayList();

        int messageY = 0;

        for (Map.Entry<String, BulletinMessageTuple> messageEntry : messages.entrySet())
        {
            Graphics2D graphics = image.createGraphics();
            graphics.setFont(new Font("Serif", Font.BOLD, 20));

            String message = messageEntry.getKey();
            BulletinMessageTuple messageInfo = messageEntry.getValue();
            MessagePriority priority = messageInfo.getPriority();
            long creationDate = messageInfo.getCreationDate();
            Date dateObject = new Date(creationDate);

            String date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);

            graphics.setPaint(fromChatColour(priority.colour));

            int priorityX = 1;
            messageY += 20;

            graphics.drawString(Character.toString(priority.icon), priorityX, messageY);

            graphics.setPaint(fromChatColour(ChatColor.GREEN));

            int messageX = priorityX + 20;

            graphics.drawString(message, messageX, messageY);
            graphics.dispose();
        }

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);

        return image;
    }

    public static BufferedImage getImage(IBulletinBoard bulletinBoard)
    {
        return createBulletinBoardImage(bulletinBoard);
    }

    public static BufferedImage getDefaultImage()
    {
        return createDefaultImage();
    }

    private static Color fromChatColour(ChatColor chatColor)
    {
        switch (chatColor)
        {
            case RED:
                return Color.RED;
            case AQUA:
                return Color.CYAN;
            case BLUE:
                return Color.BLUE;
            case GOLD:
                return Color.ORANGE;
            case GRAY:
                return Color.GRAY;
            case BLACK:
                return Color.BLACK;
            case GREEN:
                return Color.GREEN;
            case WHITE:
                return Color.WHITE;
            case YELLOW:
                return Color.YELLOW;
        }

        return null;
    }
}
