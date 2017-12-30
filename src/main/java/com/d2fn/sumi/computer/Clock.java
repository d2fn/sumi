package com.d2fn.sumi.computer;

import java.util.List;

/**
 * Simple computer that maintains an internal clock, synthesizes clock readings
 * as input, and delivers the clock readings to an output mailbox for consumers
 *
 * @author Dietrich Featherston
 */
public class Clock extends Computer {

    private static final List<String> inputNames = List.of("time");
    private static final List<String> outputNames = List.of("time");


    @Override
    public List<String> getInputNames() {
        return inputNames;
    }

    @Override
    public List<String> getOutputNames() {
        return outputNames;
    }

    @Override
    protected void compute(long time) {
        outputs.put("clock", new Mailbox("time", time, time));
    }
}
