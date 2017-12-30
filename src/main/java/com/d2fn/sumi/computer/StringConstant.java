package com.d2fn.sumi.computer;

import java.util.List;

public class StringConstant extends Computer {

    private final String c;

    public StringConstant(String c) {
        this.c = c;
    }

    @Override
    public List<String> getInputNames() {
        return List.of("time");
    }

    @Override
    public List<String> getOutputNames() {
        return List.of("value");
    }

    @Override
    protected void compute(long time) {
        outputs.put("value", new Mailbox("value", c, time));
    }
}
