package com.d2fn.sumi.computer;

import com.flowpowered.noise.NoiseQuality;

import java.util.List;

public class Perlin extends Computer {

    private static final List<String> inputNames =
            List.of(
                    // perlin configurations
                    "frequency",
                    "lacunarity",
                    "octavecount",
                    "persistence",
                    "quality",
                    "seed",
                    // spatial
                    "x", "y", "z"
            );

    private static final List<String> outputNames =
            List.of(
                    "value"
            );

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

    public double readDouble(String name) {
        return ((Number)read(name)).doubleValue();
    }

    public long readLong(String name) {
        return ((Number)read(name)).longValue();
    }

    public int readInt(String name) {
        return ((Number)read(name)).intValue();
    }

    public String readString(String name) {
        return read(name).toString();
    }

    @Override
    protected void compute(long time) {
        final com.flowpowered.noise.module.source.Perlin pm =
                new com.flowpowered.noise.module.source.Perlin();
        pm.setPersistence(readDouble("persistence"));
        pm.setOctaveCount(readInt("octavecount"));
        pm.setLacunarity(readDouble("lacunarity"));
        pm.setFrequency(readDouble("frequency"));
        pm.setSeed(readInt("seed"));
        pm.setNoiseQuality(NoiseQuality.valueOf(readString("quality")));
        final double value = pm.getValue(readDouble("x"), readDouble("y"), readDouble("z"));
        outputs.put("value", new Mailbox("value", value, time));
    }
}
