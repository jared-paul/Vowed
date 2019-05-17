package net.vowed.api.database.main;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertQuery extends Query
{
    private LinkedHashMap<String, String> values = Maps.newLinkedHashMap();
    private LinkedHashMap<String, String> duplicateKeyValues = Maps.newLinkedHashMap();
    private boolean onDuplicateKeyUpdate = false;

    public InsertQuery(String table)
    {
        super(table);
    }

    //remember sqlinjection
    public InsertQuery value(String column, String value)
    {
        values.put(column, value);
        return this;
    }

    public InsertQuery value(String column)
    {
        values.put(column, "?");
        return this;
    }

    public InsertQuery onDuplicateKeyUpdate()
    {
        onDuplicateKeyUpdate = true;
        return this;
    }

    public InsertQuery setDuplicate(String column, String value)
    {
        duplicateKeyValues.put(column, value);
        return this;
    }

    public String build()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ")
                .append(table)
                .append(" (")
                .append(separate(values.keySet(), ","))
                .append(")")
                .append(" VALUES (")
                .append(separate(values.values(), ","))
                .append(")");

        if (onDuplicateKeyUpdate)
        {
            stringBuilder.append(" ON DUPLICATE KEY UPDATE ");
            String separator = "";
            for (Map.Entry<String, String> duplicateEntry : duplicateKeyValues.entrySet())
            {
                String column = duplicateEntry.getKey();
                String value = duplicateEntry.getValue();
                stringBuilder.append(separator)
                        .append(column)
                        .append("=")
                        .append(value);
                separator = ",";
            }
        }

        return stringBuilder.toString();
    }
}
