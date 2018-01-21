package net.vowed.core.util.fetchers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.vowed.api.TaskChain;
import net.vowed.api.plugin.Vowed;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NameFetcher implements Callable<Map<UUID, String>>
{
    private final JSONParser jsonParser = new JSONParser();
    private final List<UUID> uuids;
    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final static Map<UUID, String> names = Maps.newHashMap();

    public NameFetcher(List<UUID> uuids)
    {
        this.uuids = ImmutableList.copyOf(uuids);
    }

    @Override
    public Map<UUID, String> call() throws Exception
    {
        Map<UUID, String> uuidStringMap = new HashMap<>();

        for (UUID uuid : uuids)
        {
            URL name = new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replaceAll("-", "") + "/names");
            BufferedReader in = new BufferedReader(new InputStreamReader(name.openStream()));
            String string = in.readLine();
            in.close();

            JSONArray jsonArray = (JSONArray) jsonParser.parse(string);
            uuidStringMap.put(uuid, (String) ((JSONObject) jsonArray.get(jsonArray.size() - 1)).get("name"));
        }

        return uuidStringMap;
    }

    public static TaskChain<String> getNameOfAsync(UUID playerUUID)
    {
        return TaskChain
                .newChain()
                .asyncFirst(() ->
                            {
                                try
                                {
                                    if (names.get(playerUUID) == null)
                                    {
                                        String name = new NameFetcher(Collections.singletonList(playerUUID)).call().get(playerUUID);

                                        names.put(playerUUID, name);

                                        return name;
                                    }
                                    else
                                    {
                                        return names.get(playerUUID);
                                    }
                                } catch (Exception e)
                                {
                                    Vowed.LOG.warning("NAME FETCHER ERROR: Error fetching " + playerUUID.toString() + "'s name");
                                    e.printStackTrace();
                                }

                                return null;
                            });
    }

    public static String getNameOfBlocking(UUID playerUUID)
    {
        try
        {
            if (names.get(playerUUID) == null)
            {
                String name = new NameFetcher(Collections.singletonList(playerUUID)).call().get(playerUUID);

                names.put(playerUUID, name);

                return name;
            }
            else
            {
                return names.get(playerUUID);
            }
        } catch (Exception e)
        {
            Vowed.LOG.warning("NAME FETCHER ERROR: Error fetching " + playerUUID.toString() + "'s name");
            e.printStackTrace();
        }

        return null;
    }
}
