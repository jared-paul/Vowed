package net.vowed.api.requests;

import net.vowed.api.clans.members.IClanPlayer;

/**
 * Created by JPaul on 8/16/2016.
 */
public enum VoteResult
{
    ACCEPT
            {
                @Override
                public void vote(IClanPlayer voter, Request request)
                {
                    request.accept(voter);
                }
            },
    DENY
            {
                @Override
                public void vote(IClanPlayer voter, Request request)
                {
                    request.deny(voter);
                }
            };

    public abstract void vote(IClanPlayer voter, Request request);

    public static VoteResult fromString(String string)
    {
        for (VoteResult voteResult : VoteResult.values())
        {
            if (string.equalsIgnoreCase(voteResult.name()))
            {
                return voteResult;
            }
        }

        return null;
    }
}
