package net.vowed.wir.generation;

/**
 * Created by JPaul on 9/8/2016.
 */
public interface Loader
{
    void load();

    boolean isProcessing();

    boolean isDone();
}
