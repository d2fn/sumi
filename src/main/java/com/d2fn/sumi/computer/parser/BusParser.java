package com.d2fn.sumi.computer;

public class BusParser {



    public void emitLine(String line) {
        if(line == null) return;
        line = line.trim();
        line = trimComment(line);
        if(line.length() == 0) return;
        if(line.contains("=")) {
            parseAssignment(line);
        }

    }

    private String trimComment(String line) {
        int commentIndex = line.indexOf("#");
        if(commentIndex != -1) {
            return line.substring(0, commentIndex);
        }
        return line;
    }
}
