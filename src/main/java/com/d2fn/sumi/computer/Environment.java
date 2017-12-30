package com.d2fn.sumi.computer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Environment extends Computer {

    private static final List<String> inputNames = List.of("time");
    private static final List<String> outputNames = List.of("time");

    private final AtomicLong clock = new AtomicLong(0L);

    @Override
    public List<String> getInputNames() {
        return inputNames;
    }

    @Override
    public List<String> getOutputNames() {
        return outputNames;
    }

    @Override
    public boolean hasInput(String name) {
        return true;
    }

    @Override
    public boolean hasOutput(String name) {
        return true;
    }

    public long runWithParameters(Map<String, Object> parameters) {
        final long time = clock.incrementAndGet();
        for(Map.Entry<String, Object> e : parameters.entrySet()) {
            final String name = e.getKey();
            final Object value = e.getValue();
//            super.deliverInput(e.getKey(), e.getValue(), time);
            inputs.put(name, new Mailbox(name, value, time));
        }
        super.deliverInput("time", time, time);
        if(!inputsReady(time)) {
            throw new RuntimeException("Environment inputs not ready!");
        }
        return time;
    }

    @Override
    protected void compute(long time) {
        // simply copy inputs to output
        for(Map.Entry<String, Mailbox> e : inputs.entrySet()) {
            final String name = e.getKey();
            final Mailbox mb = e.getValue();
            outputs.put(e.getKey(), new Mailbox(name, mb.getValue(), time));
        }
    }
}
