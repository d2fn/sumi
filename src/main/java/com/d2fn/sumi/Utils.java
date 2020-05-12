package com.d2fn.sumi;

public class Utils {

    public static String[] tail(String[] args) {
        if(args.length == 1) {
            return new String[0];
        }
        final String[] out = new String[args.length-1];
        System.arraycopy(args, 1, out, 0, out.length);
        return out;
    }

    private Utils() {}
}
