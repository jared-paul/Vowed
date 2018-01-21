package net.vowed.clans.requests;

import net.vowed.api.clans.IClan;
import net.vowed.api.requests.Request;

import java.util.UUID;

/**
 * Created by JPaul on 8/16/2016.
 */
public abstract class AbstractRequest implements Request
{
    protected IClan requester;
    protected UUID uuid;

    protected AbstractRequest(IClan requester)
    {
        this.requester = requester;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public UUID getUUID()
    {
        return uuid;
    }

    @Override
    public IClan getRequester()
    {
        return requester;
    }

    @Override
    public boolean isRequester(IClan clan)
    {
        return requester.getName().equals(clan.getName()); //TODO use UUIDS
    }
}
