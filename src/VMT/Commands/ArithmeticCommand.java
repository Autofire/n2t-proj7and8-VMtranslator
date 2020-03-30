//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

package VMT.Commands;

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
    public void write(PrintStream out, String currentFileName) {
        // SP points to the stack pointer
        // R13-R15 can be used as scratch space

        if(UNARY_OPERATIONS.contains(operation)) {
            // We can just yoink the value off the top of the
            // stack, modify it, and then return it to the
            // top of the stack.
            // TODO write this
        }
        else {
            // So it wasn't a unary operation, so it must be binary.
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
                case EQ:
                    //break;
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
