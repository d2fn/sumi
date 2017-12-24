import com.d2fn.sumi.Sketch;
import com.d2fn.sumi.Utils;

import com.sampullara.cli.Argument;

// auto-generated sketch class
// @Argument parameters are auto populated with command line options
// run with:
//   $sketchDir/run $(args)
public class ${sketchName} extends Sketch {

    @Argument(alias = "label", description = "Label", required = true)
    private String label;

    @Argument(alias = "labelx", description = "X")
    private Integer labelX;

    @Argument(alias = "labely", description = "Y")
    private Integer labelY;

    public void setup() {
        if(labelX == null) labelX = width/2;
        if(labelY == null) labelY = height/2;
    }

    @Override
    public void draw() {
        background(0);
        stroke(255, 0, 0);
        line(0, 0, width, height);
        line(0, height, width, 0);
        textSize(45);
        text(label, labelX, labelY);
    }
}
