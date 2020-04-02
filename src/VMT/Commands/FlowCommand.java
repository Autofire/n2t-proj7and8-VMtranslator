//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT.Commands;

import VMT.LabelProvider;

import java.io.PrintStream;

/**
 * Handles the basic flow control statements.
 */
public class FlowCommand implements Command {

    public static final String KEYWORDS = "label|goto|if-goto";

    private enum CommandType {
        LABEL, GOTO, IF_GOTO
    }

    private CommandType command;
    private String labelName;

    public FlowCommand(String[] words) {
        if(words.length != 2) {
            throw new UnsupportedOperationException("Label commands must have just two words");
        }

        if(words[0].equalsIgnoreCase("if-goto")) {
            command = CommandType.IF_GOTO;
        }
        else {
            command = CommandType.valueOf(words[0].toUpperCase());
        }

        labelName = words[1];
    }

    @Override
    public void write(PrintStream out, LabelProvider lp) {

        switch(command) {
            case LABEL:
                out.println("(" + lp.jumpLabel(labelName) + ")");
                break;
            case GOTO:
                out.println("@" + lp.jumpLabel(labelName));
                out.println("0;JMP");
                break;
            case IF_GOTO:
                // Jump is performed if the top value of the stack is non-zero.
                out.println("@SP");     // Load up the stack
                out.println("M=M-1");   // "Remove" the top value of the stack
                out.println("A=M");     // Follow the stack pointer to stack's top

                out.println("D=M");     // Read off the old value on the stack
                out.println("@" + lp.jumpLabel(labelName));
                out.println("D;JNE");
                break;
        }
    }
}
