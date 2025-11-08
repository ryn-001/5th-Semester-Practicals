import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MacroPass1 {
    public static void main(String[] args) throws IOException {
        String inputFile = args.length > 0 ? args[0] : "MacroInput.txt";
        String mntFile = args.length > 1 ? args[1] : "MNT.txt";
        String mdtFile = args.length > 2 ? args[2] : "MDT.txt";
        String alaFile = args.length > 3 ? args[3] : "ALA.txt";

        List<String> lines = Files.readAllLines(Paths.get(inputFile));
        List<String> mnt = new ArrayList<>();
        List<String> mdt = new ArrayList<>();
        List<String> ala = new ArrayList<>();

        boolean inMacro = false;
        String macroName = "";
        int mdtPtr = 1, alaPtr = 1;

        for (String line : lines) {
            line = line.trim();
            if (line.equalsIgnoreCase("MACRO")) {
                inMacro = true;
                continue;
            }
            if (inMacro) {
                if (macroName.isEmpty()) {
                    String[] parts = line.split("\\s+", 2);
                    macroName = parts[0];
                    String[] argsArr = parts.length > 1 ? parts[1].split(",") : new String[0];
                    Map<String, String> argMap = new LinkedHashMap<>();
                    for (String arg : argsArr) {
                        arg = arg.trim();
                        if (!arg.isEmpty()) {
                            argMap.put(arg, "#" + alaPtr);
                            ala.add(alaPtr + " " + arg);
                            alaPtr++;
                        }
                    }
                    mnt.add(macroName + " " + mdtPtr);
                    continue;
                }
                if (line.equalsIgnoreCase("MEND")) {
                    mdt.add(mdtPtr + " MEND");
                    mdtPtr++;
                    inMacro = false;
                    macroName = "";
                    continue;
                }
                String temp = line;
                for (String a : ala) {
                    String[] parts = a.split("\\s+");
                    String idx = parts[0];
                    String name = parts[1];
                    temp = temp.replace(name, "#" + idx);
                }
                mdt.add(mdtPtr + " " + temp);
                mdtPtr++;
            }
        }

        Files.write(Paths.get(mntFile), mnt);
        Files.write(Paths.get(mdtFile), mdt);
        Files.write(Paths.get(alaFile), ala);
        System.out.println("Macro Pass 1 completed. Output: MNT.txt, MDT.txt, ALA.txt");
    }
}
