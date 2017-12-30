package com.d2fn.sumi;

import com.sampullara.cli.Args;

public class Main {
    public static void main(String[] args) throws Exception {
        final String[] sketchArgs = new String[args.length+1];
        sketchArgs[0] = "foo";
        for(int i = 0; i < args.length; i++) {
            sketchArgs[i+1] = args[0];
        }
        final Example1 sketch = new Example1();
        try {
            Args.parse(sketch, args);
            Sketch.runSketch(
                    sketchArgs,
                    sketch
            );
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            Args.usage(sketch);
            System.exit(64);
            throw e;
        }
    }
}
