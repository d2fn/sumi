package com.d2fn.sumi.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

class CopyFileOperation {

    private final String src;
    private final String dst;

    public CopyFileOperation(String src, String dst) {
        this.src = src;
        this.dst = dst;
    }

    /**
     * @return an optional CommandResponse in the event of an error during the copy
     */
    public Optional<CommandResponse> run() {
        try {
            Files.copy(
                    Paths.get(src),
                    Paths.get(dst)
            );
        } catch (IOException e) {
            return Optional.of(
                    CommandResponse.error(
                            String.format("Error during copy operation: %s -> %s", src, dst),
                            e)
            );
        }
        return Optional.empty();
    }
}
