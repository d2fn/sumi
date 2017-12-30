package com.d2fn.sumi.computer;

import java.util.*;

public class Bus {

    private final Environment env;
    private final Map<String, Computer> computersByName;
    private final Map<Computer, List<Connection>> outputConnections;

    public Bus(Environment env, Map<String, Computer> computersByName, List<Connection> connections) {
        this.env = env;
        this.computersByName = computersByName;
        this.outputConnections = new HashMap<>();
        for(Connection c : connections) {
            outputConnections.putIfAbsent(c.getSrc(), new ArrayList<>());
            outputConnections.get(c.getSrc()).add(c);
        }
    }

    public void run() {
        run(new HashMap<>());
    }

    public void run(Map<String, Object> parameters) {
        final long time = env.runWithParameters(parameters);
        if(env.inputsReady(time)) {
            propagateDownstream(env);
        }
        else {
            throw new RuntimeException("root not ready");
        }
    }

    public double pollDouble(String computerName, String name) {
        return ((Number)poll(computerName, name)).doubleValue();
    }

    public float pollFloat(String computerName, String name) {
        return ((Number)poll(computerName, name)).floatValue();
    }

    public long pollLong(String computerName, String name) {
        return ((Number)poll(computerName, name)).longValue();
    }

    public int pollInt(String computerName, String name) {
        return ((Number)poll(computerName, name)).intValue();
    }

    public Object poll(String computerName, String name) {
        return computersByName.get(computerName).readOutput(name);
    }

    private void propagateDownstream(Computer src) {
        final Collection<Mailbox> outputs = src.getOutput();
        final List<Connection> downstream = outputConnections.get(src);
        if(outputConnections.containsKey(src)) {
            for (Connection c : downstream) {
                final Computer dst = c.getDst();
                final String dstName = c.getDstName();
                for (Mailbox mb : outputs) {
                    if (mb.getName().equals(c.getSrcName())) {
                        dst.deliverInput(dstName, mb.getValue(), mb.getTime());
                    }
                    if (dst.inputsReady(mb.getTime())) {
                        propagateDownstream(dst);
                    }
                }
            }
        }
    }

    private String getNameFor(Computer src) {
        for(Map.Entry<String, Computer> e : computersByName.entrySet()) {
            if(e.getValue() == src) {
                return e.getKey();
            }
        }
        return "unknown name, class = " + src.getClass().getCanonicalName();
    }
}
