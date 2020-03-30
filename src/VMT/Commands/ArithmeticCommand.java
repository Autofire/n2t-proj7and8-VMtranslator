//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT.Commands;

import java.io.PrintStream;

/**
 * Applies to both logical and mathematical commands.
 */
public class ArithmeticCommand implements Command {

    public static final String KEYWORDS = "add|sub|neg|eq|gt|lt|and|or|not";

    private String commandName;

    public ArithmeticCommand(String[] words) {
        // TODO Error checking

        commandName = words[0];
    }

    @Override
    public void write(PrintStream outStream, String currentFileName) {
        // SP points to the stack pointer
        // R13-R15 can be used as scratch space

        // All other operations are binary, so we'll just have to check
        // for these two.
        final String UNARY_OPS = "neg|not";

        if(commandName.matches(UNARY_OPS)) {
            // We can just yoink the value off the top of the
            // stack, modify it, and then return it to the
            // top of the stack.
            // TODO write this
        }
        else {
            // So it wasn't a unary operation, so it must be binary.
            // We'll have to modify SP, since the stack will lose a
            // value.
            // TODO write this
        }
    }
}
