package com.d2fn.sumi.computer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Scale extends Computer {

    private static final List<String> inputNames = Arrays.asList("coefficient", "value");
    private static final List<String> outputNames = Collections.singletonList("value");

    @Override
    public List<String> getInputNames() {
        return inputNames;
    }

    @Override
    public List<String> getOutputNames() {
        return outputNames;
    }

    public double readCoefficient() {
        return ((Number)inputs.get("coefficient").getValue()).doubleValue();
    }

    public double readInputValue() {
        return ((Number)inputs.get("value").getValue()).doubleValue();
    }

    @Override
    protected void compute(long time) {
        final double coefficient = readCoefficient();
        final double in = readInputValue();
        final double out = coefficient * in;
        outputs.put("value", new Mailbox("value", out, time));
    }
}
