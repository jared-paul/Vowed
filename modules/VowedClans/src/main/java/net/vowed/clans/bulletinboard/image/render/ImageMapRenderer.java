package net.vowed.clans.bulletinboard.image.render;

import net.vowed.clans.bulletinboard.image.test.Test;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

/**
 * Created by JPaul on 2017-03-20.
 */
public class ImageMapRenderer extends MapRenderer
{
    BufferedImage image;
    private boolean first = true;

    public ImageMapRenderer(BufferedImage image, int x, int y)
    {
        recalculateInput(image, x, y);
    }

    public void recalculateInput(BufferedImage input, int x1, int y1)
    {
        int x2 = Test.MAP_WIDTH;
        int y2 = Test.MAP_HEIGHT;

        if (x1 > input.getWidth() || y1 > input.getHeight())
            return;

        if (x1 + x2 >= input.getWidth())
            x2 = input.getWidth() - x1;

        if (y1 + y2 >= input.getHeight())
            y2 = input.getHeight() - y1;

        image = input.getSubimage(x1, y1, x2, y2);
        first = true;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player)
    {
        if (image != null && first)
        {
            mapCanvas.drawImage(0, 0, image);
            first = false;
        }
    }
}
