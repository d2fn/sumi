package com.d2fn.sumi.command;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.d2fn.sumi.command.Command.ensureDir;

public class Snapshot implements Command {

    private final String snapshotDir;
    private final LocalDateTime snapshotTime;
    private final List<Command> commands;

    public Snapshot() {
        this.snapshotTime = LocalDateTime.now();
        final String snapshotName =
                snapshotTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .replace("T", "_")
                        .replace(":", "_")
                        .replace(".", "_")
                        .replace("-", "_");
        final String basedir = System.getProperty("user.dir");
        this.snapshotDir = basedir + File.separator + "snapshots" + File.separator + snapshotName;
        ensureDir(this.snapshotDir);
        this.commands = new ArrayList<>();
    }

    public String getSnapshotDir() {
        return snapshotDir;
    }

    public LocalDateTime getSnapshotTime() {
        return snapshotTime;
    }

    void addCommand(Command c) {
        commands.add(c);
    }

    @Override
    public CommandResponse run() {

        for (Command c : commands) {
            CommandResponse resp = c.run();
            if (resp.isError()) {
                return resp;
            }
        }
        return CommandResponse.success();
    }

    public static Snapshot build() {

        final Snapshot snapshot = new Snapshot();

        final String basedir = System.getProperty("user.dir");
        final List<Command> commands = new ArrayList<>();

        // build.gradle
        snapshot.addCommand(
                copyFile("build.gradle",
                        basedir, snapshot.getSnapshotDir()));
        // run script
        snapshot.addCommand(
                copyFile("run",
                        basedir, snapshot.getSnapshotDir()));
        // lib dir
        snapshot.addCommand(
                copyDir("lib",
                        basedir, snapshot.getSnapshotDir() + File.separator + "lib"));
        // src dir
        snapshot.addCommand(
                copyDir("src",
                        basedir, snapshot.getSnapshotDir() + File.separator + "src"));
        // data dir
        snapshot.addCommand(
                copyDir("data",
                        basedir, snapshot.getSnapshotDir() + File.separator + "data"));

        // gradle wrapper
        snapshot.addCommand(
                new GradleWrapper(snapshot.getSnapshotDir()));

        return snapshot;
    }

    private static CopyFileOperation copyFile(String fileName, String srcDir, String dstDir) {
        return new CopyFileOperation(
                srcDir + File.separator + fileName,
                dstDir + File.separator + fileName
        );
    }

    private static CopyDirOperation copyDir(String dirName, String srcDir, String dstDir) {
        return new CopyDirOperation(
                srcDir + File.separator + dirName,
                dstDir);
    }
}
