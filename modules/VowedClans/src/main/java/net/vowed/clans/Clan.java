package net.vowed.clans;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.vowed.api.bases.IBase;
import net.vowed.api.clans.IClan;
import net.vowed.api.clans.Rank;
import net.vowed.api.clans.billboard.IBulletinBoard;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import net.vowed.clans.bulletinboard.BulletinBoard;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by JPaul on 8/12/2016.
 */
public class Clan implements IClan
{
    private UUID uuid;
    private String name;
    private File dataFolder;
    private File storageFile;
    private IBase base;
    private IBulletinBoard bulletinBoard;
    private String tag;
    private Set<IClanPlayer> members = Sets.newHashSet();
    private List<IClan> allies = Lists.newArrayList();
    private List<IClan> enemies = Lists.newArrayList();


    public Clan(String name, IClanPlayer leader)
    {
        this(name);
        addMember(leader);
        leader.setRank(Rank.LEADER);
    }

    public Clan(String name)
    {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.tag = "";
        this.dataFolder = new File(Vowed.getPlugin().getDataFolder() + "\\Clans\\" + uuid);
        this.storageFile = new File(dataFolder + "\\storage.dat");

        try
        {
            initializeStorage();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        this.bulletinBoard = new BulletinBoard(this);
        Vowed.LOG.debug(bulletinBoard.toString());
    }

    public static Clan createClan(String name)
    {
        return new Clan(name);
    }

    public static Clan createClan(String name, IClanPlayer leader)
    {
        return new Clan(name, leader);
    }

    @Override
    public UUID getUUID()
    {
        return uuid;
    }

    @Override
    public void setUUID(UUID uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public File getDataFolder()
    {
        return dataFolder;
    }

    @Override
    public IBase getBase()
    {
        return base;
    }

    @Override
    public void setBase(IBase base)
    {
        this.base = base;
    }

    @Override
    public IBulletinBoard getBulletinBoard()
    {
        return bulletinBoard;
    }

    @Override
    public void setBulletinBoard(IBulletinBoard bulletinBoard)
    {
        this.bulletinBoard = bulletinBoard;
    }

    @Override
    public String getTag()
    {
        return tag;
    }

    @Override
    public void setTag(String tag)
    {
        this.tag = tag;
    }

    @Override
    public List<IClan> getAllies()
    {
        return allies;
    }

    @Override
    public void addAlly(IClan ally)
    {
        allies.add(ally);
    }

    @Override
    public void removeAlly(IClan ally)
    {
        allies.remove(ally);
    }

    @Override
    public List<IClan> getEnemies()
    {
        return enemies;
    }

    @Override
    public void addEnemy(IClan enemy)
    {
        enemies.add(enemy);
    }

    @Override
    public void removeEnemy(IClan enemy)
    {
        enemies.remove(enemy);
    }

    @Override
    public IClanPlayer getLeader()
    {
        for (IClanPlayer member : members)
        {
            if (member.getRank() == Rank.LEADER)
            {
                return member;
            }
        }

        return null;
    }

    @Override
    public void setLeader(IClanPlayer leader)
    {
        leader.setRank(Rank.LEADER);
    }

    @Override
    public List<IClanPlayer> getOfficers()
    {
        List<IClanPlayer> officers = Lists.newArrayList();

        for (IClanPlayer member : members)
        {
            if (member.getRank() == Rank.OFFICER)
            {
                officers.add(member);
            }
        }

        return officers;
    }

    @Override
    public void addOfficer(IClanPlayer member)
    {
        member.setRank(Rank.OFFICER);
    }

    @Override
    public void removeOfficer(IClanPlayer member)
    {
        member.setRank(Rank.TRUSTED);
    }

    @Override
    public List<IClanPlayer> getTrustedMembers()
    {
        List<IClanPlayer> trustedMembers = Lists.newArrayList();

        for (IClanPlayer member : members)
        {
            if (member.getRank() == Rank.TRUSTED)
            {
                trustedMembers.add(member);
            }
        }

        return trustedMembers;
    }

    @Override
    public void addTrusted(IClanPlayer member)
    {
        member.setRank(Rank.TRUSTED);
    }

    @Override
    public void removeTrusted(IClanPlayer member)
    {
        member.setRank(Rank.MEMBER);
    }

    @Override
    public Set<IClanPlayer> getMembers()
    {
        return members;
    }

    @Override
    public void setMembers(Set<IClanPlayer> members)
    {
        this.members = members;
        for (IClanPlayer clanPlayer : members)
        {
            clanPlayer.setClan(this);
        }
    }

    @Override
    public void addMember(IClanPlayer member)
    {
        members.add(member);
        member.setClan(this);
    }

    @Override
    public void removeMember(IClanPlayer member)
    {
        members.remove(member);
        member.setClan(null);
    }

    private void initializeStorage() throws IOException
    {
        this.dataFolder.mkdirs();
        storageFile.createNewFile();
    }

    @Override
    public void save() throws IOException
    {
        Vowed.getSQLStorage().updateQuery("clans",
                new String[]{"clan_uuid", "leader", "name", "tag"},
                new String[]{uuid.toString(), getLeader().getUUID().toString(), name, tag},
                new String[]{uuid.toString(), getLeader().getUUID().toString(), name, tag},
                new SQLStorage.Callback<Integer>()
                {
                    @Override
                    public void onSuccess(Integer integer) throws SQLException
                    {
                        for (IClanPlayer member : members)
                        {
                            Vowed.getSQLStorage().updateQuery("members",
                                    new String[]{"member_uuid", "clan_uuid", "rank"},
                                    new String[]{member.getUUID().toString(), uuid.toString(), member.getRank().name()},
                                    new String[]{member.getUUID().toString(), uuid.toString(), member.getRank().name()},
                                    new SQLStorage.Callback<Integer>()
                                    {
                                        @Override
                                        public void onSuccess(Integer integer) throws SQLException
                                        {

                                        }

                                        @Override
                                        public void onFailure(Throwable cause)
                                        {
                                            Vowed.LOG.warning("FAILED TO SAVE MEMBER: " + member.getUUID());
                                            cause.printStackTrace();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(Throwable cause)
                    {
                        Vowed.LOG.warning("FAILED TO SAVE CLAN: " + name);
                        cause.printStackTrace();
                    }
                });

        bulletinBoard.save();
    }

}
