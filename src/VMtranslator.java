//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

import VMT.CodeWriter;
import VMT.Commands.StackCommand;
import VMT.Parser;

import java.io.*;

public class VMtranslator {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: VMtranslator sourceFile.vm");
            System.out.println("   or: VMtranslator sourceDirectory");
        }
        else {
            String[] targets;
            String outputFileName;

            if(args[0].endsWith(".vm")) {
                targets = new String[]{args[0]};
                outputFileName = args[0].replace(".vm", ".asm");
            }
            else {
                // We must have gotten a directory
                // TODO make this dig through a directory
                throw new UnsupportedOperationException("TODO");
            }

            try(PrintStream outStream = new PrintStream(outputFileName)) {
                CodeWriter writer = new CodeWriter(outStream);

                for(String targetFileName : targets) {
                    writer.setFileName(targetFileName);

                    try(BufferedReader reader = new BufferedReader(new FileReader(targetFileName))) {
                        Parser p = new Parser(reader);

                        while(p.hasMoreCommands()) {
                            writer.write(p.nextCommand());
                        }
                    }
                    catch (IOException e) {
                        System.out.println("Failed to read " + targetFileName);
                        throw e;
                    }
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("Failed to open output file: " + outputFileName);
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
}
