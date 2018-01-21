package net.vowed.api.requests;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.requests.exceptions.HasVotedException;
import net.vowed.api.requests.exceptions.NotInClanException;
import net.vowed.api.requests.exceptions.RequestNonExistentException;
import net.vowed.api.requests.exceptions.TooManyRequestsException;

/**
 * Created by JPaul on 8/16/2016.
 */
public interface IRequestManager
{
    Request createRequest(Request created);

    Request vote(IClan requester, IClanPlayer requested, String requestName, IClanPlayer voter, VoteResult voteResult) throws RequestNonExistentException, HasVotedException;

    Request vote(IClan requester, IClanPlayer requested, IClanPlayer voter, VoteResult voteResult) throws TooManyRequestsException, NotInClanException, RequestNonExistentException, HasVotedException;

    Request getOnlyRequest(IClan requester, IClanPlayer requested) throws TooManyRequestsException, NotInClanException, RequestNonExistentException;

    void clearRequests(IClan clan);

    void clearRequests(IClanPlayer clanPlayer);
}
