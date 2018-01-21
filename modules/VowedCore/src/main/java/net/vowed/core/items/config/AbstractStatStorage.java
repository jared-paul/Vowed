package net.vowed.core.items.config;

/**
 * Created by JPaul on 6/15/2016.
 */
public abstract class AbstractStatStorage implements StatStorage
{
    public int statMIN;
    public int statMAX;

    public AbstractStatStorage(int statMIN, int statMAX)
    {
        this.statMIN = statMIN;
        this.statMAX = statMAX;
    }
}
