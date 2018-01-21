package net.vowed.clans.requests;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.vowed.api.clans.IClan;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.requests.IRequestManager;
import net.vowed.api.requests.Request;
import net.vowed.api.requests.VoteResult;
import net.vowed.api.requests.exceptions.HasVotedException;
import net.vowed.api.requests.exceptions.NotInClanException;
import net.vowed.api.requests.exceptions.RequestNonExistentException;
import net.vowed.api.requests.exceptions.TooManyRequestsException;

import java.util.Map;
import java.util.Set;

/**
 * Created by JPaul on 8/16/2016.
 */
public class RequestManager implements IRequestManager
{
    Map<IClan, Set<Map<String, Map<IClanPlayer, Request>>>> requests = Maps.newHashMap();

    @Override
    public Request createRequest(Request created)
    {
        if (requests.get(created.getRequester()) == null)
        {
            requests.put(created.getRequester(), Sets.newHashSet());
        }

        Map<String, Map<IClanPlayer, Request>> requestNameMap = Maps.newHashMap();

        Map<IClanPlayer, Request> requestMap = Maps.newHashMap();
        requestMap.put(created.getRequested(), created);


        requestNameMap.put(created.getName(), requestMap);

        requests.get(created.getRequester()).add(requestNameMap);

        created.onRequesting();
        return created;
    }

    @Override
    public Request vote(IClan requester, IClanPlayer requested, String requestName, IClanPlayer voter, VoteResult voteResult) throws RequestNonExistentException, HasVotedException
    {
        if (requests.isEmpty() || requests.get(requester).isEmpty())
            throw new RequestNonExistentException();

        Request request = getRequest(requester, requested, requestName);

        if (request.hasVoted(voter))
            throw new HasVotedException();

        voteResult.vote(voter, request);

        if (request.hasEveryoneVoted())
        {
            if (request.checkRequest())
            {
                request.onAccepted();
                deleteRequest(requester, requested, request);
            }
            else
            {
                request.onDenied();
                deleteRequest(requester, requested, request);
            }
        }

        return request;
    }

    @Override
    public Request vote(IClan requester, IClanPlayer requested, IClanPlayer voter, VoteResult voteResult) throws TooManyRequestsException, NotInClanException, RequestNonExistentException, HasVotedException
    {
        if (requests.isEmpty() || requests.get(requester).isEmpty())
            throw new RequestNonExistentException();

        Request request = getOnlyRequest(requester, requested);

        if (request.hasVoted(voter))
            throw new HasVotedException();

        voteResult.vote(voter, request);

        if (request.hasEveryoneVoted())
        {
            if (request.checkRequest())
            {
                request.onAccepted();
                deleteRequest(requester, requested, request);
            }
            else
            {
                request.onDenied();
                deleteRequest(requester, requested, request);
            }
        }

        return request;
    }

    @Override
    public Request getOnlyRequest(IClan requester, IClanPlayer requested) throws TooManyRequestsException, NotInClanException, RequestNonExistentException
    {
        if (getHowManyRequests(requester, requested) > 1)
            throw new TooManyRequestsException();

        if (!requester.getMembers().contains(requested))
            throw new NotInClanException();

        if (requests.get(requester) != null)
        {
            for (Map<String, Map<IClanPlayer, Request>> requestMap : requests.get(requester))
            {
                for (Map<IClanPlayer, Request> requests : requestMap.values())
                {
                    if (requests.get(requested) != null)
                    {
                        return requests.get(requested);
                    }
                }
            }
        }

        throw new RequestNonExistentException();
    }

    public boolean hasVoted(IClanPlayer voter, MultiRequest request)
    {
        return request.getVote().getPeopleVoted().contains(voter);
    }

    public Request getRequest(IClan requester, IClanPlayer requested, String requestName) throws RequestNonExistentException
    {
        for (Map<String, Map<IClanPlayer, Request>> requestFinder : requests.get(requester))
        {
            Map<IClanPlayer, Request> requestMap = requestFinder.get(requestName);

            if (requestMap != null && requestMap.get(requested) != null)
            {
                return requestMap.get(requested);
            }
        }

        throw new RequestNonExistentException();
    }

    public int getHowManyRequests(IClan requester, IClanPlayer requested)
    {
        int howManyRequests = 0;

        if (requests.get(requester) != null)
        {
            for (Map<String, Map<IClanPlayer, Request>> requestMap : requests.get(requester))
            {
                for (Map<IClanPlayer, Request> requests : requestMap.values())
                {
                    if (requests.containsKey(requested))
                        howManyRequests++;
                }
            }
        }

        return howManyRequests;
    }

    public void deleteRequest(IClan requester, IClanPlayer requested, Request request)
    {
        if (requests.get(requester) != null)
        {
            for (Map<String, Map<IClanPlayer, Request>> requestNameMap : requests.get(requester))
            {
                for (Map<IClanPlayer, Request> requestMap : requestNameMap.values())
                {
                    Request requestFinder = requestMap.get(requested);

                    if (requestFinder != null && requestFinder.getUUID().equals(request.getUUID()))
                    {
                        requestMap.remove(requested);
                    }
                }
            }
        }
    }

    @Override
    public void clearRequests(IClan clan)
    {

    }

    @Override
    public void clearRequests(IClanPlayer clanPlayer)
    {

    }
}
