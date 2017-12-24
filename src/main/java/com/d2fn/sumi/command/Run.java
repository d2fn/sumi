package com.d2fn.sumi.command;

import com.d2fn.sumi.Sketch;
import com.sampullara.cli.Args;

import java.lang.reflect.Constructor;

import static com.d2fn.sumi.Utils.tail;

public class Run implements Command {

    private Sketch sketch;
    private String[] processingArgs;
    private String[] sketchArgs;

    public Run(String[] args) {
        this.processingArgs = args;
        // todo parameterize package name in ~/.sumi
        final String sketchClassName = String.format("com.d2fn.sumi.%s", args[0]);
        this.processingArgs[0] = sketchClassName;
        try {
            final Class<? extends Sketch> klass =
                    (Class<? extends Sketch>)Class.forName(sketchClassName);
            final Constructor<? extends Sketch> constructor = klass.getConstructor();
            this.sketchArgs = tail(args);
            this.sketch = constructor.newInstance();
            try {
                Args.parse(sketch, sketchArgs);
            }
            catch(Exception e) {
                Args.usage(sketch);
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Can't instantiate sketch class: " + sketchClassName);
            System.exit(1);
        }
    }

    @Override
    public void run() {
        Sketch.runSketch(
                processingArgs,
                sketch
        );
    }
}
