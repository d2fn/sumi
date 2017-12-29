package com.d2fn.sumi.computer;

import com.d2fn.sumi.Sketch;
import com.d2fn.sumi.computer.parser.BusParseException;
import com.d2fn.sumi.computer.parser.BusParser;

import java.util.Map;

public class PerlinLines extends Sketch {

    @Override
    public void draw() {
        background(0);
        stroke(255);
        strokeWeight(4f);
        try {
            final Bus bus = BusParser.parseResource("com/d2fn/sumi/computer/perlin-lines.bus");
            for(int y = 100; y < height; y += 500) {
                for(int x = 0; x < width; x += 50) {
                    bus.run(Map.of(
                            "x", x,
                            "y", y
                    ));
                    float height = bus.pollFloat("height", "value");
                    float starty = y - height/2f;
                    float endy   = y + height/2f;
                    line(x, starty, x, endy);
                }
            }
        }
        catch(BusParseException e) {
            System.err.println("error parsing line " + e.getErrorOffset());
            throw new RuntimeException(e);
        }
    }
}
