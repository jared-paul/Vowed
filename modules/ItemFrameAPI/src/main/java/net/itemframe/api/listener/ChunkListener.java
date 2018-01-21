package net.itemframe.api.listener;

import net.itemframe.api.FrameManager;
import net.itemframe.api.util.Frame;
import net.itemframe.api.util.Utils;
import org.bukkit.Chunk;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.List;

public class ChunkListener implements Listener {
    private final FrameManager manager;

    public ChunkListener(FrameManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        List<Frame> frames = this.manager.getFramesInChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
        List<Frame> framesToRemove = new ArrayList<Frame>();

        for (Frame frame : frames) {
            ItemFrame entity = Utils.getItemFrameFromChunk(chunk, frame.getLocation(), frame.getFacing());
            if (entity == null) {
                // The item frame doesn't exists. Remove it.
                framesToRemove.remove(frame);
                continue;
            }
            frame.setEntity(entity);
        }

        if (!framesToRemove.isEmpty()) {
            for (Frame frame : framesToRemove) {
                frame.clearCache();
                frames.remove(frame);
            }
            this.manager.setFramesInChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ(), frames);
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.isCancelled()) return;
        Chunk chunk = event.getChunk();
        List<Frame> frames = this.manager.getFramesInChunk(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());

        for (Frame frame : frames) {
            frame.setEntity(null);
        }
    }

}