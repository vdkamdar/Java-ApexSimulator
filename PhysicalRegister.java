package apex_v2;

import java.util.ArrayList;

public class PhysicalRegister {

	int number;
	int value;
	int valid; // 0 = valid
	int status; //0 = free 1 = allocated 2 committed 
	public PhysicalRegister(int number,int value, int valid, int status) {
		this.number = number;
		this.status = status;
		this.value = value;
		this.valid = valid;
	}
	
	static PhysicalRegister allocateNew (ArrayList<PhysicalRegister> physicalRegisters){
		for(int i=0; i<physicalRegisters.size(); i++){
			if(physicalRegisters.get(i).status == 0)
				return physicalRegisters.get(i); 
		}
		return null;				
	}
}
