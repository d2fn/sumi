package com.d2fn.sumi.command;

import com.d2fn.sumi.Sketch;
import com.d2fn.sumi.SumiSettings;
import com.sampullara.cli.Args;

import java.lang.reflect.Constructor;

import static com.d2fn.sumi.Utils.tail;

public class Run implements Command {

    private final SumiSettings sumi;

    private Sketch sketch;
    private String[] processingArgs;
    private String[] sketchArgs;

    public Run(SumiSettings sumi, String[] args) {
        this.sumi = sumi;
        this.processingArgs = args;
    }

    @Override
    public CommandResponse run() {

        // todo parameterize package name in ~/.sumi
        final String sketchClassName = processingArgs[0];
        this.processingArgs[0] = sketchClassName;
        try {
            final Class<? extends Sketch> klass =
                    (Class<? extends Sketch>)Class.forName(sketchClassName);
            final Constructor<? extends Sketch> constructor = klass.getConstructor();
            this.sketchArgs = tail(processingArgs);
            this.sketch = constructor.newInstance();
            try {
                Args.parse(sketch, sketchArgs);
            }
            catch(Exception e) {
                final StringBuilder sb = new StringBuilder();
                for(int i = 0; i < sketchArgs.length; i++) {
                    sb.append(String.format("sketchArgs[%d] => %s", i, sketchArgs[i]) + "\n");
                }
                Args.usage(sketch);
                return CommandResponse.error("error parsing sketch arguments:\n" + sb.toString());
            }
        } catch (Exception e) {
            return CommandResponse.error("Error initializing sketch", e);
        }

        Sketch.runSketch(
                processingArgs,
                sketch
        );

        return CommandResponse.success();
    }
}
