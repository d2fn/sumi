package com.d2fn.sumi.computer;

import java.util.Collections;
import java.util.List;

public class Delta extends Computer {

    private static final List<String> inputNames  = Collections.singletonList("value");
    private static final List<String> outputNames = Collections.singletonList("value");

    private Double previousValue;

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
        if(previousValue == null) {
            previousValue = ((Number)inputs.get("value").getValue()).doubleValue();
            outputs.put("value", new Mailbox("value", 0, time));
        }
        else {
            final double currentValue = ((Number)inputs.get("value").getValue()).doubleValue();
            final double delta = currentValue - previousValue;
            outputs.put("value", new Mailbox("value", delta, time));
        }
    }
}
