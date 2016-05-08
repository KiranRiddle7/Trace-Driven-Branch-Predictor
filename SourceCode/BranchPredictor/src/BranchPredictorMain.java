import java.io.*;
import java.util.*;

public class BranchPredictorMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//Read Properties file
		Properties props= Utils.readProperties(System.getProperty("user.dir")+"/config.properties");
		System.out.println("*----------------------------------------------------------------------------*");
		System.out.println("*  Location of config file:"+ System.getProperty("user.dir")+"/config.properties");
		System.out.println("*  Reading configuration file properties.....................................Done.");
		System.out.println("*  Parsing trace file.............................................................");
		
		
		//Parse the trace file and put it in list
		List<String> tracelist = new LinkedList<String>();
		
		FileInputStream fistream = null;
		InputStreamReader istream = null;
		BufferedReader buffer = null;
		
		try {
			fistream = new FileInputStream(System.getProperty("user.dir")+"/"+props.getProperty("tracefile"));
			istream = new InputStreamReader(fistream,"UTF-8");
			buffer = new BufferedReader(istream);
			
			String line = buffer.readLine();

			while(line!=null){
				
				String[] token = line.split("\\s+");//separated by white spaces
				if(token[6].equals("T") || token[6].equals("N")){//read program counter and branch field
					tracelist.add(token[1]+"-"+token[6]); // append to list
					} 
				line = buffer.readLine();
				
			}
				
				
				
		}
		
		finally {
			if(buffer !=null) 
				{
				buffer.close();
				}
			if(istream !=null) 
			{
			istream.close();
			}
			if(fistream !=null) 
			{
			fistream.close();
			}
			
		}
		
		System.out.println("*----------------------------------------------------------------------------*");
		
		//System.out.println(tracelist.size());	
		String pred_type = props.getProperty("type");
		
		if(pred_type.equals("Type1")){
			System.out.println("* Starting Type1 (n-bit) Counter Based Branch Prediction......");
			if(props.getProperty("number_bits").trim().equals("1"))
			{
				System.out.println("Starting 1-bit Counter Based Branch Prediction......");
			SaturatingCounterPredictor.oneBit_predictor(tracelist);
			}
			else{
				int num_bits = Integer.parseInt(props.getProperty("number_bits").trim());
			SaturatingCounterPredictor.nBit_predictor(tracelist,num_bits);	
			}
		}
		else if(pred_type.equals("Type2")){
			System.out.println("* Starting Type-2 (m,n) Two-Level Correlated Branch Prediction......");
			int m=0;
			int n=0;
			if(props.getProperty("ghistory_buff").trim().equals("0")){
				m = 0;
				n = Integer.parseInt(props.getProperty("counter_bits").trim());
			}
			else if(props.getProperty("counter_bits").trim().equals("0")){
					
				System.out.println("Invalid counter configuration");
				
			}
			else{
			m = Integer.parseInt(props.getProperty("ghistory_buff").trim());
			n = Integer.parseInt(props.getProperty("counter_bits").trim());
			}
			
			if(m != 0){
			TwoLevelCorrelatingPredictor.correlating_m_nPredictor(tracelist, m, n);
			}
			//If m = 0, no global history register so should fall back on n-bit predictor
			else if( m== 0 && n==1){
			SaturatingCounterPredictor.oneBit_predictor(tracelist);	
			}
			else{
			SaturatingCounterPredictor.nBit_predictor(tracelist, n);
			}
		}
	
		else{
			System.out.println("Invalid Configuration provided for Type parameter. Specify Type to be Type1 or Type2");
		}
}
	
}