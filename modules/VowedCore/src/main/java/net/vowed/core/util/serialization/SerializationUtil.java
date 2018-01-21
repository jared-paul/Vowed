package net.vowed.core.util.serialization;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by JPaul on 3/19/2016.
 */
public class SerializationUtil
{
    public static String toBase64(Object object) throws IllegalStateException
    {
        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(object);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Unable to save " + object, e);
        }
    }

    public static Object fromBase64(String data)
    {
        try
        {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Object object = dataInput.readObject();

            dataInput.close();
            return object;
        }
        catch (ClassNotFoundException e)
        {
            try
            {
                throw new IOException("Unable to decode class setType", e);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
