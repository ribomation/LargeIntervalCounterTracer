package com.ribomation.large_interval_counter;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * DESCRIPTION
 *
 */
public class IntegerUnit {
    private static class Unit {
        String  name, shortName;
        int     base;

        public Unit(int base, String name, String shortName) {
            this.base = base;
            this.name = name;
            this.shortName = shortName;
        }
    }

    private static final Unit[]     units = {
            new Unit(60 * 60 * 1000, "hour"       , "h"),
            new Unit(     60 * 1000, "minute"     , "m"),
            new Unit(          1000, "second"     , "s"),
            new Unit(             1, "milliSecond", "ms")
    };

    private int     rawValue, unitValue;
    private String  unitName;

    public IntegerUnit(int value) {
        rawValue = value;

        for (int i = 0; i < units.length; i++) {
            int  v    = value / units[i].base;
            if (v > 0) {
                unitValue = v;
                unitName  = units[i].name;
                break;
            }
        }
    }

    protected IntegerUnit(int value, String name) {
        unitName  = units[ units.length-1 ].name;
        unitValue = value;
        rawValue  = value * units[ units.length-1 ].base;

        for (int i = 0; i < units.length; i++) {
            if (units[i].shortName.equals(name)) {
                unitName = units[i].name;
                rawValue = value * units[i].base;
                break;
            }
        }
    }

    public static IntegerUnit   create(String txtValue) {
        Pattern  p = Pattern.compile("(\\d+)([hms]?)");
        Matcher  m = p.matcher(txtValue);
        if (m.matches()) {
            return new IntegerUnit(Integer.parseInt( m.group(1) ), m.group(2));
        }
        throw new IllegalArgumentException("Not a parseable integer unit: "+txtValue);
    }

    public String getUnitName() {
        return unitName;
    }

    public int getUnitValue() {
        return unitValue;
    }

    public int getValue() {
        return rawValue;
    }

    public String toString() {
        return getUnitValue() + " " + getUnitName() + (getUnitValue() > 1 ? "s" : "");
    }

}
