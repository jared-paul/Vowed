package net.vowed.api.clans;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by JPaul on 8/12/2016.
 */
public enum Rank
{
    LEADER(1)
            {
                @Override
                public List<Rank> allowedPromotions()
                {
                    return Arrays.asList(OFFICER, TRUSTED);
                }
            },
    OFFICER(2)
            {
                @Override
                public List<Rank> allowedPromotions()
                {
                    return Collections.singletonList(TRUSTED);
                }
            },
    TRUSTED(3)
            {
                @Override
                public List<Rank> allowedPromotions()
                {
                    return Lists.newArrayList();
                }
            },
    MEMBER(4)
            {
                @Override
                public List<Rank> allowedPromotions()
                {
                    return Lists.newArrayList();
                }
            };

    public int priority;

    Rank(int priority)
    {
        this.priority = priority;
    }

    public static Rank fromString(String rank)
    {
        for (Rank rankFinder : Rank.values())
        {
            if (rankFinder.name().equalsIgnoreCase(rank))
            {
                return rankFinder;
            }
        }

        return null;
    }

    public static Rank fromPriority(int priority)
    {
        for (Rank rank : Rank.values())
        {
            if (rank.priority == priority)
            {
                return rank;
            }
        }

        return null;
    }

    public abstract List<Rank> allowedPromotions();
}
