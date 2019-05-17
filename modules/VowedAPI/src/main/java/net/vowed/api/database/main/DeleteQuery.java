package net.vowed.api.database.main;

import com.google.common.collect.Lists;

import java.util.List;

public class DeleteQuery extends Query
{
    private List<String> wheres = Lists.newArrayList();

    public DeleteQuery(String table)
    {
        super(table);
    }

    public DeleteQuery where(String where)
    {
        wheres.add(where);
        return this;
    }

    public DeleteQuery and(String and)
    {
        where(and);
        return this;
    }

    public String build()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM ")
                .append(table);

        if (wheres.size() > 0)
        {
            stringBuilder.append(" WHERE ")
                    .append(separate(wheres, " AND "));
        }

        return stringBuilder.toString();
    }
}
