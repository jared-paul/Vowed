package net.itemframe.api.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.injector.GamePhase;
import net.itemframe.api.FramePicturePlugin;
import net.itemframe.api.util.Frame;
import net.itemframe.api.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FramePacketListener implements PacketListener
{

    @Override
    public void onPacketSending(PacketEvent pe)
    {
        if (pe.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY)
        {
            PacketContainer packet = pe.getPacket();
            final Player player = pe.getPlayer();

            int entityID = packet.getIntegers().read(0);
            Location loc = new Location(
                    player.getWorld(),
                    (packet.getDoubles().read(0) / 32.0D),
                    (packet.getDoubles().read(1) / 32.0D),
                    (packet.getDoubles().read(2) / 32.0D)
            );
            int entityType = packet.getIntegers().read(6);
            float yaw = packet.getIntegers().read(5) * 360.F / 256.0F;

            // Check if the entity is a item frame (Id 71)
            if (entityType != 71)
            {
                return;
            }

            Chunk chunk = loc.getChunk();
            if (!chunk.isLoaded())
            {
                return;
            }

            Frame frame = FramePicturePlugin.getManager().getFrameWithEntityID(chunk, entityID);
            if (frame == null)
            {
                // Search the frame in the chunk.
                BlockFace facing = yawToFace(yaw);
                ItemFrame entity = Utils.getItemFrameFromChunk(chunk, loc, facing);
                if (entity == null)
                {
                    return;
                }

                frame = FramePicturePlugin.getManager().getFrame(loc, facing);
                if (frame == null)
                {
                    return;
                }
                frame.setEntity(entity);
            }

            final Frame frameToSend = frame;
            Bukkit.getScheduler().runTaskLater(FramePicturePlugin.getPlugin(), new Runnable()
            {
                @Override
                public void run()
                {
                    frameToSend.sendTo(player);
                }
            }, 10L);
        }
    }

    private BlockFace convertDirectionToBlockFace(int direction)
    {
        switch (direction)
        {
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.EAST;
            default:
                return BlockFace.NORTH;
        }
    }

    public static BlockFace yawToFace(float yaw)
    {
        return yawToFace(yaw, true);
    }

    public static float normalAngle(float angle)
    {
        while (angle <= -180) angle += 360;
        while (angle > 180) angle -= 360;
        return angle;
    }

    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections)
    {
        yaw = normalAngle(yaw);
        if (useSubCardinalDirections)
        {
            switch ((int) yaw)
            {
                case 0:
                    return BlockFace.NORTH;
                case 45:
                    return BlockFace.NORTH_EAST;
                case 90:
                    return BlockFace.EAST;
                case 135:
                    return BlockFace.SOUTH_EAST;
                case 180:
                    return BlockFace.SOUTH;
                case 225:
                    return BlockFace.SOUTH_WEST;
                case 270:
                    return BlockFace.WEST;
                case 315:
                    return BlockFace.NORTH_WEST;
            }
            //Let's apply angle differences
            if (yaw >= -22.5 && yaw < 22.5)
            {
                return BlockFace.NORTH;
            }
            else if (yaw >= 22.5 && yaw < 67.5)
            {
                return BlockFace.NORTH_EAST;
            }
            else if (yaw >= 67.5 && yaw < 112.5)
            {
                return BlockFace.EAST;
            }
            else if (yaw >= 112.5 && yaw < 157.5)
            {
                return BlockFace.SOUTH_EAST;
            }
            else if (yaw >= -67.5 && yaw < -22.5)
            {
                return BlockFace.NORTH_WEST;
            }
            else if (yaw >= -112.5 && yaw < -67.5)
            {
                return BlockFace.WEST;
            }
            else if (yaw >= -157.5 && yaw < -112.5)
            {
                return BlockFace.SOUTH_WEST;
            }
            else
            {
                return BlockFace.SOUTH;
            }
        }
        else
        {
            switch ((int) yaw)
            {
                case 0:
                    return BlockFace.NORTH;
                case 90:
                    return BlockFace.EAST;
                case 180:
                    return BlockFace.SOUTH;
                case 270:
                    return BlockFace.WEST;
            }
            //Let's apply angle differences
            if (yaw >= -45 && yaw < 45)
            {
                return BlockFace.NORTH;
            }
            else if (yaw >= 45 && yaw < 135)
            {
                return BlockFace.EAST;
            }
            else if (yaw >= -135 && yaw < -45)
            {
                return BlockFace.WEST;
            }
            else
            {
                return BlockFace.SOUTH;
            }
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent pe)
    {
    }

    @Override
    public ListeningWhitelist getSendingWhitelist()
    {
        return ListeningWhitelist.newBuilder().
                priority(ListenerPriority.LOW).
                types(PacketType.Play.Server.SPAWN_ENTITY).
                gamePhase(GamePhase.BOTH).
                options(new ListenerOptions[0]).
                build();
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist()
    {
        return ListeningWhitelist.EMPTY_WHITELIST;
    }

    @Override
    public Plugin getPlugin()
    {
        return FramePicturePlugin.getPlugin();
    }

}
