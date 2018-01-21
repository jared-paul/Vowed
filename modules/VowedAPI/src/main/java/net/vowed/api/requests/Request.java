package net.vowed.api.requests;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.members.IClanPlayer;

import java.util.UUID;

/**
 * Created by JPaul on 8/16/2016.
 */
public interface Request
{
    UUID getUUID();

    String getName();

    IClan getRequester();

    boolean isRequester(IClan clanPlayer);

    boolean isRequested(IClanPlayer clanPlayer);

    IClanPlayer getRequested();

    void onRequesting();

    void accept(IClanPlayer voter);

    void deny(IClanPlayer voter);

    boolean hasVoted(IClanPlayer clanPlayer);

    boolean hasEveryoneVoted();

    boolean checkRequest();

    void announceMessage(String message);

    /**
     * Sends message to the officers and leaders
     */
    void announceToHead(String message);

    boolean onAccepted();

    void onDenied();
}
