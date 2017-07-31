package apex_v2;

import java.lang.reflect.Array;
import java.util.ArrayList;


class Stages {
	
	void executeALU1(){
		
	}
	void executeALU2(Instruction instruction, ArrayList<Integer> registers, ArrayList<Integer> memoriyLocation,
			ArrayList<PhysicalRegister> physicalRegisters){
		if (instruction.operation.equals("BUBBLE")){
			return;
		}
		int destRegIndex = Integer.parseInt(instruction.des.substring(1));
		int src1RegIndex = 0;
		int src2RegIndex = 0;
		int result;
		if (instruction.operation.equals("ADD") || instruction.operation.equals("SUB") || instruction.operation.equals("MUL") ||
				instruction.operation.equals("DIV") || instruction.operation.equals("AND") || instruction.operation.equals("OR") || 
				instruction.operation.equals("EX-OR") ){
			
			if(instruction.src1 != "0")
				src1RegIndex = Integer.parseInt(instruction.src1.substring(1));
			if(instruction.src2 != "0")
				src2RegIndex = Integer.parseInt(instruction.src2.substring(1));
			result = arithmaticOperation(registers.get(src1RegIndex), registers.get(src2RegIndex), instruction.operation);
			if(result == 0)
				Apex.zeroFlag = 0;
			else
				Apex.zeroFlag =1;
			registers.set(destRegIndex, result);
			physicalRegisters.get(destRegIndex).value =  result;
			System.out.println("updated register P"+destRegIndex+ " value = "+ registers.get(destRegIndex));
		}
		else if(instruction.operation.equals("MOVC")){
			result = Integer.parseInt(instruction.src1);
			registers.set(destRegIndex, result);
			physicalRegisters.get(destRegIndex).value =  result;
			System.out.println("updated register P"+destRegIndex+ " value = "+ registers.get(destRegIndex));
		}
		else if(instruction.operation.equals("LOAD") || instruction.operation.equals("STORE") ){
			src1RegIndex = Integer.parseInt(instruction.src1.substring(1));
			destRegIndex = Integer.parseInt(instruction.des.substring(1));
			result = arithmaticOperation(registers.get(src1RegIndex), Integer.parseInt(instruction.src2), "ADD");
			if(instruction.operation.equals("STORE")){
				memoriyLocation.set(result/4, registers.get(destRegIndex));
				System.out.println("Memory location updated  to MEM["+result/4 +"] = "+registers.get(destRegIndex));
			}else{
				registers.set(destRegIndex, result);
				physicalRegisters.get(destRegIndex).value =  result;
				System.out.println("updated register P"+destRegIndex+ " value = "+ registers.get(destRegIndex));
			}
		}
		else if(instruction.operation.equals("BNZ") || instruction.operation.equals("BZ")){
			result = Integer.parseInt(instruction.src1);
			registers.set(destRegIndex, result);
			System.out.println("updated register R"+destRegIndex+ " value = "+ registers.get(destRegIndex));
		}
		else if(instruction.operation.equals("HALT")){
			System.out.println("HALT encountered");
			System.exit(0);
		}
		
	}
	boolean executeBranch(Instruction instruction){
		if(instruction.operation.equals("BZ"))
			if(Apex.zeroFlag == 0)
				return true;
		if(instruction.operation.equals("BNZ"))
			if(Apex.zeroFlag != 0)
				return true;
		if(instruction.operation.equals("BAL"))
				return true;
		if(instruction.operation.equals("JUMP"))
				return true;
		return false;
	}
	void executeDelay(){
		
	}
	void memory(Instruction instruction, ArrayList<Integer> registers, ArrayList<Integer> memoryLocations){
		int destRegIndex = Integer.parseInt(instruction.des.substring(1));
		int src1RegIndex = 0;
		int src2RegIndex = 0;
		int result;
		if(instruction.operation.equals("LOAD")){
			registers.set(destRegIndex, Integer.parseInt(memoryLocations.get(src1RegIndex)+instruction.src2));
			if(instruction.src1 != "0")
				src1RegIndex = Integer.parseInt(instruction.src1.substring(1));
			if(instruction.src2 != "0")
				src2RegIndex = Integer.parseInt(instruction.src2.substring(1));
		}			
	}
	void writeBack(Instruction instruction, ArrayList<Integer> registers, ArrayList<Integer> registersActuals,
			ArrayList<PhysicalRegister> physicalRegisters, ArrayList<PhysicalRegister> backPhysicalRegisters){
		System.out.println(instruction +"in WB");
		int destRegIndex;
		destRegIndex = Integer.parseInt(instruction.des.substring(1));
		//physicalRegisters.set( instruction.des, element)
		
	}
	int arithmaticOperation(int src1, int src2, String op){
		if(op.equals("ADD")){
			return src1 + src2;
		}else if(op.equals("DIV")){
			return src1 / src2;
		}else if(op.equals("MUL")){
			return src1 * src2;
		}else if(op.equals("SUB")){
			return src1 - src2;
		}else if(op.equals("AND")){
			return src1 & src2;
		}else if(op.equals("OR")){
			return src1 | src2;
		}else if(op.equals("EX-OR")){
			return src1 ^ src2;
		}
		System.out.println(op);
		return 1;
	}
}

