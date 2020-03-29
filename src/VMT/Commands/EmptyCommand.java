//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT.Commands;

import java.io.PrintStream;

/**
 * This is a nop command. Results on any empty or comment-only line.
 */
public class EmptyCommand implements Command {
    @Override
    public void write(PrintStream outStream) {
        // We do nothing.
    }
}
