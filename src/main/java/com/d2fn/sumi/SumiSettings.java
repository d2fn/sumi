package com.d2fn.sumi;

public class SumiSettings {

    private String sumiHome;
    private String sumiJarName;

    public SumiSettings(String sumiHome, String sumiJarName) {
        this.sumiHome = sumiHome;
        this.sumiJarName = sumiJarName;
    }

    public String getSumiHome() {
        return sumiHome;
    }

    public String getSumiJarName() {
        return sumiJarName;
    }
}
