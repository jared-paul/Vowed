package net.vowed.core.util.schematics.builder;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.PlayerAnimation;
import net.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Queue;

/**
 * Created by JPaul on 6/9/2016.
 */
public class BuilderRunnable extends BukkitRunnable
{
    private Vector offset;
    private Location location;
    private Queue<Coords> coords;
    private NPC builder;
    private File schematic;

    private int ticksSinceLastPlace = 0;
    private BukkitTask task;

    private final String BLOCK_VECTOR_KEY = "blockVector";
    private final String BLOCK_LOCATION_KEY = "blockLocation";
    private final String BLOCK_KEY = "block";
    private final String OFFSET_KEY = "offset";
    private final String CENTER_LOCATION_KEY = "center";
    private final String COORDS_KEY = "coords";
    private final String BUILDERS_KEY = "builders";
    private final String SCHEMATIC_KEY = "schematic";
    private final BuilderCallBack callBack;


    public BuilderRunnable(Vector offset, Location location, Queue<Coords> coords, NPC builder, File schematic, BuilderCallBack callback)
    {
        this.offset = offset;
        this.location = location;
        this.coords = coords;
        this.builder = builder;
        this.schematic = schematic;
        this.callBack = callback;
    }


    @Override
    public void run()
    {
        if (coords.isEmpty())
        {
            callBack.onFinished(builder);
        }

        Coords block = coords.poll();
        BlockVector blockVector = new Vector(block.x, block.y, block.z).add(new Vector(location.getX(), location.getY(), location.getZ()).add(offset)).toBlockPoint();

        int x = blockVector.getBlockX();
        int y = blockVector.getBlockY();
        int z = blockVector.getBlockZ();
        Location blockLocation = new Location(location.getWorld(), x, y, z);

        builder.getNavigator().setTarget(findaSpot(blockLocation.getBlock()).add(0.5, 1, 0.5));

        ((HumanEntity) builder.getEntity()).getInventory().setItemInMainHand(new ItemStack(block.baseBlock.getId()));

        builder.data().set(BLOCK_VECTOR_KEY, blockVector);
        builder.data().set(BLOCK_KEY, block);
        builder.data().set(BLOCK_LOCATION_KEY, blockLocation);
        builder.data().set(OFFSET_KEY, offset);
        builder.data().set(CENTER_LOCATION_KEY, location);
        builder.data().set(COORDS_KEY, coords);
        builder.data().set(BUILDERS_KEY, builder);
        builder.data().set(SCHEMATIC_KEY, schematic);

        builder.getNavigator().getLocalParameters().addSingleUseCallback(cancelReason -> placeNextBlock(builder));

        task = Bukkit.getScheduler().runTaskTimer(Vowed.getPlugin(), () ->
        {
            if (ticksSinceLastPlace >= 200)
            {
                builder.teleport(findaSpot(blockLocation.getBlock()).add(0.5, 1, 0.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
                placeNextBlock(builder);
                ticksSinceLastPlace = 0;
                cancel();
            }

            ticksSinceLastPlace++;
        }, 1, 1);
    }

    public void placeNextBlock(NPC builder)
    {
        if (task != null)
        {
            task.cancel();
        }

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(builder.getEntity().getWorld()), Integer.MAX_VALUE);

        Coords block = builder.data().get(BLOCK_KEY);
        BlockVector blockVector = builder.data().get(BLOCK_VECTOR_KEY);
        Location blockLocation = builder.data().get(BLOCK_LOCATION_KEY);

        try
        {
            PlayerAnimation.ARM_SWING.play((Player) builder.getEntity(), 64);

            editSession.setBlock(blockVector, block.baseBlock);
            blockLocation.getWorld().playEffect(blockLocation, Effect.STEP_SOUND, block.baseBlock.getId());
        }
        catch (MaxChangedBlocksException e)
        {
            e.printStackTrace();
        }

        try
        {
            MetadataStore data = builder.data();

            new BuilderRunnable(data.get(OFFSET_KEY), data.get(CENTER_LOCATION_KEY), data.get(COORDS_KEY), data.get(BUILDERS_KEY), data.get(SCHEMATIC_KEY), callBack).runTask(Vowed.getPlugin());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Location findaSpot(Block base)
    {
        if (base == null)
        {
            return null;
        }

        for (int a = 3; a >= -5; a--)
        {
            if (canStand(base.getRelative(0, a, -1)))
            {
                return base.getRelative(0, a - 1, -1).getLocation();
            }
            if (canStand(base.getRelative(0, a, 1)))
            {
                return base.getRelative(0, a - 1, 1).getLocation();
            }
            if (canStand(base.getRelative(1, a, 0)))
            {
                return base.getRelative(1, a - 1, 0).getLocation();
            }
            if (canStand(base.getRelative(-1, a, 0)))
            {
                return base.getRelative(-1, a - 1, 0).getLocation();
            }
            if (canStand(base.getRelative(-1, a, -1)))
            {
                return base.getRelative(-1, a - 1, -1).getLocation();
            }
            if (canStand(base.getRelative(-1, a, 1)))
            {
                return base.getRelative(-1, a - 1, 1).getLocation();
            }
            if (canStand(base.getRelative(1, a, 1)))
            {
                return base.getRelative(1, a - 1, 1).getLocation();
            }
            if (canStand(base.getRelative(1, a, -1)))
            {
                return base.getRelative(1, a - 1, -1).getLocation();
            }
        }

        return base.getLocation();
    }

    private boolean canStand(org.bukkit.block.Block base)
    {
        org.bukkit.block.Block below = base.getRelative(0, -1, 0);
        if (!below.isEmpty() && below.getType().isSolid())
        {
            if (base.isEmpty() || !base.getType().isSolid())
            {
                return true;
            }
        }
        return false;
    }

    public interface BuilderCallBack
    {
        void onFinished(NPC builder);
    }
}
