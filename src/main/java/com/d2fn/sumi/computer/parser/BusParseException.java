package com.d2fn.sumi.computer.parser;

import java.text.ParseException;

public class BusParseException extends ParseException {
    public BusParseException(String message, int lineNumber) {
        super(message, lineNumber);
    }
}
