package apex_v2;

public class ROBEntry {
	//address of dispatched instruction
	public String address;
    public String dest_ar;
    public String value_ar;
    public String exCodes;
    public boolean status;
    
    public ROBEntry(String address, String des, String val, String ex, boolean st) {

        address = address;
        dest_ar = des;
        value_ar = val;
        exCodes = ex;
        status = st;
	}
    
}
