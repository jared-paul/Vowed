package net.vowed.core.mobs;

/**
 * Created by JPaul on 11/23/2015.
 */
public abstract class AIGoal
{
    public abstract boolean shouldStart();

    public abstract boolean shouldFinish();

    public abstract void start();

    public abstract void finish();

    public abstract void tick();
}