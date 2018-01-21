package net.vowed.shops.transactions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 12/25/2015.
 */
public class TransactionUtil
{
    public static List<List<String>> getTransactions(UUID uuid) throws IOException
    {
        File file = new File("C:\\ProjectVowed\\plugins\\Vowed\\Transactions\\" + uuid.toString());

        List<List<String>> files = new ArrayList<>();
        if (file.listFiles() != null)
        {
            for (File fileFinder : file.listFiles())
            {
                FileReader fileReader = new FileReader(fileFinder);
                BufferedReader reader = new BufferedReader(fileReader);

                String line;
                List<String> lines = new ArrayList<>();
                try
                {
                    while ((line = reader.readLine()) != null)
                    {
                        lines.add(line + "\n");
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                lines.add(lines.size() - 1, fileFinder.getName().substring(fileFinder.getName().indexOf("_") + 1, fileFinder.getName().lastIndexOf(".")));
                files.add(lines);
            }
        }
        return files;
    }
}
