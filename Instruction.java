/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apex_v2;

class Instruction {
    int address;
    String operation;
    String des;
    String src1;
    String src2;
    boolean status;
   
    //IssueQueue Entry properties
    String FUType;
	String destARF;
	int src1Value;
	int src2Value;
	int src1ReadyBit;
	int src2ReadyBit;
    
    Instruction(int add,String op, String d, String s1, String s2)
   {
       address = add;
       operation = op;
       des = d;
       src1 = s1;
       src2 = s2;
   }

@Override
public String toString() {
	// TODO Auto-generated method stub
	return this.operation;
}   

static Instruction renameInstruction(Instruction instruction){
	Instruction newInstruction = instruction;
	newInstruction.des = newInstruction.des.replace('R', 'P');
	if (instruction.operation.equals("ADD") || instruction.operation.equals("SUB") || instruction.operation.equals("EX-OR") ||
			instruction.operation.equals("DIV") || instruction.operation.equals("AND") || instruction.operation.equals("OR")  )
		newInstruction.FUType = "integerALU";
	if (instruction.operation.equals("MUL") )
		newInstruction.FUType = "multiplicationALU";
	if (instruction.operation.equals("ADD") || instruction.operation.equals("SUB") || instruction.operation.equals("EX-OR") ||
			instruction.operation.equals("DIV") || instruction.operation.equals("AND") || instruction.operation.equals("OR")  )
		newInstruction.FUType = "integerALU";
	if(instruction.operation.equals("LOAD") || instruction.operation.equals("STORE") )
		newInstruction.FUType = "LSFU";
	if(instruction.operation.equals("BNZ") || instruction.operation.equals("BZ") || instruction.operation.equals("BAL") ||
			instruction.operation.equals("JUMP"))
		newInstruction.FUType = "branchFU";
	return newInstruction;
}
}