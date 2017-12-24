package com.d2fn.sumi;

import com.d2fn.sumi.command.Command;
import com.d2fn.sumi.command.CommandResponse;
import com.d2fn.sumi.command.Create;
import com.d2fn.sumi.command.Run;

import java.lang.reflect.Constructor;

import static com.d2fn.sumi.Utils.tail;

public class Sumi {

    public static void main(String[] args) throws Exception {

        // todo -- load from ~/.sumi
        final SumiSettings sumi = new SumiSettings("/home/d/bin/sumi", "sumi.jar");

        final String commandName= args[0];

        final CommandType commandType = CommandType.valueOf(commandName);
        final Constructor<? extends Command> constructor =
                commandType.getCommandClass().getConstructor(SumiSettings.class, String[].class);
        final String[] commandArgs = tail(args);
        final Command command = constructor.newInstance(sumi, commandArgs);
        final CommandResponse resp = command.run();
        if(resp.isError()) {
            resp.printError(System.err);
        }
    }

    private static void usage() {
        System.out.println("Valid commands");
        for(CommandType c : CommandType.values()) {
            System.out.println(" - " + c);
        }
    }

    enum CommandType {

        create {
            @Override
            Class<? extends Command> getCommandClass() {
                return Create.class;
            }
        },

        run {
            @Override
            Class<? extends Command> getCommandClass() {
                return Run.class;
            }
        }
        ;

        abstract Class<? extends Command> getCommandClass();
    }
}
