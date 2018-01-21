package net.vowed.api.clans.billboard;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by JPaul on 2017-05-16.
 */
public interface IBoardImage
{
    BufferedImage getImage();

    File getImageFile();

    void save() throws IOException;

    boolean place(Block block, BlockFace direction, boolean isLoading);
}
