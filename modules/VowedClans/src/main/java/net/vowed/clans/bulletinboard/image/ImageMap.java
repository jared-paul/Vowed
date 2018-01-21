package net.vowed.clans.bulletinboard.image;

import java.io.File;

public class ImageMap
{
    private File imageFile;
    private int x;
    private int y;
    private boolean fastsend;

    public ImageMap(File imageFile, int x, int y, boolean fastsend)
    {
        this.imageFile = imageFile;
        this.x = x;
        this.y = y;
        this.fastsend = fastsend;
    }

    public File getImageFile()
    {
        return imageFile;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public boolean isFastSend()
    {
        return fastsend;
    }

    public boolean isSimilar(File file, int x2, int y2)
    {
        if (!getImageFile().getName().equalsIgnoreCase(file.getName()))
            return false;
        if (getX() != x2)
            return false;
        if (getY() != y2)
            return false;

        return true;
    }

    private void storeMap()
    {

    }
}
