package com.d2fn.sumi.computer;

import java.util.List;

public class SequenceToXY extends Computer {

    private static final List<String> inputNames = List.of("sequence", "width", "height");
    private static final List<String> outputNames = List.of("x", "y");

    @Override
    public List<String> getInputNames() {
        return inputNames;
    }

    @Override
    public List<String> getOutputNames() {
        return outputNames;
    }

    public Long readSequence() {
        return ((Number)inputs.get("sequence").getValue()).longValue();
    }

    public Long readWidth() {
        return ((Number)inputs.get("width").getValue()).longValue();
    }

    public Long readHeight() {
        return ((Number)inputs.get("height").getValue()).longValue();
    }

    @Override
    protected void compute(long time) {
        final long seq = readSequence();
        final long width = readWidth();
        final long height = readHeight();
        double x = seq % width;
        double y = seq / width;
        if(y >= height) {
            x = 0;
            y = 0;
        }
        outputs.put("x", new Mailbox("x", x, time));
        outputs.put("y", new Mailbox("y", y, time));
    }

    public double getX() {
        return (double)outputs.get("x").getValue();
    }

    public double getY() {
        return (double)outputs.get("y").getValue();
    }
}
