package com.d2fn.sumi.command;

import java.io.PrintStream;

public class CommandResponse {

    private final boolean error;
    private final String message;
    private final Exception exception;

    public CommandResponse(boolean error, String message, Exception exception) {
        this.error = error;
        this.message = message;
        this.exception = exception;
    }

    public boolean isError() {
        return error;
    }

    public boolean hasException() {
        return exception != null;
    }

    public Exception getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public static CommandResponse error(String msg) {
        return new CommandResponse(true, msg, null);
    }

    public static CommandResponse error(String msg, Exception e) {
        return new CommandResponse(true, msg, e);
    }

    public static CommandResponse success() {
        return new CommandResponse(false, null, null);
    }

    public void printError(PrintStream err) {
        if(!isError()) {
            throw new IllegalArgumentException("cannot print error when response was success");
        }
        System.err.println(getMessage());
        if(hasException()) {
            exception.printStackTrace(err);
        }
    }
}
