package com.d2fn.sumi;

import com.d2fn.sumi.command.Command;
import com.d2fn.sumi.command.Create;
import com.d2fn.sumi.command.Run;

import java.lang.reflect.Constructor;

import static com.d2fn.sumi.Utils.tail;

public class Sumi {

    public static void main(String[] args) throws Exception {

        if(args.length == 0) {
            usage();
            System.exit(1);
        }

        final String commandName= args[0];

        try {
            final CommandType commandType = CommandType.valueOf(commandName);
            final Constructor<? extends Command> constructor =
                    commandType.getCommandClass().getConstructor(String[].class);
            final String[] commandArgs = tail(args);
            final Command command = constructor.newInstance(new Object[] { commandArgs });
            command.run();
        }
        catch(Exception e) {
            usage();
            System.exit(1);
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
            Class getCommandClass() {
                return Create.class;
            }
        },

        run {
            @Override
            Class getCommandClass() {
                return Run.class;
            }
        }
        ;

        abstract Class<? extends Command> getCommandClass();
    }
}
