package net.itemframe.api.event;

import net.itemframe.api.util.Frame;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreateFrameEvent extends Event implements Cancellable {
    private final Frame frame;
    private final ItemFrame entity;
    private boolean cancelled = false;
    private static HandlerList handlerList = new HandlerList();

    public CreateFrameEvent(Frame frame, ItemFrame entity) {
        this.frame = frame;
        this.entity = entity;
    }

    public Frame getFrame() {
        return this.frame;
    }

    public ItemFrame getEntity() {
        return this.entity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
