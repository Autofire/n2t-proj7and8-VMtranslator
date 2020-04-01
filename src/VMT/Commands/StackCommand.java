//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT.Commands;

import VMT.LabelProvider;

import java.io.PrintStream;

/**
 * Pushes and pops.
 */
public class StackCommand implements Command {
    public static final String KEYWORDS = "push|pop";

    private enum ModeType {
        PUSH, POP
    }

    private enum SegmentType {
        ARGUMENT, LOCAL, STATIC, CONSTANT, THIS, THAT, POINTER, TEMP
    }

    private ModeType mode;
    private SegmentType segment;
    private int index;

    public StackCommand(String[] words) {
        if(words.length != 3) {
            throw new IllegalArgumentException("Must have exactly 2 arguments");
        }

        mode = ModeType.valueOf(words[0].toUpperCase());
        segment = SegmentType.valueOf(words[1].toUpperCase());
        index = Integer.parseInt(words[2]);
    }

    @Override
    public void write(PrintStream out, LabelProvider lp) {
        if(index < 0) {
            throw new UnsupportedOperationException("Cannot have a negative value when pushing/popping.");
        }

        // According to the book, memory is divided up this way:
        //
        // Registers        0 -    15
        //   SP             0
        //   LCL            1
        //   ARG            2
        //   THIS           3
        //   THAT           4
        //   [temp seg]     5 -    12
        //   [general]     13 -    15
        // Static Vars     16 -   255
        // Stack          256 -  2047
        // Heap          2048 - 16483
        // I/O          16384 - 24575

        // For pushes (i.e. READ from a segment), our procedure is thus:
        //  1. Figure out address of data, and put it into A.
        //  2. Read that data into the D register
        //  3. Store value at new top of stack.
        //  4. Increment the stack pointer.
        //
        // Step 1 is different depending on our instruction.
        // However, the rest are always the same!
        //
        //
        // For pops (i.e. WRITE to a segment), our procedure is thus:
        //  1. Determine where we shall write; put that into A.
        //  2. Save that address into R13.
        //  3. Yank the old top value of the stack.
        //  4. Store the value into the address determined at 1.
        //  5. Decrement the stack pointer.
        //
        // Again, only the first step is unique.
        // We may use the same code to handle the remaining steps.
        //
        // Also, steps 1 of both pushes and pops are can be handled
        // the same way. Thus, we should get away with minimal duplication.

        // Step 1: Finding the source/destination
        switch (segment) {
            case CONSTANT:
                out.println("@" + index);
                break;
            case LOCAL:
                out.println("@LCL");
                break;
            case ARGUMENT:
                out.println("@ARG");
                break;
            case THIS:
                out.println("@THIS");
                break;
            case THAT:
                out.println("@THAT");
                break;
            case STATIC:
            case POINTER:
            case TEMP:
            default: // and CONSTANT
                System.out.println("Not handled yet");
                break;
        }

        if(segment != SegmentType.CONSTANT) {
            out.println("A=M");         // Follow the address loaded in step 1
            out.println("D=A");         // Store that address temporarily; we need to add in the offset
            out.println("@" + index);   // Load up the offset
            out.println("A=D+A");       // Now we got the true location of the data
        }

        switch(mode) {
            case PUSH:
                // Step 2: Reading the data into D
                if(segment == SegmentType.CONSTANT) {
                    out.println("D=A");     // make sure the value doesn't get lost
                }
                else {
                    out.println("D=M");     // Read the data out of memory
                }

                // Step 3: Store the data on the top of the stack
                out.println("@SP");   // Pointer to a pointer atm
                out.println("A=M");   // Delve one layer in

                // D == data
                // A -> stack top (i.e. one past old stack top)
                out.println("M=D");   // Update new top of stack

                // Step 4: Increment stack pointer
                out.println("@SP");   // Get ready to update SP
                out.println("M=M+1"); // SP = SP + 1
                break;

            case POP:
                if(segment == SegmentType.CONSTANT) {
                    throw new UnsupportedOperationException("W-wait, popping into constant space? THAT'S ILLEGAL!");
                }

                // Step 2: Saving the address into R13
                out.println("D=A");     // Address is currently in A, we'd lose it if we loaded R13's address
                out.println("@R13");    // Prep to write into R13
                out.println("M=D");     // Save the destination address into R13

                // Step 3: Load the current top value from the stack
                out.println("@SP");     // Load the address of stack + 1
                out.println("A=M-1");   // Get the address of the top value on the stack
                out.println("D=M");     // Get the top value on the stack

                // Step 4: Store the value into the address determined at 1
                //  (Note that the value we want to store is safe in D... just can't use that)
                out.println("@R13");    // We stored the address here, remember?
                out.println("A=M");     // Recall the address to store the data in
                out.println("M=D");     // Finally, store the value from the stack in memory

                // Step 5: Decrement the stack pointer.
                out.println("@SP");     // Load the pointer to the pointer of the stack
                out.println("M=M-1");   // No longer include the old top of the stack
                break;

            default:
                throw new UnsupportedOperationException("Unhandled mode");
        }
    }
}
