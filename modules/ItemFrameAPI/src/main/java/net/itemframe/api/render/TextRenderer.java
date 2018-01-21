package net.itemframe.api.render;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TextRenderer extends MapRenderer {

    private final String text;
    private Integer mapId = null;
    private boolean rendered = false;

    public TextRenderer(String text) {
        this.text = text;
    }

    public TextRenderer(String text, Integer mapId) {
        this.text = text;
        this.mapId = mapId;
    }

    public String getText() {
        return this.text;
    }

    public Integer getMapId() {
        return this.mapId;
    }

    public boolean isRendered() {
        return this.rendered;
    }

    @Override
    public void render(final MapView view, final MapCanvas canvas, final Player player) {
        if (this.rendered) return;
        this.rendered = true;

        BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.drawString(TextRenderer.this.text, 5, 12);
        if (TextRenderer.this.mapId != null)
            g.drawString("Map #" + TextRenderer.this.mapId.toString(), 70, 115);

        canvas.drawImage(0, 0, image);
    }

}
