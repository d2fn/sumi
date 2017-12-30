package com.d2fn.sumi;

import com.d2fn.sumi.command.Command;
import com.d2fn.sumi.command.CommandResponse;
import com.d2fn.sumi.command.Snapshot;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class Sketch extends PApplet {

    private Map<String, String> namedBusResources = new HashMap<>();

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
            snapshot();
        }
    }

    public void snapshot() {
        final Snapshot snapshot = Snapshot.build();
        final CommandResponse resp = snapshot.run();
        if(resp.isError()) {
            System.err.println("Error taking snapshot");
            System.err.println(resp.getMessage());
            if(resp.hasException()) {
                resp.getException().printStackTrace(System.err);
            }
        }
        // save a frame in the snapshot dir
        saveFrame(snapshot.getSnapshotDir() + File.separator + "snapshot.tif");
        // todo - save frame into a ledger on the parent sketch
    }
}
