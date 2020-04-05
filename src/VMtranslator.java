//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

// ../08/FunctionCalls/NestedCall

import VMT.CodeWriter;
import VMT.Commands.Command;
import VMT.Commands.StackCommand;
import VMT.Parser;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class VMtranslator {

    private static final String BOOTSTRAP_FUNCTION = "Sys.init";

    private static void printUsage() {
        System.out.println("Usage: VMtranslator [-b or -nb] sourceFile.vm");
        System.out.println("   or: VMtranslator [-b or -nb] sourceDirectory");
        System.out.println("");
        System.out.println("Use -b to generate bootstrap code, and -nb to neglect it.");
        System.out.println("If you are using " + BOOTSTRAP_FUNCTION + " in your code, you should use -b.");
    }

    public static void main(String[] args) {
        // NOTE: This uses some early return statements
        //       to avoid nesting everything in a big 'ol if-else statement
        if(args.length != 2) {
            printUsage();
            return;
        }

        String rawBootstrapArg = args[0];
        String rawTargetArg = args[1];

        String[] targetFileNames;
        String outputFileName;
        boolean generateBootstrapCode;

        if(rawBootstrapArg.equals("-b")) {
            generateBootstrapCode = true;
        }
        else if(rawBootstrapArg.equals("-nb")) {
            generateBootstrapCode = false;
        }
        else {
            System.out.println("Please use just -b or -nb!");
            printUsage();
            return;
        }

        if(rawTargetArg.endsWith(".vm")) {
            targetFileNames = new String[]{rawTargetArg};
            outputFileName = rawTargetArg.replace(".vm", ".asm");
        }
        else {
            File targetDir = new File(rawTargetArg);

            if(targetDir.isDirectory()) {
                // We must have gotten a directory
                FilenameFilter vmFilter = new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                                                               return name.toLowerCase().endsWith(".vm");
                                                                                                         }
                };

                List<String> targetFiles = new LinkedList<String>();

                for (File subFile : targetDir.listFiles(vmFilter)) {
                    if (subFile.isFile()) {
                        String filePath = subFile.getPath();
                        System.out.println("Adding " + filePath);
                        targetFiles.add(filePath);
                    }
                }

                if(targetFiles.size() > 0) {
                    targetFileNames = targetFiles.toArray(new String[targetFiles.size()]);

                    outputFileName = targetDir.getPath() + "\\" + targetDir.getPath()
                            .replaceAll("^.*[/\\\\]", "") + ".asm";
                }
                else {
                    throw new IllegalArgumentException("Directory must contain at least one .vm file.");
                }
            }
            else {
                throw new IllegalArgumentException("Must receive either a .vm file or a directory.");
            }

        }

        System.out.println("Writing to " + outputFileName);

        try(PrintStream outStream = new PrintStream(outputFileName)) {
            CodeWriter writer = new CodeWriter(outStream);

            System.out.println();
            if(generateBootstrapCode) {
                System.out.print("Generating bootstrap code...");
                outStream.println("// BOOTSTRAP CODE //");
                writer.writeBootstrapCode(BOOTSTRAP_FUNCTION);
                System.out.println("done");
            }
            else {
                System.out.println("Skipping bootstrap code...");
            }

            for(String fullFilePath : targetFileNames) {
                String strippedFileName = fullFilePath
                        .replaceAll("^.*[/\\\\]", "")
                        .replaceFirst("\\.vm$", "");

                System.out.println();
                System.out.println("Compiling " + strippedFileName + ".vm");
                writer.setFileName(strippedFileName);

                try(BufferedReader reader = new BufferedReader(new FileReader(fullFilePath))) {
                    Parser p = new Parser(reader);

                    int lineNum = 1;
                    while(p.hasMoreCommands()) {
                        //System.out.print("Line " + lineNum + ": ");
                        Command cmd = p.nextCommand();

                        String lineInfo = "Line " + lineNum + ": " + cmd;
                        System.out.println(lineInfo);
                        outStream.println("// " + lineInfo);

                        writer.write(cmd);
                        lineNum++;
                    }
                }
                catch (IOException e) {
                    System.out.println("Failed to read file: " + fullFilePath);
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
