package net.vowed.core.mobs;

/**
 * Created by JPaul on 11/23/2015.
 */

import java.util.*;

public class AIGoalSelector
{
    private Map<Class<? extends AIGoal>, AIGoal> AIGoalMap = new HashMap<>();
    private List<AIGoal> AIGoalList = new LinkedList<>();
    private List<AIGoal> activeAIGoalList = new LinkedList<>();

    public void addGoal(Class<? extends AIGoal> name, AIGoal myPetAIgoal)
    {
        if (AIGoalMap.containsKey(name))
        {
            return;
        }
        AIGoalMap.put(name, myPetAIgoal);
        AIGoalList.add(myPetAIgoal);
    }

    public void addGoal(Class<? extends AIGoal> name, int pos, AIGoal myPetAIgoal)
    {
        if (AIGoalMap.containsKey(name))
        {
            return;
        }
        AIGoalMap.put(name, myPetAIgoal);
        AIGoalList.add(pos, myPetAIgoal);
    }

    public void replaceGoal(Class<? extends AIGoal> name, AIGoal myPetAIgoal)
    {
        if (AIGoalMap.containsKey(name))
        {
            AIGoal oldGoal = AIGoalMap.get(name);
            if (activeAIGoalList.contains(oldGoal))
            {
                activeAIGoalList.remove(oldGoal);
                oldGoal.finish();
            }
            int index = AIGoalList.indexOf(oldGoal);
            AIGoalList.add(index, myPetAIgoal);
            AIGoalList.remove(oldGoal);
            AIGoalMap.put(name, myPetAIgoal);
        }
        else
        {
            addGoal(name, myPetAIgoal);
        }
    }

    public void removeGoal(Class<? extends AIGoal> name)
    {
        if (AIGoalMap.containsKey(name))
        {
            AIGoal goal = AIGoalMap.get(name);
            AIGoalList.remove(goal);
            AIGoalMap.remove(name);
            if (activeAIGoalList.contains(goal))
            {
                goal.finish();
            }
            activeAIGoalList.remove(goal);
        }
    }

    public boolean hasGoal(Class<? extends AIGoal> name)
    {
        return AIGoalMap.containsKey(name);
    }

    public AIGoal getGoal(Class<? extends AIGoal> name)
    {
        return AIGoalMap.get(name);
    }

    public void clearGoals()
    {
        AIGoalList.clear();
        AIGoalMap.clear();
        for (AIGoal goal : activeAIGoalList)
        {
            goal.finish();
        }
        activeAIGoalList.clear();
    }

    public void tick()
    {
        // add goals
        ListIterator iterator = AIGoalList.listIterator();
        while (iterator.hasNext())
        {
            AIGoal goal = (AIGoal) iterator.next();
            if (!activeAIGoalList.contains(goal))
            {
                if (goal.shouldStart())
                {
                    goal.start();
                    activeAIGoalList.add(goal);
                }
            }
        }

        // remove goals
        iterator = activeAIGoalList.listIterator();
        while (iterator.hasNext())
        {
            AIGoal goal = (AIGoal) iterator.next();
            if (goal.shouldFinish())
            {
                goal.finish();
                iterator.remove();
            }
        }

        // tick goals
        iterator = activeAIGoalList.listIterator();
        while (iterator.hasNext())
        {
            AIGoal goal = (AIGoal) iterator.next();
            goal.tick();
        }
    }
}
