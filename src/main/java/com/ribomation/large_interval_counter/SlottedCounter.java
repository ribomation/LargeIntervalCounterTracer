package com.ribomation.large_interval_counter;

/**
 * Manages an fixed array of counters, in such a way that it maintains the sum of all counts
 * and when a new count is pushed in front, the last one is popped (if full) plus the
 * total sum is adjusted.
 */
public class SlottedCounter {
    private long[]              buffer;
    private int                 idx = 0;
    private long                sum = 0;

    public SlottedCounter(int numCounters) {
        buffer = new long[numCounters];
    }

    public long     value() {
        return sum;
    }

    public void     push(long value) {
        sum        -= buffer[idx];
        sum        += value;
        buffer[idx] = value;
        idx         = (idx + 1) % buffer.length;
    }
}
