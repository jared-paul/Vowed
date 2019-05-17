package net.vowed.api.database.main;

import java.util.Collection;

public abstract class Query
{
    protected String table;

    public Query(String table)
    {
        this.table = table;
    }

    protected String separate(Collection<String> collection, String separator)
    {
        StringBuilder builder = new StringBuilder();
        String sep = "";
        for (String item : collection)
        {
            builder.append(sep)
                    .append(item);
            sep = separator;
        }
        return builder.toString();
    }
}

