package net.vowed.wir.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by JPaul on 8/25/2016.
 */
public class Test
{
    public static void main(String[] args)
    {


        /*
        List<TestObject> testObjects = Lists.newArrayList();

        for (int i = 0; i < 10; i++)
        {
            TestObject testObject = new TestObject(i);

            testObjects.add(testObject);
        }

        Collections.shuffle(testObjects);

        for (TestObject testObject : testObjects)
        {
            if (testObject.order == 0)
            {
                int index = testObjects.indexOf(testObject);

                Collections.swap(testObjects, index, 0);
            }
        }

        log(testObjects);
        log("ORDERED LIST: " + orderList(testObjects));
        */
    }

    public static List<TestObject> orderList(List<TestObject> testObjects)
    {
        List<TestObject> ordered = Arrays.asList(new TestObject[testObjects.size()]);
        ordered.set(0, testObjects.get(0));

        for (int i = 0; i < testObjects.size(); i++)
        {
            TestObject testObject = testObjects.get(i);
            TestObject compatible = findNextCompatible(testObject, testObjects);

            if (compatible != null)
            {
                int compatibleIndex = testObjects.indexOf(compatible);

                if (i != testObjects.size() - 1)
                {
                    Collections.swap(testObjects, compatibleIndex, i + 1);
                }
            }
        }

        return testObjects;
    }

    public static TestObject findNextCompatible(TestObject testObject, List<TestObject> testObjects)
    {
        for (TestObject object : testObjects)
        {
            if (testObject.isCompatible(object))
            {
                log("FOUND COMPATIBILITY: COMPATIBLE = " + object + " || OBJECT = " + testObject);

                return object;
            }
        }

        return null;
    }

    private static void log(Object object)
    {
        System.out.println(object.toString());
    }
}
