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
    public void write(PrintStream outStream, LabelProvider lp) {
        if(segment == SegmentType.CONSTANT) {
            // We'll handle constant pushes differently,
            // as we can make some sneaky assumptions thanks to
            // it.
            //
            // Note that a constant pop isn't possible. That
            // makes no sense.
            switch(mode) {
                case PUSH:
                    // Store the constant into D
                    outStream.println("@" + index);   // Load up our value
                    outStream.println("D=A");            // Put it in storage

                    // Store the address of the stack into A
                    outStream.println("@SP");   // Pointer to a pointer atm
                    outStream.println("A=M");   // Delve one layer in

                    // D == const
                    // A -> stack top
                    outStream.println("M=D");   // Update top of stack

                    // Now to do SP=SP+1
                    outStream.println("D=A+1"); // Save the current stack top address, but offset
                    outStream.println("@SP");   // Get ready to update SP
                    outStream.println("M=D");   // SP = SP + 1
                    break;
                case POP:
                    throw new UnsupportedOperationException("Popping into constant memory is impossible!");
            }
        }
        else {
            // Other than the constant pushes (which have already been handled),
            // all of these pushes/pops behave similarly. Thus, the plan is
            // to initially select locations from which we'll push/pop,
            // and then drop those into a standard template for pushing
            // and popping.
            switch (segment) {
                default:
                    System.out.println("Not handled yet");
                    break;
            }
        }
    }
}
