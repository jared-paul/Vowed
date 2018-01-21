package net.vowed.wir.generation;

import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by JPaul on 9/8/2016.
 */
public class Task
{
    LinkedList<Loader> loaderQueue = Lists.newLinkedList();

    public Task(List<Loader> toLoadOrder)
    {
        toLoadOrder.forEach(loader -> loaderQueue.offer(loader));
    }

    public void loadNext(Loader current)
    {
        int index = loaderQueue.indexOf(current);

        if (index != loaderQueue.size() - 1)
        {
            Loader next = loaderQueue.get(index + 1);

            next.load();
        }
    }
}
