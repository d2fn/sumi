import com.d2fn.sumi.Sketch;
import com.sampullara.cli.Args;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("MAIN ==========> ");
        final String[] sketchArgs = new String[args.length+1];
        sketchArgs[0] = "${sketchName}";
        for(int i = 0; i < args.length; i++) {
            sketchArgs[i+1] = args[i];
            System.out.println(String.format("args[%d] => %s", i, args[i]));
        }
        final ${sketchName} sketch = new ${sketchName}();
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
