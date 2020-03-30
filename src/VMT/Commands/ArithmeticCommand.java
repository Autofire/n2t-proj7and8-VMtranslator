//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT.Commands;

import VMT.LabelProvider;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/**
 * Applies to both logical and mathematical commands.
 */
public class ArithmeticCommand implements Command {

    public static final String KEYWORDS = "add|sub|neg|eq|gt|lt|and|or|not";

    private enum OperationType {
        ADD, SUB, NEG,
        EQ, GT, LT,
        AND, OR, NOT
    }
    private static final List<OperationType> UNARY_OPERATIONS = Arrays.asList(
            new OperationType[]{
                    OperationType.NEG, OperationType.NOT
            }
    );

    //private String commandName;
    private OperationType operation;

    public ArithmeticCommand(String[] words) {
        // TODO Error checking

        //commandName = words[0];
        operation = OperationType.valueOf(words[0].toUpperCase());
    }

    @Override
    public void write(PrintStream out, LabelProvider lp) {
        // SP points to the stack pointer
        // R13-R15 can be used as scratch space

        if(UNARY_OPERATIONS.contains(operation)) {
            // We can just yoink the value off the top of the
            // stack, modify it, and then return it to the
            // top of the stack.
            // TODO write this
        }
        else {
            // It wasn't a unary operation, so it must be binary.
            // We'll have to modify SP, since the stack will lose a
            // value.

            out.println("@SP");   // Prepare to access the stack
            out.println("M=M-1"); // "Remove" top of stack
            out.println("A=M");   // Load stack pointer itself

            // A -> Old top value of stack
            out.println("D=M");   // Grab the top value of the stack
            out.println("A=A-1"); // Point to the new top of the stack

            // A -> X operand, result
            // D == Y operand
            switch(operation) {
                // Each of these just drop the result directly into
                // the X position.
                case ADD:
                    out.println("M=D+M");   // Store Y plus X
                    break;
                case SUB:
                    out.println("M=M-D");   // Store X minus Y
                    break;
                case AND:
                    out.println("M=D&M");   // Store Y and X
                    break;
                case OR:
                    out.println("M=D|M");   // Store Y or X
                    break;

                // These aren't as straightforward; they drop their
                // results into the X position, but that result
                // is either a one or a zero.
                case EQ:
                    String baseLabel = lp.nextLabel();
                    String trueLabel = baseLabel + ".true";
                    String endLabel  = baseLabel + ".end";

                    // X = Y -> X - Y = 0
                    // X > Y -> X - Y > 0
                    // X < Y -> X - Y < 0
                    out.println("D=M-D");           // Store X minus Y
                    out.println("@"+trueLabel);     // If X-Y = 0...
                    out.println("D;JEQ");           // ...jump to true branch

                    // FALSE BRANCH
                    out.println("@0");              // Prep false
                    out.println("D=A");             // Save false for later
                    out.println("@"+endLabel);      // Prep to skip true branch
                    out.println("0;JMP");           // Skip true branch

                    // TRUE BRANCH
                    out.println("("+trueLabel+")");
                    out.println("@1");              // Prep true
                    out.println("D=A");             // Save true for later

                    // BRANCHES CONVERGE
                    out.println("("+endLabel+")");

                    // Now we must store the result of the condition
                    // (held in D) on top of the stack.
                    out.println("@SP");     // Prepare to access the stack
                    out.println("A=M-1");   // Load the address of top value on stack
                    out.println("M=D");     // Store result

                    break;

                case GT:
                    //break;
                case LT:
                    throw new UnsupportedOperationException("WIP");
                    //break;

                default: // If we somehow get here with an unary operation
                    throw new UnsupportedOperationException("Illegal state");
            }
        }
    }
}
