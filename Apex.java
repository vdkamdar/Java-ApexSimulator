
package apex_v2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class Apex {

	static ArrayList<Instruction> ins = new ArrayList<Instruction>();
	static Instruction inst;
	//static int[] memory = new int[10000];
	ArrayList<Integer> memory = new ArrayList<Integer>();
	static ArrayList<Instruction> stages= new ArrayList<Instruction>();
	static ArrayList<Instruction> issueQueue = new ArrayList<Instruction>();
	static ArrayList<ROBEntry> rob = new ArrayList<ROBEntry>(); 
	static boolean flagBranch = false;
	static boolean branchTaken = false;
	static ArrayList<Instruction> stagePrev= new ArrayList<Instruction>();
	static ArrayList<Integer> registers = new ArrayList<Integer>(15);		//frontEnd Registers
	static ArrayList<Integer> registersActuals = new ArrayList<Integer>(15);	//backEnd Registers
	//static Map<Integer, PhysicalRegister> renameTable = new TreeMap<Integer, PhysicalRegister>();
	static ArrayList<PhysicalRegister> physicalRegisters = new ArrayList<PhysicalRegister>(); //RAT
	static ArrayList<PhysicalRegister> backPhysicalRegisters = new ArrayList<PhysicalRegister>(); //RRAT
	static ArrayList<Integer> memoryLocations = new ArrayList<Integer>(4000);
	static int zeroFlag = 1;
	static int bubbleStat =0;
		public static void main(String[] args) throws FileNotFoundException, IOException {
		Scanner sc2 = null;
		//System.out.println(args[0]);
		sc2 = new Scanner(new File("C:/java workspace/Project2/src/apex_v2/input.txt"));
		int addr = 4000;
		int stall_inst = 0;
		boolean ftch = true;
		int bubbles_added = 0;
		int X = 0;
		  
		  //Menu
		String s = null;
		int[] src1 = new int[5];
		int[] src2 = new int[5];
		int counter = 0;
		int iCount = 0; 
		int issueCount = 0;// instruction issued count
		int dCount = 0; // dispatch count 
		int cycle = 1;
		int noOfURF = 0;
		int maxPhysicalReg = 0;
		boolean bz_flag = false;
		int z = 0;// to count cycle
		String oper = "JUMP";
		Stages stage = new Stages();
		do
		{
			System.out.println("Menu \n1.Initiation \n2.Simulate \n3.Display");
			System.out.println("Enter your option");
			Scanner reader = new Scanner(System.in);
			int n = reader.nextInt();
			switch(n)
			{
			case 1:  Instruction finishInstruction = new Instruction(0, "FINISH", "R16", "R16", "R16");
				while (sc2.hasNextLine()) {
					String line = sc2.nextLine(); 
					String[] words = null;
					line = line.replace(",", "");
					line = line.replace("#", "");
					words = line.split(" ");
					stages.add(null);
					stages.add(null);
					stages.add(null);
					stages.add(null);
					stages.add(null);
					stages.add(null);
					stages.add(null);
					stages.add(null);
					stagePrev.add(null);
					stagePrev.add(null);
					stagePrev.add(null);
					stagePrev.add(null);
					stagePrev.add(null);
					stagePrev.add(null);
					stagePrev.add(null);
					stagePrev.add(null);
				
					if(ftch)
						{
						
						switch(words[0])
						{
							case "ADD": inst = new Instruction(addr, words[0],words[1],words[2],words[3]);
							ins.add(inst);
							break;
							case "SUB": inst = new Instruction(addr, words[0],words[1],words[2],words[3]);
							ins.add(inst);
							break;
							case "MOVC":inst = new Instruction(addr, words[0],words[1],(words[2]),"0");
							ins.add(inst); 
							IssueQueueEntry iqEntry = new IssueQueueEntry(addr,words[0],words[1],(words[2]),"0");
							//ins.add(iqEntry);
							break;
							case "MUL": inst = new Instruction(addr, words[0],words[1],words[2],words[3]);
							ins.add(inst);
							break;
							case "STORE": inst = new Instruction(addr, words[0],words[1],words[2],words[3]);
							ins.add(inst);
							break;
							case "LOAD": inst = new Instruction(addr, words[0],words[1],words[2],words[3]);
							ins.add(inst); 
							break;                                
							case "HALT": inst = new Instruction(addr, words[0],"R8","0","0");
							ins.add(inst);
							break;
							case "BZ": inst = new Instruction(addr, words[0],words[1],"0","0");
							ins.add(inst); 
							break;
							case "JUMP":inst = new Instruction(addr, words[0],words[1],words[2],"0");
							ins.add(inst); 
							break;
							case "BNZ": inst = new Instruction(addr, words[0],words[1],"0","0");
							ins.add(inst); 
							break;
							case "BAL": inst = new Instruction(addr, words[0],words[1],words[2],"0");
							ins.add(inst); 
							X = addr + 1;
							break;
						}
						}
				addr++;
				}
			ins.add(finishInstruction);
			ins.add(finishInstruction);
			ins.add(finishInstruction);
			/*registers.add(0);registers.add(0);registers.add(0);registers.add(0);registers.add(0);registers.add(0);
			registers.add(0);registers.add(0);registers.add(0);registers.add(0);registers.add(0);registers.add(0);
			registers.add(0);registers.add(0);registers.add(0);registers.add(0);
			registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);
			registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);
			registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);registersActuals.add(0);*/
			for(int m = 0; m<100; m++)
				memoryLocations.add(m, 0);
			System.out.println("Pipeline Initialised");
			break;
			case 2:
				
				System.out.println("Enter the number of cycles you want to simulate");
				int nc = reader.nextInt();
				nc = nc + z;
				if(noOfURF == 0){
					System.out.println("Enter size of URF");
					noOfURF = reader.nextInt();
					maxPhysicalReg = noOfURF -16;
					//initialize mapping tables
					for(int u = 0; u< 16; u++){
						physicalRegisters.add(new PhysicalRegister(u, 0, 1, 0)); //1= invalid | 0 = free 1 = allocated 2 committed 
						backPhysicalRegisters.add(new PhysicalRegister(u, 0, 1, 0)); //1= invalid | 0 = free 1 = allocated 2 committed
					}
					//initialize physical registers size 
					for(int u=0; u<maxPhysicalReg; u++){
						registers.add(u);
						registersActuals.add(u);
					}
				}
				Instruction bubleInstruction = new Instruction(0, "BUBBLE", "R16", "R16", "R16");
				//for(int z = 0, iCount = 0; z < nc; z++)
				for(; z < nc; z++)
				{
				System.out.println("");
				System.out.println("Cycle "+(cycle++));
				if(z<4){
					stages.set(0, ins.get(iCount));                              	
					if(z==0){
						iCount++;
					}
					if(z==1 || z==2 || z==3 || z==4){
				    	stages.set(1, ins.get(iCount-1));
				    	
				    	if(z==2)
				    		stages.set(2, ins.get(iCount-2));
				    	if(z==3)
				    		stages.set(3, ins.get(iCount-3));
				    	if(z==4)
				    		stages.set(4, ins.get(iCount-4));
				    	//DecodeRename1 check availability of IQ and physical register
				    	PhysicalRegister dest = PhysicalRegister.allocateNew(physicalRegisters);
				    	physicalRegisters.set(iCount-1, dest);
				    	dest.status = 1;
				    	Instruction renamedInstruction = Instruction.renameInstruction(ins.get(iCount-1));
				    	issueQueue.add(renamedInstruction);
				    	issueCount++;
				    	if(z>=2)
				    		//stage.executeALU2(ins.get(iCount-2), registers,memoryLocations);
				    		stage.executeALU2(issueQueue.get(issueCount-2), registers,memoryLocations,physicalRegisters);
				    	iCount++;
				    }    	
				    /*if(z==2){
				    	stages.set(1, ins.get(iCount-1));
				    	stages.set(2, ins.get(iCount-2));
				    	//stage.executeALU2(ins.get(iCount-2), registers,memoryLocations);
				    	iCount++;
				    }                		
				    if(z==3){
				    	stages.set(1, ins.get(iCount-1));
				    	if(ins.get(iCount-2).src1.equalsIgnoreCase(ins.get(iCount-3).des) || 
				    			ins.get(iCount-2).src2.equalsIgnoreCase(ins.get(iCount-3).des)){
				    		stages.set(z-2, bubleInstruction);
				    	}                    		
				    	else{
				    		stages.set(2, ins.get(iCount-2));
				    		stage.executeALU2(ins.get(iCount-3), registers,memoryLocations);
				    		stages.set(3, ins.get(iCount-3));
				    		iCount++;
				    	}                    	
				    }	
				    if(z==3){
				    	stages.set(1, ins.get(iCount-1));
				    	if(ins.get(iCount-2).src1.equalsIgnoreCase(ins.get(iCount-3).des) || 
				    			ins.get(iCount-2).src2.equalsIgnoreCase(ins.get(iCount-3).des)){
				    		stages.set(z-2, bubleInstruction);
				    	}                    		
				    	else{
				    		stages.set(2, ins.get(iCount-2));
				    		stage.executeALU2(ins.get(iCount-3), registers,memoryLocations);
				    		stages.set(3, ins.get(iCount-3));
				    		stages.set(4, null);
				    		stages.set(5, null);
				    		stages.set(6, ins.get(iCount-4));
				    		iCount++;
				    		
				    	}                    	
				    }*/	
				}
				if(z >=4)
				{
					stagePrev.set(0, stages.get(0));
					stagePrev.set(1, stages.get(1));
					stagePrev.set(2, stages.get(2));
					stagePrev.set(3, stages.get(3));
					stagePrev.set(4, stages.get(4));
					stagePrev.set(5, stages.get(5));
					stagePrev.set(6, stages.get(6));
					//stagePrev.set(7, stages.get(7));
					
					//Delay stage
					stages.set(4, stagePrev.get(3));
					if(z>4)
						stages.set(5, stagePrev.get(4));
					//stage.executeALU2(ins.get(iCount-2), registers,memoryLocations);
					
					//MEM stage
					/*if((stagePrev.get(5)== null  || stagePrev.get(5).operation.equals("BUBBLE") ) && 
							!(stagePrev.get(3).equals("BZ") || (stagePrev.get(3).equals("BZ")) ||
							(stagePrev.get(3).equals("BAL")) || (stagePrev.get(3).equals("JUMP")) ) ){
						stages.set(6, stagePrev.get(3));
						stage.memory(stagePrev.get(2),registers, memoryLocations);						
					}
					else{
						stages.set(6, stagePrev.get(5));				
					}*/
						
					//WB stage
					if(stagePrev.get(5) != null ){//&& stagePrev.get(5).)
						stages.set(6, stagePrev.get(5));
						stage.writeBack(stagePrev.get(5), registers, registersActuals, physicalRegisters, backPhysicalRegisters);
					}
					else if(stagePrev.get(3) != null ){//&& stagePrev.get(5).){
							
			    		stages.set(6, stagePrev.get(3));
			    		stage.writeBack(stagePrev.get(3), registers, registersActuals, physicalRegisters, backPhysicalRegisters);
					}	
					
					//ALU2 stage
					stages.set(3, stagePrev.get(2));
					stage.executeALU2(stagePrev.get(2), registers,memoryLocations,physicalRegisters);
					//execute branch
					if( flagBranch == true && (ins.get(iCount-2).operation.equals("BZ") ||  ins.get(iCount-2).operation.equals("BNZ") ||
							 ins.get(iCount-2).operation.equals("BNZ"))  ){
						stagePrev.set(0, ins.get(iCount));
						stagePrev.set(1, ins.get(iCount-1));
						stagePrev.set(4, ins.get(iCount-2));
						flagBranch = stage.executeBranch(ins.get(iCount-2));
						if(flagBranch)
							iCount = iCount +  (Integer.parseInt(ins.get(iCount-2).des)/4);
						//flagBranch = false; when brn instrn is in exe stage cycle = 15
					}
					
					//add bubble for branch
		    		if( (ins.get(iCount-2).operation.equals("BZ") || ins.get(iCount-2).operation.equals("BNZ") ) ){
		    			stages.set(2, bubleInstruction);
		    			stages.set(4, bubleInstruction);
		    			flagBranch =true;
		    			bubbleStat++;
		    			//branchTaken = true;
		    		}
		    		
		    		//add bubble for dependency
		    		else if(ins.get(iCount-2).operation.equals("STORE") &&  
		    				ins.get(iCount-2).des.equalsIgnoreCase(stages.get(3).des) || 
		    				ins.get(iCount-2).src1.equalsIgnoreCase(stages.get(3).des) ){
		    			stages.set(2, bubleInstruction);
		    			bubbleStat++;
		    		}
		    			
		    		
		    		else if(ins.get(iCount-2).src1.equalsIgnoreCase(stages.get(3).des) || 
						ins.get(iCount-2).src2.equalsIgnoreCase(stages.get(3).des) ||
						ins.get(iCount-2).src2.equalsIgnoreCase(stages.get(4).des) ||	//	for arithmatic instrn; do brnFlag
						ins.get(iCount-2).src1.equalsIgnoreCase(stages.get(4).des)	)
						{
		    			bubbleStat++;
						stages.set(2, bubleInstruction);
						}
					else{
						if(branchTaken){//will go here when branch instrn is in Branch FU stage
							iCount -=2;
							stages.set(0, ins.get(iCount));
							stages.set(1, bubleInstruction);
							stages.set(2, bubleInstruction);
							stages.set(4, bubleInstruction);bubbleStat++;
							branchTaken = false;
							iCount++;
						}else{
							stages.set(0, ins.get(iCount));
							stages.set(1, ins.get(iCount-1));
							if(!flagBranch && !( ins.get(iCount-2).operation.equals("BZ")  || ins.get(iCount-2).operation.equals("BNZ") ||  //check brnFlag to not set stage(2)
									ins.get(iCount-2).operation.equals("BAL") || ins.get(iCount-2).operation.equals("JUMP") )){
								if(!stagePrev.get(1).operation.equals("BUBBLE"))
									stages.set(2, ins.get(iCount-2));// do iCount++ here
								iCount++;	
							}
								
							else{//will go here when branch instrn is in D/RF FU stage
								stages.set(0, stagePrev.get(0));
								stages.set(1, stagePrev.get(1));
								stages.set(4, stagePrev.get(4));
								flagBranch = false;
								branchTaken = true;
								
							}
						}	
					}                		
					                    
				}
				for(int i =0; i<7; i++){
					System.out.print( stages.get(i) == null ? " NULL" : "  "+stages.get(i).operation );
				    }
				    
				}
				break;
			case 3: 
			
				System.out.println("Details of every stage was displayed after end of every cycle");
				System.out.println("Enter command");
				System.out.println("1 Print_map_tables");
				System.out.println("2 Print_IQ");
				System.out.println("3 Print_ROB");
				System.out.println("4 Print_URF");
				System.out.println("5 Print_Memory");
				System.out.println("6 Print_Stats");
				//Scanner reader = new Scanner(System.in);
				n = reader.nextInt();
				
				if(n==1){
					System.out.println("RAT");
					for(int u=0; u<16; u++){
						System.out.println("R"+u+" P" + physicalRegisters.get(u).number +" "+
								physicalRegisters.get(u).status+" "+ physicalRegisters.get(u).value+
								" "+physicalRegisters.get(u).valid);
					}
					System.out.println("R-RAT");
					for(int u=0; u<16; u++){
						System.out.println("R"+u+" P" + backPhysicalRegisters.get(u).number +" "+
								backPhysicalRegisters.get(u).status+" "+ backPhysicalRegisters.get(u).value+
								" "+backPhysicalRegisters.get(u).valid);
					}
					
					
				}else if(n==2){
					System.out.println("IssueQueue");
					for(int u=0; u<issueQueue.size(); u++){
						System.out.println(issueQueue.get(u).toString());
					}
				}else if(n==3){
					System.out.println("ROB");
					for(int u=0; u<issueQueue.size(); u++){
						System.out.println(rob.get(u).dest_ar);
					}					
					
				}else if(n==4){
					System.out.println("URF");
					for(int u=0; u<16; u++){
						System.out.println("R"+u+" P" + backPhysicalRegisters.get(u).number +" "+
								backPhysicalRegisters.get(u).status+" "+ backPhysicalRegisters.get(u).value+
								" "+backPhysicalRegisters.get(u).valid);
					}
					
					
					
				}else if(n==5){
					System.out.println("memories");
					for(int l = 0; l<100; l++){
						System.out.println("R"+l +"="+memoryLocations.get(l));
					}					
				}else if(n==6){
					System.out.println("Total stalls = "+bubbleStat);
				}
				System.out.println("registers");
				for(int l = 0; l<15; l++){
					System.out.println("R"+l +"="+registers.get(l));
				}
				break;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Do you want to continue? Type Y or y to conti1nue...");
				s = br.readLine();
		}while(s.equals("Y") || s.equals("y"));
	}
      
}
