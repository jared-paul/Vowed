package net.itemframe.api.event;

import net.itemframe.api.util.Frame;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RemoveFrameEvent extends Event implements Cancellable {
    private Frame frame;
    private boolean cancelled = false;
    private static HandlerList handlerList = new HandlerList();

    public RemoveFrameEvent(Frame frame) {
        this.frame = frame;
    }

    public Frame getFrame() {
        return this.frame;
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