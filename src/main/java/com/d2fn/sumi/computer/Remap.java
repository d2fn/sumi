package com.d2fn.sumi.computer;

import com.d2fn.sumi.Sketch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Remap extends Computer {

    private static final List<String> inputNames = Arrays.asList("value", "domainlo", "domainhi", "rangelo", "rangehi", "clamp");
    private static final List<String> outputNames = Collections.singletonList("value");

    @Override
    public List<String> getInputNames() {
        return inputNames;
    }

    @Override
    public List<String> getOutputNames() {
        return outputNames;
    }

    public Object read(String name) {
        return inputs.get(name).getValue();
    }

    public float readFloat(String name) {
        return ((Number)read(name)).floatValue();
    }

    public boolean readBoolean(String name) {
        return Boolean.valueOf((String)read(name));
    }

    @Override
    protected void compute(long time) {
        final float input = readFloat("value");
        final float domainlo = readFloat("domainlo");
        final float domainhi = readFloat("domainhi");
        final float rangelo = readFloat("rangelo");
        final float rangehi = readFloat("rangehi");
        final boolean clamp = readBoolean("clamp");
        float output = Sketch.map(input, domainlo, domainhi, rangelo, rangehi);
        if(clamp) {
            if (output > rangehi) output = rangehi;
            if (output < rangelo) output = rangelo;
        }
        outputs.put("value", new Mailbox("value", output, time));
    }
}
