//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT;

import VMT.Commands.Command;

import java.io.PrintStream;

/**
 * Outputs commands to a file. This really doesn't seem to necessary,
 * since the actual "How is this written to a file" logic has been
 * stuffed into the Command objects. Still, it's here just in case.
 */
public class CodeWriter {

    private PrintStream writer;

    public CodeWriter(PrintStream writer) {
        this.writer = writer;
    }

    /**
     * Call this when starting to read a new file. Note that we
     * continue to output to the same file.
     * @param newFileName
     */
    public void setFileName(String newFileName) {
        // I honestly have no clue what the heck this is supposed
        // to do, so I'll just spit out a random comment.
        writer.println("// " + newFileName);
    }

    public void write(Command cmd) {
        cmd.write(writer);
    }
}
