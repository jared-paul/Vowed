package net.vowed.core.util.strings;

import com.google.common.collect.Lists;
import com.google.gson.stream.JsonWriter;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by JPaul on 3/19/2016.
 */
public class FancyMessageBuilder
{
    private List<MessagePart> messageParts;

    public FancyMessageBuilder(String firstMessagePart)
    {
        messageParts = Lists.newArrayList();
        messageParts.add(new MessagePart(firstMessagePart));
    }

    public FancyMessageBuilder setColour(ChatColor colour)
    {
        if (!colour.isColor())
        {
            throw new IllegalArgumentException(colour.name() + " is not a colour");
        }
        getLatest().colour = colour;
        return this;
    }

    public FancyMessageBuilder setStyle(ChatColor... styles)
    {
        for (ChatColor style : styles)
        {
            if (!style.isFormat())
            {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }

        getLatest().styles = styles;
        return this;
    }

    public FancyMessageBuilder openFile(String path)
    {
        onClick("open_file", path);
        return this;
    }

    public FancyMessageBuilder openLink(String url)
    {
        onClick("open_url", url);
        return this;
    }

    public FancyMessageBuilder suggestCommand(String command)
    {
        onClick("suggest_command", command);
        return this;
    }

    public FancyMessageBuilder runCommand(String command)
    {
        onClick("run_command", command);
        return this;
    }

    public FancyMessageBuilder setTip(String text)
    {
        onHover("show_text", text);
        return this;
    }

    public FancyMessageBuilder pair(Object object)
    {
        messageParts.add(new MessagePart(object.toString()));
        return this;
    }

    public String toJSONString()
    {
        StringWriter stringWriter = new StringWriter();
        JsonWriter json = new JsonWriter(stringWriter);

        try
        {
            if (messageParts.size() == 1)
            {
                getLatest().writeJson(json);
            }
            else
            {
                json.beginObject().name("text").value("").name("extra").beginArray();
                for (MessagePart part : messageParts)
                {
                    part.writeJson(json);
                }
                json.endArray().endObject();
            }

        } catch (IOException e)
        {
            throw new RuntimeException("invalid message");
        }
        return stringWriter.toString();
    }

    public void sendTo(Player... players)
    {
        for (Player player : players)
        {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(toJSONString())));
        }
    }

    private void onClick(String name, String data)
    {
        MessagePart latest = getLatest();
        latest.clickActionName = name;
        latest.clickActionData = data;
    }

    private void onHover(String name, String data)
    {
        MessagePart latest = getLatest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
    }

    private MessagePart getLatest()
    {
        return messageParts.get(messageParts.size() - 1);
    }

    static class MessagePart
    {
        public ChatColor colour = null;
        public ChatColor[] styles = null;
        public String clickActionName = null;
        public String clickActionData = null;
        public String hoverActionName = null;
        public String hoverActionData = null;
        public final String text;

        public MessagePart(final String text)
        {
            this.text = text;
        }

        public JsonWriter writeJson(final JsonWriter json) throws IOException
        {
            json.beginObject().name("text").value(text);
            if (colour != null)
            {
                json.name("color").value(colour.name().toLowerCase());
            }
            if (styles != null)
            {
                for (final ChatColor style : styles)
                {
                    json.name(style == ChatColor.UNDERLINE ? "underlined" : style.name().toLowerCase()).value(true);
                }
            }
            if (clickActionName != null && clickActionData != null)
            {
                json.name("clickEvent")
                        .beginObject()
                                .name("action").value(clickActionName)
                                .name("value").value(clickActionData)
                        .endObject();
            }
            if (hoverActionName != null && hoverActionData != null)
            {
                json.name("hoverEvent")
                        .beginObject()
                                .name("action").value(hoverActionName)
                                .name("value").value(hoverActionData)
                        .endObject();
            }
            return json.endObject();
        }

    }
}
