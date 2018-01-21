package net.vowed.wir.generation;

import java.io.File;

/**
 * Created by JPaul on 9/28/2016.
 */
public class Widget
{
    private File schematic;
    private boolean loaded;

    public Widget(File schematic)
    {
        this.schematic = schematic;
    }

    public File getSchematic()
    {
        return schematic;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
    }

    public boolean isWall()
    {
        return schematic.getPath().toLowerCase().contains("wall");
    }

    public Size getSize()
    {
        String path = schematic.getPath().toLowerCase();

        if (path.contains("tiny"))
        {
            return Size.TINY;
        }
        else if (path.contains("small"))
        {
            return Size.SMALL;
        }
        else if (path.contains("medium"))
        {
            return Size.MEDIUM;
        }
        else if (path.contains("big"))
        {
            return Size.BIG;
        }
        else if (path.contains("huge"))
        {
            return Size.HUGE;
        }

        return null;
    }

    public enum Size
    {
        HUGE,
        BIG,
        MEDIUM,
        SMALL,
        TINY;
    }
}
