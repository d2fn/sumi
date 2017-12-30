package com.d2fn.sumi;

import com.d2fn.sumi.computer.*;
import com.d2fn.sumi.computer.parser.BusParseException;
import com.d2fn.sumi.computer.parser.BusParser;
import com.sampullara.cli.Argument;

import java.util.Map;

public class Example1 extends Sketch {

    @Argument(alias = "label", description = "Label", required = true)
    private String label;

    @Argument(alias = "labelx", description = "X")
    private Integer labelX;

    @Argument(alias = "labely", description = "Y")
    private Integer labelY;

    public void setup() {
        if (labelX == null) labelX = width / 2;
        if (labelY == null) labelY = height / 2;
    }

    @Override
    public void draw() {
        background(0);
        stroke(255, 0, 0);
        line(0, 0, width, height);
        textSize(45);
        text(label, labelX, labelY);
    }
}
