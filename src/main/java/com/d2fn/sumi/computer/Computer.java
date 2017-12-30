package com.d2fn.sumi.computer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Computer {

    protected final Map<String, Mailbox> inputs;
    protected final Map<String, Mailbox> outputs;

    public Computer() {
        this.inputs = new HashMap<>();
        for(String inputName : getInputNames()) {
            this.inputs.put(inputName, null);
        }
        this.outputs = new HashMap<>();
        for(String outputName : getOutputNames()) {
            this.outputs.put(outputName, null);
        }
    }

    /**
     * @return the named inputs
     */
    public abstract List<String> getInputNames();

    /**
     * @return the named outputs
     */
    public abstract List<String> getOutputNames();

    public Object poll(String name) {
        return outputs.get(name).getValue();
    }

    public double pollDouble(String name) {
        return ((Number)poll(name)).doubleValue();
    }

    public long pollLong(String name) {
        return ((Number)poll(name)).longValue();
    }

    public int pollInt(String name) {
        return ((Number)poll(name)).intValue();
    }

    public String pollString(String name) {
        return poll(name).toString();
    }

    /**
     * run computation on the input mailboxes and copy outputs to the output mailboxes
     * @param time - the time reading from the clock gating this computation
     * @throws IllegalStateException if the clock readings for all inputs are not in sync
     */
    protected abstract void compute(long time);

    /**
     * @param name - the name of the input being delivered
     * @param value - the input value being delivered
     * @param time - the time reading from the clock gating this computation
     */
    public void deliverInput(String name, Object value, long time) {
        final Mailbox mb = new Mailbox(name, value, time);
        inputs.put(name, mb);
        if(inputsReady(time)) {
            compute(time);
        }
    }

    /**
     * @return true if all inputs reflect the given clock cycle
     */
    public boolean inputsReady(long time) {
        for(Mailbox mb : inputs.values()) {
            if(mb == null) {
                return false;
            }
            if(mb.getTime() != time) {
                return false;
            }
        }
        return true;
    }

    public Object readOutput(String name) {
        return outputs.get(name).getValue();
    }

    protected Collection<Mailbox> getOutput() {
        return outputs.values();
    }

    public boolean hasInput(String name) {
        return getInputNames().contains(name);
    }

    public boolean hasOutput(String name) {
        return getOutputNames().contains(name);
    }
}
