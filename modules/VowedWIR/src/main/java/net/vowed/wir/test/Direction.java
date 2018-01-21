package net.vowed.wir.test;

import org.bukkit.block.BlockFace;

/**
 * Created by JPaul on 9/3/2016.
 */
public enum Direction
{
    NORTH(BlockFace.NORTH, 0),
    SOUTH(BlockFace.SOUTH, 1),
    WEST(BlockFace.WEST, 2),
    EAST(BlockFace.EAST, 3);

    private BlockFace faceDirection;
    private byte byteDirection;

    Direction(BlockFace faceDirection, int byteDirection)
    {
        this.faceDirection = faceDirection;
        this.byteDirection = (byte) byteDirection;
    }

    public BlockFace getFaceDirection()
    {
        return faceDirection;
    }

    public byte getByteDirection()
    {
        return byteDirection;
    }

    public static Direction fromByte(byte direction)
    {
        for (Direction dir : Direction.values())
        {
            if (dir.byteDirection == direction)
            {
                return dir;
            }
        }

        return null;
    }
}
