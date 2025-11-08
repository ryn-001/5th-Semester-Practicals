import java.io.*;
import java.util.*;

class PassOneAssembler{
	
	static class Symbol{
		String symbol;
		int address;


		Symbol(String symbol, int address){
			this.symbol = symbol;
			this.address = address;
		}
	}

	static class Literal{
		String literal;
		int address;
		
		Literal(String literal, int address){
			this.literal = literal;	
			this.address = address;
		}
	}

	public static void main(String[] args){
		Map<String,Integer> MOT = new HashMap<>();
		MOT.put("STOP",0);
		MOT.put("ADD",1);
		MOT.put("SUB",2);
		MOT.put("MULT",3);
		MOT.put("MOVER",4);
		MOT.put("MOVEM",5);
		MOT.put("COMP",6);
		MOT.put("BC",7);
		MOT.put("DIV",8);
		MOT.put("READ",9);
		MOT.put("PRINT",10);

		List<Symbol> SymbolTable = new ArrayList<>();
		List<Literal> LiteralTable = new ArrayList<>();
		List<String> intermediateCode = new ArrayList<>();

		int LC = 0;
		Boolean startFound = false;

		try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))){
			String line;

			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.isEmpty()) continue;

				String[] tokens = line.split("\\s+");

				if(tokens[0].equalsIgnoreCase("START")){
					LC = Integer.parseInt(tokens[1]);
					startFound = true;
					intermediateCode.add("(AD,01) (C," + LC + ")");
					continue;
				}

				if(!startFound){
					System.out.println("Start not found");
					return;
				}
				
				if(tokens[0].equalsIgnoreCase("END")){
					intermediateCode.add("(AD,02)");

					for(Literal lit : LiteralTable){
						if(lit.address == -1) lit.address = LC++;
					}
					break;
				}

				int index = 0;
				String label = "";

				if(!MOT.containsKey(tokens[0]) && !tokens[0].equalsIgnoreCase("DS") && !tokens[0].equalsIgnoreCase("DC")){
					label = tokens[0];
					SymbolTable.add(new Symbol(label,LC));
					index = 1;
				}

				String opcode = tokens[index];
					
				if(MOT.containsKey(opcode)){
					intermediateCode.add("(IS," + String.format("%02d", MOT.get(opcode)) + ")");
					LC++;
				} else if(opcode.equalsIgnoreCase("DS")){
					int size = Integer.parseInt(tokens[index+1]);
					SymbolTable.add(new Symbol(label,LC));
					LC += size;
					intermediateCode.add("(DL,01) (C,"+size+")");
				} else if(opcode.equalsIgnoreCase("DC")){
					int value = Integer.parseInt(tokens[index+1]);
					SymbolTable.add(new Symbol(label,LC));
					intermediateCode.add("(DL,02) (C,"+value+")");
					LC++;
				}

				for(String token : tokens){
					if(token.startsWith("=")) LiteralTable.add(new Literal(token,-1));
				}
			}
			
		} catch(IOException e){
			e.printStackTrace();
		}
		
		try(BufferedWriter symOut = new BufferedWriter(new FileWriter("SymbolTable.txt")); 
		    BufferedWriter litOut = new BufferedWriter(new FileWriter("LiteralTable.txt"));
		     BufferedWriter icOut = new BufferedWriter(new FileWriter("IntermediateCode.txt"))){
			
			symOut.write("Symbol\tAddress\n");
			for(Symbol s : SymbolTable){
				symOut.write(s.symbol + "\t" + s.address + "\n");
			}
			
			litOut.write("Literal\tAddress\n");
			for(Literal l : LiteralTable){
				litOut.write(l.literal + "\t" + l.address + "\n");
			}

			for(String ic : intermediateCode){
				icOut.write(ic + "\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}