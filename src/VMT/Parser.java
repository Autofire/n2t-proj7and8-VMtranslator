//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT;

import VMT.Commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

        String line = reader.readLine();
        Command result = new EmptyCommand();



        return result;
    }
}
