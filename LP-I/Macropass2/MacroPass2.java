import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MacroPass2 {
    public static void main(String[] args) throws IOException {
        String inputFile = args.length > 0 ? args[0] : "MacroInput.txt";
        String mntFile = args.length > 1 ? args[1] : "MNT.txt";
        String mdtFile = args.length > 2 ? args[2] : "MDT.txt";
        String alaFile = args.length > 3 ? args[3] : "ALA.txt";
        String outputFile = args.length > 4 ? args[4] : "Pass2Output.txt";

        List<String> inputLines = Files.readAllLines(Paths.get(inputFile));
        List<String> mntLines = Files.readAllLines(Paths.get(mntFile));
        List<String> mdtLines = Files.readAllLines(Paths.get(mdtFile));
        List<String> alaLines = Files.readAllLines(Paths.get(alaFile));

        Map<String, Integer> mnt = new HashMap<>();
        for (String line : mntLines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2)
                mnt.put(parts[0], Integer.parseInt(parts[1]));
        }

        Map<Integer, String> mdt = new HashMap<>();
        for (String line : mdtLines) {
            String[] parts = line.trim().split("\\s+", 2);
            if (parts.length == 2)
                mdt.put(Integer.parseInt(parts[0]), parts[1]);
        }

        Map<Integer, String> ala = new HashMap<>();
        for (String line : alaLines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2)
                ala.put(Integer.parseInt(parts[0]), parts[1]);
        }

        List<String> output = new ArrayList<>();
        boolean inMacroDef = false;

        for (String line : inputLines) {
            line = line.trim();
            if (line.equalsIgnoreCase("MACRO")) { inMacroDef = true; continue; }
            if (line.equalsIgnoreCase("MEND")) { inMacroDef = false; continue; }
            if (inMacroDef || line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String macroName = parts[0];
            String argList = parts.length > 1 ? parts[1] : "";

            if (mnt.containsKey(macroName)) {
                int mdtIndex = mnt.get(macroName);
                String[] actualArgs = argList.split(",");
                Map<String, String> argMap = new HashMap<>();
                int count = 1;
                for (String arg : actualArgs) {
                    arg = arg.trim();
                    if (!arg.isEmpty())
                        argMap.put("#" + count, arg);
                    count++;
                }

                while (true) {
                    String macroLine = mdt.get(mdtIndex);
                    if (macroLine == null || macroLine.equalsIgnoreCase("MEND"))
                        break;

                    String expanded = macroLine;
                    for (Map.Entry<String, String> entry : argMap.entrySet())
                        expanded = expanded.replace(entry.getKey(), entry.getValue());

                    output.add(expanded);
                    mdtIndex++;
                }
            } else {
                output.add(line);
            }
        }

        Files.write(Paths.get(outputFile), output);
        System.out.println("Macro Pass 2 completed. Output written to: " + outputFile);
    }
}
