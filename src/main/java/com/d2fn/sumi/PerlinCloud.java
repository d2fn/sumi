package com.d2fn.sumi;

import com.d2fn.sumi.computer.Bus;
import com.d2fn.sumi.computer.parser.BusParseException;
import com.d2fn.sumi.computer.parser.BusParser;

import java.util.Map;

public class PerlinCloud extends Sketch {

    public void setup() {
    }

    public void draw() {
        try {
            Bus bus = BusParser.parseResource("com/d2fn/sumi/computer/perlin-cloud.bus");
            final int blockSize = 5;
            for (int x = 0; x < width; x += blockSize) {
                for (int y = 0; y < height; y += blockSize) {
                    bus.run(Map.of(
                            "x", x,
                            "y", y
                    ));
                    int c = color(
                            bus.pollInt("r", "value"),
                            bus.pollInt("g", "value"),
                            bus.pollInt("b", "value")
                    );
                    fill(c);
                    noStroke();
                    rect(x, y, blockSize, blockSize);
                }
            }
        } catch (BusParseException e) {
            System.err.println("error parsing line " + e.getErrorOffset());
            throw new RuntimeException(e);
        }
        System.out.println(String.format("finished frame %d", frameCount));
    }
}
