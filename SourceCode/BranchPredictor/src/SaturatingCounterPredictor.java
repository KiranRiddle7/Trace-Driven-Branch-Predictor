import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SaturatingCounterPredictor {
	
	//one bit saturating counter based predictor
	// uses 1 bit to indicate branch direction
	public static void oneBit_predictor(List<String> tracelist){
			
			Map<String,Integer> BHT = new LinkedHashMap<String,Integer>(); // The key is the last few bits of pC
																		   // and the value is an integer
			
			int total_count=0;
			int mispred_count =0 ; //to count number of mispredictions
			
			BHT=initialize_BHT(256, BHT);
			
		//Start Prediction
			for (String entry : tracelist){
			
				
				//add it to BHT- make the T/NT to 1/0 first
					int pred_value;
					String index_bht = entry.substring(4, 6);
					pred_value = BHT.get(index_bht);
					
					//System.out.println("Branch found: "+ entry);
					
					//System.out.println("Substring got:"+index_bht);
					
					if(entry.contains("T")){//actual value is taken
						int actual_value=1; //converting T as 1
						if(actual_value != pred_value){
							mispred_count++;
							//update BHT
							BHT.put(index_bht, actual_value);
						}
							total_count++; //increment branch instruction count
						
						
					}
					else{//entry.getValue() == NT
						int actual_value=0;// this can be made n
						if(actual_value != pred_value){
							mispred_count++;
							//update BHT
							BHT.put(index_bht, actual_value);
						}
						
							total_count++;//increment branch instruction count
						
					    }
					 
					
				
					
			}
			
			Utils.printStatistics(total_count, mispred_count);
			
			
		}


	//n-bit Branch predictor

	public static void nBit_predictor(List<String> tracelist, int n){
		
		Map<String,Integer> BHT = new LinkedHashMap<String,Integer>();
		int total_count=0;
		int mispred_count =0 ; //to count number of mispredictions
		int pred_value;
		
		BHT = initialize_BHT(256, BHT);
		
		for (String entry : tracelist){
				
				String index_bht = entry.substring(4, 6);
				pred_value = BHT.get(index_bht);
			//	System.out.println("BHT Index :" +index_bht+ " Branch direction:" + entry);
				if(entry.contains("T")){
					
					if(pred_value<(n/2)){
						mispred_count++;
						pred_value = (pred_value < (n-1))? pred_value+1 : n-1; //Increment count but saturate at n-1.Saturating counter logic
						BHT.put(index_bht, pred_value);
					}
					else{
						//counter still needs to increment- case of strongly taken
						pred_value = (pred_value < (n-1))? pred_value+1 : n-1; //Saturating counter logic
					}
					total_count++;
				}
				else{//entry.getValue().equals("NT")
					
					if(pred_value>(n/2)){
						mispred_count++;
						pred_value = (pred_value ==0)? 0: pred_value-1; //Decrement counter but saturate at 0 
						BHT.put(index_bht, pred_value);
					}
					else{
						//counter still needs to decrement- case of weakly taken- still needs to decrement and remain at 0
						pred_value = (pred_value ==0)? 0: pred_value-1; //Saturating counter logic
					}
					total_count++;
					
					}

				
			
		}
		Utils.printStatistics(total_count, mispred_count);
	}

	public static Map<String,Integer> initialize_BHT(int config_size,Map<String,Integer> BHT){
		
			BHT = new LinkedHashMap<String,Integer>();
			for (int i=0; i < config_size; i++){
			BHT.put(Utils.decToHex(i), 0); //initialize the branch history table to Not Taken to start with
			}
			return BHT;
	}
	}
