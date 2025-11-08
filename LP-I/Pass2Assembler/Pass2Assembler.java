import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Pass2Assembler {
    private static final Pattern TOKEN = Pattern.compile("\\(([A-Za-z]+)\\s*,\\s*([A-Za-z0-9_\\-]+)\\)");

    public static void main(String[] args) throws IOException {
        String icFile = args.length > 0 ? args[0] : "IntermediateCode.txt";
        String symFile = args.length > 1 ? args[1] : "SymbolTable.txt";
        String litFile = args.length > 2 ? args[2] : "LiteralTable.txt";
        String outFile = args.length > 3 ? args[3] : "Pass2Output.txt";

        Map<Integer, String> symAddr = parseIndexAddressFile(symFile);
        Map<Integer, String> litAddr = parseIndexAddressFile(litFile);

        List<String> icLines = Files.readAllLines(Paths.get(icFile));
        List<String> outputLines = new ArrayList<>();

        for (String line : icLines) {
            if (line.trim().isEmpty()) continue;
            List<Token> tokens = tokenizeLine(line);
            String objectCode = translateTokensToObject(tokens, symAddr, litAddr);
            if (!objectCode.isEmpty()) outputLines.add(objectCode);
        }

        Files.write(Paths.get(outFile), outputLines);
        System.out.println("Pass 2 finished. Machine code written to: " + outFile);
    }

    private static class Token {
        String type, value;
        Token(String type, String value) { this.type = type; this.value = value; }
    }

    private static List<Token> tokenizeLine(String line) {
        List<Token> tokens = new ArrayList<>();
        Matcher m = TOKEN.matcher(line);
        while (m.find()) tokens.add(new Token(m.group(1).toUpperCase(), m.group(2)));
        return tokens;
    }

    private static Map<Integer, String> parseIndexAddressFile(String filename) throws IOException {
        Map<Integer, String> map = new HashMap<>();
        for (String line : Files.readAllLines(Paths.get(filename))) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.trim().split("\\s+");
            int idx = -1, addr = -1;
            for (String p : parts) {
                try {
                    int v = Integer.parseInt(p);
                    if (idx == -1) idx = v;
                    else addr = v;
                } catch (NumberFormatException ignored) {}
            }
            if (idx != -1 && addr != -1) map.put(idx, String.valueOf(addr));
        }
        return map;
    }

    private static String translateTokensToObject(List<Token> tokens, Map<Integer, String> symAddr, Map<Integer, String> litAddr) {
        if (tokens.isEmpty()) return "";
        Token first = tokens.get(0);
        if (first.type.equals("AD")) return "";
        if (first.type.equals("DL")) {
            if (first.value.equals("02") && tokens.size() > 1 && tokens.get(1).type.equals("C"))
                return "00 00 " + tokens.get(1).value;
            return "";
        }
        if (first.type.equals("IS")) {
            String opcode = first.value;
            String reg = "00", mem = "000";
            for (Token t : tokens) {
                if (t.type.equals("R")) reg = t.value;
                else if (t.type.equals("S")) mem = symAddr.getOrDefault(Integer.parseInt(t.value), "000");
                else if (t.type.equals("L")) mem = litAddr.getOrDefault(Integer.parseInt(t.value), "000");
                else if (t.type.equals("C")) mem = t.value;
            }
            return opcode + " " + reg + " " + mem;
        }
        return "";
    }
}
