package com.ribomation.large_interval_counter;

import junit.framework.TestCase;

/**
 * Unit test of class IntegerUnit.
 */
public class IntegerUnit_Test extends TestCase {

    // ----------------------------------------------------------
    // Tests
    // ----------------------------------------------------------

    public void test_create() {
        assertEquals("", "15 seconds", IntegerUnit.create("15s").toString());
        assertEquals("", "17 minutes", IntegerUnit.create("17m").toString());
        assertEquals("", "1 hour", IntegerUnit.create("1h").toString());
        assertEquals("", "30 milliSeconds", IntegerUnit.create("30").toString());
    }

    // ----------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------

    // ----------------------------------------------------------
    // Test Fixture
    // ----------------------------------------------------------
    private IntegerUnit target;

    protected void setUp() {
//        target = new IntegerUnit();
    }

    protected void tearDown() {
        target = null;
    }

    public IntegerUnit_Test(String name) {
        super(name);
    }
}
