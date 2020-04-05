//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT;

import VMT.Commands.Command;
import VMT.Commands.FunctionCommand;

import java.io.PrintStream;

/**
 * Outputs commands to a file. This really doesn't seem to necessary,
 * since the actual "How is this written to a file" logic has been
 * stuffed into the Command objects. Still, it's here just in case.
 */
public class CodeWriter {

    private static final int STACK_START = 256;

    private PrintStream writer;
    private String fileName = "";
    private LabelProvider lp = null;

    public CodeWriter(PrintStream writer) {
        this.writer = writer;
        //WriteSetupCode();
    }

    public void writeBootstrapCode(String initFunctionName) {
        // We gotta make sure SP contains the
        // beginning of the stack.
        writer.println("@" + STACK_START);
        writer.println("D=A");
        writer.println("@SP");
        writer.println("M=D");

        // This is according to the test code
        writer.println("@LCL");
        writer.println("MD=-1");
        writer.println("@ARG");
        writer.println("MD=D-1");
        writer.println("@THIS");
        writer.println("MD=D-1");
        writer.println("@THAT");
        writer.println("MD=D-1");

        /*
        writer.println("@Sys.init");
        writer.println("0;JMP");
         */
        LabelProvider originalLP = lp;
        lp = new LabelProvider("Bootstrap");
        write(new FunctionCommand(
                new String[] {"call", initFunctionName, "0"}
        ));
        lp = originalLP;
    }

    /**
     * Call this when starting to read a new file. Note that we
     * continue to output to the same file.
     * @param newFileName
     */
    public void setFileName(String newFileName) {

        fileName = newFileName;
        lp = new LabelProvider(newFileName);

        // Just to make debugging this stuff easier,
        // let's also log where the new file begins.
        writer.println("// BEGIN " + newFileName + ".vm //");

    }

    public void write(Command cmd) {
        cmd.write(writer, lp);
    }
}
