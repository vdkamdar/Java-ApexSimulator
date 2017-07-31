package apex_v2;

public class IssueQueueEntry extends Instruction {

	String FUType;
	String destARF;
	int src1Value;
	int src2Value;
	int src1ReadyBit;
	int src2ReadyBit;
	

	IssueQueueEntry(int address, String op, String dest, String s1, String s2) {
		super(address, op, dest, s1, s2);
	}

}
