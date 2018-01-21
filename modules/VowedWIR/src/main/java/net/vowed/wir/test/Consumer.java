package net.vowed.wir.test;

import java.util.concurrent.BlockingQueue;

/**
 * Created by JPaul on 10/1/2016.
 */
public class Consumer implements Runnable
{
    protected BlockingQueue queue;

    public Consumer(BlockingQueue blockingQueue)
    {
        this.queue = blockingQueue;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println(queue.take());
            System.out.println(queue.take());
            System.out.println(queue.take());
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
