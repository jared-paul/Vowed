package net.vowed.api.clans.members;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.Rank;
import net.vowed.api.player.IVowedPlayer;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by JPaul on 8/12/2016.
 */
public interface IClanPlayer extends Serializable
{
    IClan getClan();

    void setClan(IClan clan);

    UUID getUUID();

    Rank getRank();

    void setRank(Rank rank);

    boolean isLeader();

    boolean isOfficer();

    boolean isTrusted();

    boolean isMember();

    IVowedPlayer getVowedPlayer();
}
