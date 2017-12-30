package com.d2fn.sumi.computer;

public class Connection {

    // src
    private final Computer src;
    private final String srcName;

    // dst
    private final Computer dst;
    private final String dstName;

    public Connection(Computer src, String srcName, Computer dst, String dstName) {
        this.src = src;
        this.srcName = srcName;
        this.dst = dst;
        this.dstName = dstName;
    }

    public Computer getSrc() {
        return src;
    }

    public String getSrcName() {
        return srcName;
    }

    public Computer getDst() {
        return dst;
    }

    public String getDstName() {
        return dstName;
    }
}
