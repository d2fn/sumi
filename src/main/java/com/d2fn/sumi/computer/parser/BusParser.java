package com.d2fn.sumi.computer.parser;

import com.d2fn.sumi.computer.*;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.*;

public class BusParser {

    private final Environment env;
    private Map<String, Computer> computersByName;
    private final List<Connection> connections;

    public BusParser() {
        this.env = new Environment();
        this.computersByName = new HashMap<>();
        this.computersByName.put("env", env);
        this.connections = new ArrayList<>();
    }

    private void emitLine(String line, int lineNumber) throws BusParseException {
        if(line == null) return;
        line = line.trim();
        line = trimComment(line);
        if(line.length() == 0) return;
        if(line.contains("=")) {
            parseAssignment(line, lineNumber);
        }
        if(line.contains("->")) {
            parseConnection(line, lineNumber);
        }
    }

    public Bus build() {
        return new Bus(env, computersByName, connections);
    }

    private void parseAssignment(String line, int lineNumber) throws BusParseException {
        final String[] parts = line.split("=");
        if(parts.length != 2) {
            throw new BusParseException(String.format("Expecting two assignment operands, found %d: %s", parts.length, line), lineNumber);
        }
        final String name = parts[0].trim();
        final String className = parts[1].trim();
        try {
            final Class<? extends Computer> cclass = (Class<? extends Computer>)Class.forName(className);
            final Constructor<? extends Computer> constructor = cclass.getConstructor();
            final Computer computer = constructor.newInstance();
            computersByName.put(name, computer);
        } catch (ClassNotFoundException e) {
            throw new BusParseException(
                    String.format("Class not found: %s", className),
                    lineNumber);
        } catch (NoSuchMethodException e) {
            throw new BusParseException(
                    String.format("Default, no argument constructor on Computer class %s not found", className),
                    lineNumber);
        } catch (Exception e) {
            throw new BusParseException(
                    String.format("Error initializing Computer class %s: %s", className, e.getMessage()),
                    lineNumber);
        }
    }

    private void parseConnection(String line, int lineNumber) throws BusParseException {
        final String[] parts = line.split("->");
        if(parts.length < 2) {
            throw new BusParseException(String.format("Expecting > 2 connection operands, found %d: %s", parts.length, line), lineNumber);
        }
        // parse connection pairs allowing inputs and outputs to be chained together for easier readability
        for(int i = 1; i < parts.length; i++) {
            final String srcDescriptor = parts[i-1].trim();
            final String dstDescriptor = parts[i].trim();
            final Connection conn = parseConnectionComponents(srcDescriptor, dstDescriptor, lineNumber);
            connections.add(conn);
        }
    }

    private boolean canParseAsNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    private Connection parseConnectionComponents(String srcDescriptor, String dstDescriptor, int lineNumber) throws BusParseException {

        Port src;
        Port sink;

        if(srcDescriptor.contains(".") && !canParseAsNumber(srcDescriptor)) {
            src = dereferencePort(srcDescriptor, lineNumber);
        }
        else {
            src = dereferenceConstant(srcDescriptor, lineNumber);
        }

        sink = dereferencePort(dstDescriptor, lineNumber);

        return src.to(sink, lineNumber);
    }

    private Port dereferencePort(final String portDescriptor, int lineNumber) throws BusParseException {
        // dereference
        final String[] parts = portDescriptor.split("\\.");
        if(parts.length != 2) {
            throw new BusParseException(String.format("Expected computerName.outputProperty, found %s", portDescriptor), lineNumber);
        }
        final String srcComputerName = parts[0].trim();
        if(!computersByName.containsKey(srcComputerName)) {
            throw new BusParseException(String.format("No compute with name '%s' found", srcComputerName), lineNumber);
        }
        final String srcName = parts[1].trim();
        return new Port(srcComputerName, srcName);
    }

    private Port dereferenceConstant(final String portDescriptor, int lineNumber) throws BusParseException {
        if(portDescriptor.startsWith("\"") && portDescriptor.endsWith("\"")) {
            final String str = portDescriptor.substring(1, portDescriptor.length()-1);
            if(!computersByName.containsKey(portDescriptor)) {
                computersByName.put(portDescriptor, new StringConstant(str));
            }
            final Computer src = computersByName.get(portDescriptor);
            connections.add(new Connection(env, "time", src, "time"));
            return new Port(portDescriptor, "value");
        }
        // assume numeric constant, puke otherwise
        try {
            // create constant computer
            final double constant = Double.parseDouble(portDescriptor);
            if(!computersByName.containsKey(portDescriptor)) {
                computersByName.put(portDescriptor, new DoubleConstant(constant));
            }
            final Computer src = computersByName.get(portDescriptor);
            // connect to the compute root
            connections.add(new Connection(env, "time", src, "time"));
            return new Port(portDescriptor, "value");
        } catch (NumberFormatException e) {
            throw new BusParseException(String.format("Expected computerName.outputProperty or numeric constant, found neither: ", portDescriptor), lineNumber);
        }
    }

    private String trimComment(String line) {
        int commentIndex = line.indexOf("#");
        if(commentIndex != -1) {
            return line.substring(0, commentIndex);
        }
        return line;
    }

    public static Bus parseResource(String resource) throws BusParseException {
        int lineNumber = 1;
        try(final Scanner s = open(resource)) {
            BusParser parser = new BusParser();
            while(s.hasNextLine()) {
                parser.emitLine(s.nextLine(), lineNumber);
                lineNumber++;
            }
            return parser.build();
        }
    }

    private static Scanner open(String resource) {
        final InputStream is = Bus.class.getClassLoader().getResourceAsStream(resource);
        return new Scanner(is);
    }

    private class Port {

        private final String computer;
        private final String name;

        Port(String computer, String name) {
            this.computer = computer;
            this.name = name;
        }

        Connection to(Port sink, int lineNumber) throws BusParseException {
            final Computer src = computersByName.get(this.computer);
            final Computer dst = computersByName.get(sink.computer);
            if(!src.hasOutput(this.name)) {
                throw new BusParseException(
                        String.format(
                                "Output property '%s' not found for computer of type %s",
                                this.name, src.getClass().getCanonicalName()), lineNumber);
            }
            if(!dst.hasInput(sink.name)) {
                throw new BusParseException(
                        String.format(
                                "Input property '%s' not found for computer of type %s",
                                this.name, dst.getClass().getCanonicalName()), lineNumber);
            }
            return new Connection(src, this.name, dst, sink.name);
        }
    }
}
