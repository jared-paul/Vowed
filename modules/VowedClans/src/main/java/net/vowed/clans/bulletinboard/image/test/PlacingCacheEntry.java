package net.vowed.clans.bulletinboard.image.test;

/**
 * Created by JPaul on 2017-03-20.
 */
public class PlacingCacheEntry
{
    private String image;
    private boolean fastsend;

    public PlacingCacheEntry(String image, boolean fastsend)
    {
        this.image = image;
        this.fastsend = fastsend;
    }

    public String getImage()
    {
        return image;
    }

    public boolean isFastSend()
    {
        return fastsend;
    }
}
