package com.d2fn.sumi;

import processing.core.PApplet;
import processing.event.KeyEvent;

public abstract class Sketch extends PApplet {

    public Sketch() {
    }

    @Override
    public void settings() {
        fullScreen();
    }

    @Override
    public abstract void draw();

    public void keyPressed(KeyEvent e) {

        super.keyPressed();
        if(key == ' ') {
            // snapshot raster graphics + code
            snapshotCode();
            snapshotRaster();
        }
        else if(key == 'p') {
            snapshotCode();
            snapshotPdf();
        }

    }

    private void snapshotPdf() {
        System.err.println("pdf snapshot invoked but not yet implemented");
    }

    public void snapshotRaster() {
        System.err.println("raster snapshot invoked but not yet implemented");
    }

    public void snapshotCode() {
        System.err.println("code snapshot invoked but not yet implemented");
    }
}
