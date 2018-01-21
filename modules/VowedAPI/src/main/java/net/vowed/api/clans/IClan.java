package net.vowed.api.clans;

import net.vowed.api.bases.IBase;
import net.vowed.api.clans.billboard.IBulletinBoard;
import net.vowed.api.clans.members.IClanPlayer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by JPaul on 8/12/2016.
 */
public interface IClan
{
    UUID getUUID();

    void setUUID(UUID uuid);

    String getName();

    void setName(String name);

    File getDataFolder();

    IBase getBase();

    void setBase(IBase base);

    IBulletinBoard getBulletinBoard();

    void setBulletinBoard(IBulletinBoard bulletinBoard);

    String getTag();

    void setTag(String tag);

    List<IClan> getAllies();

    void addAlly(IClan ally);

    void removeAlly(IClan ally);

    List<IClan> getEnemies();

    void addEnemy(IClan enemy);

    void removeEnemy(IClan enemy);

    IClanPlayer getLeader();

    void setLeader(IClanPlayer newLeader);

    List<IClanPlayer> getOfficers();

    void addOfficer(IClanPlayer officer);

    void removeOfficer(IClanPlayer officer);

    List<IClanPlayer> getTrustedMembers();

    void addTrusted(IClanPlayer member);

    void removeTrusted(IClanPlayer member);

    Set<IClanPlayer> getMembers();

    void setMembers(Set<IClanPlayer> members);

    void addMember(IClanPlayer member);

    void removeMember(IClanPlayer member);

    void save() throws IOException;
}
