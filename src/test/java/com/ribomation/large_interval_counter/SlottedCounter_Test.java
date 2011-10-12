package com.ribomation.large_interval_counter;

import junit.framework.TestCase;


public class SlottedCounter_Test extends TestCase {
    int  interval = 1000;
    int  puls     = 50;

    public void test_usages() {
        SlottedCounter target = new SlottedCounter(3);

        assertEquals("value", 0, target.value());

        target.push(10);
        assertEquals("value", 10, target.value());

        target.push(20);
        assertEquals("value", 30, target.value());

        target.push(30);
        assertEquals("value", 60, target.value());

        target.push(40);
        assertEquals("value", 90, target.value());

        target.push(50);
        assertEquals("value", 120, target.value());

        target.push(60);
        assertEquals("value", 150, target.value());
    }

    public void     test_array() {
        run("BoundedBuffer", new SlottedCounter(interval / puls), interval, puls);
    }

    public void run(String name, SlottedCounter target, int interval, int puls) {
        System.out.println("Running SlottedCounter test using " + name + "...");
        int   idx = 1, K = 10, n = interval / puls;

        long  warmUp  = System.currentTimeMillis() + interval;
        for (int k = 1; System.currentTimeMillis() < warmUp; ++k) {
            target.push(K);
            System.out.println(idx++ + "] value = " + target.value());
            assertEquals("", k * K, target.value());
            sleep(puls);
        }

        long  endTime = System.currentTimeMillis() + 4 * interval;
        while (System.currentTimeMillis() < endTime) {
            target.push(K);
            System.out.println(idx++ + "] value = " + target.value());
            assertEquals("", n * K, target.value());
            sleep(puls);
        }

        long  coolDown  = System.currentTimeMillis() + 2*interval;
        while (System.currentTimeMillis() < coolDown) {
            target.push(0);
            System.out.println(idx++ + "] value = " + target.value());
            sleep(2*puls);
        }

        assertEquals("", 0, target.value());
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
    }

    public SlottedCounter_Test(String name) {
        super(name);
    }
}
