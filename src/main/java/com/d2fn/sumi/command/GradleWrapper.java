package com.d2fn.sumi.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class GradleWrapper implements Command {

    private final String gradleProjectDir;

    public GradleWrapper(String gradleProjectDir) {
        this.gradleProjectDir = gradleProjectDir;
    }

    @Override
    public CommandResponse run() {
        try {
            Process p = Runtime.getRuntime().exec("gradle wrapper -p " + gradleProjectDir);
            int returnCode = p.waitFor();
            if(returnCode != 0) {
                String errorMsg = readStream(p.getErrorStream());
                return CommandResponse.error(errorMsg);
            }
        } catch (IOException e) {
            return CommandResponse.error("error creating gradle wrapper for new sketch: " + gradleProjectDir, e);
        } catch (InterruptedException e) {
            return CommandResponse.error("gradle wrapper interrupted", e);
        }

        return CommandResponse.success();
    }

    private static String readStream(InputStream errorStream) {
        final StringBuilder out = new StringBuilder();
        final Scanner s = new Scanner(errorStream);
        while(s.hasNextLine()) {
            out.append(s.nextLine() + "\n");
        }
        return out.toString();
    }
}
