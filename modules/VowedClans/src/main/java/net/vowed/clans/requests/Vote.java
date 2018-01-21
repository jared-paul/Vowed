package net.vowed.clans.requests;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.requests.VoteResult;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by JPaul on 8/17/2016.
 */
public class Vote
{

    private Set<IClanPlayer> peopleVoted = Sets.newHashSet();

    private int accepts = 0;
    private int denies = 0;
    private List<Integer> data = Lists.newArrayList();

    public Vote()
    {
        data.add(accepts);
        data.add(denies);
    }

    public Set<IClanPlayer> getPeopleVoted()
    {
        return peopleVoted;
    }

    public int getAccepts()
    {
        return accepts;
    }

    public void addAccept(IClanPlayer voter)
    {
        accepts++;
        peopleVoted.add(voter);
    }

    public int getDenies()
    {
        return denies;
    }

    public void addDeny(IClanPlayer voter)
    {
        denies++;
        peopleVoted.add(voter);
    }

    public int getTotalVotes()
    {
        return accepts + denies;
    }

    public int getMax()
    {
        int max;

        if (accepts > denies)
            max = accepts;
        else
            max = denies;

        return max;
    }


    public Map<VoteResult, List<String>> toBarGraph()
    {
        Map<Integer, List<String>> barMap = Maps.newHashMap();
        barMap.put(0, Lists.newArrayList());
        barMap.put(1, Lists.newArrayList());

        int max = getMax();

        if (max <= 0)
            max = 1;

        int height = 5;

        for (int i = 0; i < data.size(); i++)
        {
            int barheight = (int) Math.floor(height * data.get(i) / max);

            for (int j = 0; j <= barheight; j++)
            {
                barMap.get(i).add("▉");
            }
        }

        Map<VoteResult, List<String>> voteMap = Maps.newHashMap();
        voteMap.put(VoteResult.ACCEPT, barMap.get(0));
        voteMap.put(VoteResult.DENY, barMap.get(1));

        return voteMap;
    }

    public Map<VoteResult, List<ItemStack>> toBarGraphInventory()
    {
        Map<Integer, List<ItemStack>> barMap = Maps.newHashMap();
        barMap.put(0, Lists.newArrayList());
        barMap.put(1, Lists.newArrayList());

        int max = getMax();

        if (max <= 0)
            max = 1;

        int height = 5;

        for (int i = 0; i < data.size(); i++)
        {
            int barheight = (int) Math.floor(height * data.get(i) / max);

            for (int j = 0; j <= barheight; j++)
            {
                barMap.get(i).add(new ItemStack(Material.WOOL));
            }
        }

        Map<VoteResult, List<ItemStack>> voteMap = Maps.newHashMap();
        voteMap.put(VoteResult.ACCEPT, barMap.get(0));
        voteMap.put(VoteResult.DENY, barMap.get(1));

        return voteMap;
    }
}

