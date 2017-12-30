package com.d2fn.sumi.command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.d2fn.sumi.command.Command.ensureDir;

class CopyDirOperation implements Command {

    private final String src;
    private final String dst;

    public CopyDirOperation(String src, String dst) {
        this.src = src;
        this.dst = dst;
    }

    /**
     * @return an optional CommandResponse in the event of an error during the copy
     */
    public CommandResponse run() {

        final Path srcPath = Paths.get(src);
        final Path dstPath = Paths.get(dst);

        ensureDir(dst);

        final AtomicBoolean error = new AtomicBoolean(false);

        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream.forEach(sourcePath -> {
                final Path dstFilePath = dstPath.resolve(srcPath.relativize(sourcePath));
                if (sourcePath.toFile().isDirectory()) {
                    ensureDir(dstFilePath.toString());
                } else {
                    final CopyFileOperation copyOperation = new CopyFileOperation(sourcePath.toString(), dstFilePath.toString());
                    final CommandResponse resp = copyOperation.run();
                    if (resp.isError()) {
                        error.set(true);
                        System.err.println(resp.getMessage());
                        resp.getException().printStackTrace(System.err);
                    }
                }
            });
        } catch (IOException e) {
            return CommandResponse.error("Error walking src directory " + src, e);
        }
        if (error.get()) {
            return CommandResponse.error(String.format("One or more copy operations failed: %s -> %s", src, dst));
        }
        return CommandResponse.success();
    }
}
