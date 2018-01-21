package net.vowed.api.clans.billboard;

import net.vowed.api.clans.billboard.MessagePriority;

/**
 * Created by JPaul on 8/18/2016.
 */
public class BulletinMessageTuple
{
    private long creationDate;
    private MessagePriority priority;

    public BulletinMessageTuple(long creationDate, MessagePriority priority)
    {
        this.creationDate = creationDate;
        this.priority = priority;
    }

    public long getCreationDate()
    {
        return creationDate;
    }

    public MessagePriority getPriority()
    {
        return priority;
    }
}
