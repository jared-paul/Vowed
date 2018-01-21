package net.vowed.wir.test;

/**
 * Created by JPaul on 8/25/2016.
 */
public class TestObject implements Comparable<TestObject>
{
    int order;

    public TestObject(int place)
    {
        this.order = place;
    }

    public boolean isCompatible(TestObject other)
    {
        return order + 1 == other.order;
    }

    public int distanceTo(TestObject other)
    {
        return order - other.order;
    }

    @Override
    public int compareTo(TestObject o)
    {
        return distanceTo(o);
    }

    @Override
    public String toString()
    {
        return String.valueOf(order);
    }
}
