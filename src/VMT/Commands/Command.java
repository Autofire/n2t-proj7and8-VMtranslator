//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT.Commands;

import java.io.PrintStream;

/**
 * This represents a single command in the VM, and can be converted
 * into assembly instructions.
 */
public interface Command {
    /**
     * Write this command onto the output stream as assembly. Note
     * that this will almost certainly output multiple lines.
     * @param outStream Stream which to write onto.
     */
    public void write(PrintStream outStream, String sourceFileName);
}
