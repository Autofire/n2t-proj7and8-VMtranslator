//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT;

import VMT.Commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Stack;

/**
 * Serves as a wrapper for a vm file. This will run through,
 * line by line, parsing each one and converting it to a command
 * object. The reader is NOT automatically closed.
 */
public class Parser {

    private BufferedReader reader;

    public Parser(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Checks if there are (possibly) more commands in the file.
     * @return False if EOF is reached; true otherwise.
     * @throws IOException if there was an issue reading the file.
     */
    public boolean hasMoreCommands() throws IOException {
        return reader.ready();
    }

    /**
     * Gets the next command from the file.
     * @return The command object. CAN BE NULL.
     * @throws IOException if there was an issue reading the file.
     */
    public Command nextCommand() throws IOException {

        if(!hasMoreCommands()) {
            throw new IllegalStateException("Can't fetch a command when there are none left!");
        }

        Command result;
        String rawLine = reader.readLine();
        //System.out.println(rawLine);
        String[] splitRawLine = rawLine.split("//");
        String line = "";

        // Gotta make this check so it doesn't choke on //
        if(splitRawLine.length > 0) {
            line = splitRawLine[0];
        }

        if(line.isBlank()) {
            result = new EmptyCommand(rawLine);
        }
        else {
            String[] words = line.split("\\s+");

            if(words[0].matches(ArithmeticCommand.KEYWORDS)) {
                result = new ArithmeticCommand(words);
            }
            else if(words[0].matches(StackCommand.KEYWORDS)) {
                result = new StackCommand(words);
            }
            else if(words[0].matches(FlowCommand.KEYWORDS)) {
                result = new FlowCommand(words);
            }
            else if(words[0].matches(FunctionCommand.KEYWORDS)) {
                result = new FunctionCommand(words);
            }
            else {
                throw new IllegalArgumentException(
                        "Unrecognized statement: " + line
                );
            }
        }

        return result;
    }
}
