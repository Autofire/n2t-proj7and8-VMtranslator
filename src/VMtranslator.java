//  Author: Daniel Edwards
//   Class: CS 3650 (Section 1)
// Project: 7 & 8
//     Due: 04/06/2020

// ../08/FunctionCalls/NestedCall

import VMT.CodeWriter;
import VMT.Commands.StackCommand;
import VMT.Parser;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class VMtranslator {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: VMtranslator sourceFile.vm");
            System.out.println("   or: VMtranslator sourceDirectory");
        }
        else {
            String[] targetFileNames;
            String outputFileName;

            if(args[0].endsWith(".vm")) {
                targetFileNames = new String[]{args[0]};
                outputFileName = args[0].replace(".vm", ".asm");
            }
            else {
                File targetDir = new File(args[0]);

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

                for(String fullFilePath : targetFileNames) {
                    String strippedFileName = fullFilePath
                            .replaceAll("^.*[/\\\\]", "")
                            .replaceFirst("\\.vm$", "");
                    //System.out.println();

                    writer.setFileName(strippedFileName);

                    try(BufferedReader reader = new BufferedReader(new FileReader(fullFilePath))) {
                        Parser p = new Parser(reader);

                        int lineNum = 1;
                        while(p.hasMoreCommands()) {
                            System.out.print("Line " + lineNum + ": ");
                            writer.write(p.nextCommand());
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
}
