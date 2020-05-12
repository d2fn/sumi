package com.d2fn.sumi;

import com.d2fn.sumi.computer.Bus;
import com.d2fn.sumi.computer.parser.BusParseException;
import com.d2fn.sumi.computer.parser.BusParser;

import java.util.Map;

public class PerlinLines extends Sketch {

    @Override
    public void draw() {
        background(0);
        stroke(255, 128);
        strokeWeight(3f);
        translate(width/2, height/3);
        scale(0.4f);
        try {
            final Bus bus = BusParser.parseResource("com/d2fn/sumi/computer/perlin-lines.bus");
            int xstep = 7;
            int ystep = 150;
            int v = 0;
            for(int y = -1500; y < height*2; y += ystep) {
                int u = 0;
                pushMatrix();
                translate(0, y);
                for(int x = 0; x < width*10; x += xstep) {
                    bus.run(
                            "x", x,
                            "y", y,
                            "u", u,
                            "v", v
                    );
                    final float height = bus.pollFloat("height", "value");
                    final float starty = -height/2f;
                    final float endy   =  height/2f;
                    final float dtheta = bus.pollFloat("dtheta", "value");
                    translate(xstep, 0);
                    rotate(dtheta);
                    line(0, starty, 0, endy);
                    u++;
                }
                popMatrix();
                v++;
            }
        }
        catch(BusParseException e) {
            System.err.println("error parsing line " + e.getErrorOffset());
            throw new RuntimeException(e);
        }
    }
}
