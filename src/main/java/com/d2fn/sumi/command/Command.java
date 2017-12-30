package com.d2fn.sumi.command;

import java.io.File;

public interface Command {

    CommandResponse run();

    public static void ensureDir(String s) {
        final File dir = new File(s);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        if(!dir.isDirectory()) {
            throw new RuntimeException(dir + " is not a directory");
        }
    }
}