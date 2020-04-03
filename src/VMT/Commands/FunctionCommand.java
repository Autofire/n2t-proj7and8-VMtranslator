//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT.Commands;

import VMT.LabelProvider;

import java.io.PrintStream;

/**
 * Handles all function-related operations. Thus, declaring functions,
 * calling functions, and returning from functions.
 */
public class FunctionCommand implements Command {

    public static final String KEYWORDS = "call|function|return";

    private enum CommandType {
        CALL, FUNCTION, RETURN;
    }

    private CommandType command;
    private String functionName;
    private int parameter;

    public FunctionCommand(String[] words) {
        command = CommandType.valueOf(words[0].toUpperCase());

        if(command == CommandType.RETURN) {

            if(words.length != 1) {
                throw new IllegalArgumentException("A return operation must have no arguments.");
            }

            functionName = "";
            parameter = 0;
        }
        else {
            if(words.length != 3) {
                throw new IllegalArgumentException("Call and function operations must have two arguments.");
            }

            functionName = words[1];
            parameter = Integer.parseInt(words[2]);
        }
    }

    @Override
    public void write(PrintStream out, LabelProvider lp) {
        switch (command) {
            case CALL:
                throw new UnsupportedOperationException("WIP");

            case FUNCTION:
                out.println("@" + lp.functionLabel(functionName));

                // In this case, the parameter refers to the number
                // of bytes of local space the function requires.
                // If none were requested, we can skip all of this code.
                if(parameter > 0) {
                    // NOTE: If I rework this, it should be an unrolled loop;
                    //       Our compiler can just generate a set of instructions to zero
                    //       each one of the values and it should be far more efficient

                    // We need to loop for a bit; will be pushing stuff
                    // onto the stack.
                    String loopLabel = lp.generatedLabel();

                    // Now we'll reserve the space requested by the programmer.
                    // First, we'll prep for the loop.
                    out.println("@" + parameter);   // Load up the number of elements to reserve
                    out.println("D=A");             // Save that number; we'll need it a lot

                    // Loop starts here. We KNOW we'll be looping at least once,
                    // so we can wait to do the check until the end.
                    out.println("(" + loopLabel + ")");

                    // Push a 0 onto the stack
                    out.println("@SP");             // Load the stack pointer
                    out.println("M=M+1");           // Update include a new element on the stack
                    out.println("A=M-1");           // Follow the stack pointer to the new top of stack
                    out.println("M=0");             // Put 0 as the new top value of the stack

                    // Check if we'll be looping again
                    out.println("D=D-1");           // There's one fewer element to push now
                    out.println("@" + loopLabel);   // We might jump to the start of the loop
                    out.println("D;JNE");           // If there's still more to push,
                }
                break;

            case RETURN:
                // We'll be basing this on the code in the book

                // These are all of hte temporary variables used by
                // that code.
                final String FRAME = "@R13";
                final String RET   = "@R14";

                // FRAME = LCL
                // This way we can change LCL all we want
                out.println("@LCL");            // Load up LCL
                out.println("D=M");             // Get the address LCL is pointing to
                out.println(FRAME);             // Load var which we'll store the frame of reference in
                out.println("M=D");             // Save the current frame of reference

                // RET = *(FRAME-5)
                // Keep track of the return address
                // Note that FRAME's value is already stored in D
                out.println("@5");              // Load the offset
                out.println("A=D-A");           // Calculate the return address's location in memory
                out.println("D=M");             // Load the return address
                out.println(RET);               // Prepare to save the return address
                out.println("M=D");             // Save the return address

                // *ARG = pop()
                // So the top value on the stack is the return value... gotta save it!
                out.println("@SP");             // Fetch pointer to pointer of stack
                out.println("A=M-1");           // Fetch address of top value on stack
                out.println("D=M");             // Fetch return value
                out.println("@ARG");            // Prepare to store the return value
                out.println("A=M");             // Load the address of the new stack stop
                out.println("M=D");             // Store the return value

                // SP = ARG+1
                out.println("@ARG");            // ARG is the old SP
                out.println("D=M+1");           // Since we added return value to stack, we need to do old SP + 1
                out.println("@SP");             // Prepare to drop in the new SP value
                out.println("M=D");             // Update SP

                // THAT = *(FRAME-1)
                // THIS = *(FRAME-2)
                // ARG = *(FRAME-3)
                // LCL = *(FRAME-4)
                String[] segments = {"@THAT", "@THIS", "@ARG", "@LCL"};
                for(int i = 0; i < segments.length; i++) {
                    out.println(FRAME);             // Load FRAME's address
                    out.println("D=M");             // Load FRAME's value
                    out.println("@" + (i+1));       // Load the offset
                    out.println("A=D-A");           // Compute the address of the segment's old value
                    out.println("D=M");             // Load the segment's old value
                    out.println(segments[i]);       // Load the associated segment
                    out.println("M=D");             // Restore the segment to its old value
                }

                // goto RET
                out.println(RET);                   // Load the location of the return address
                out.println("A=M");                 // Load the return address
                out.println("0;JMP");               // We're done here, Captain Obvious!
                break;
        }
    }
}
