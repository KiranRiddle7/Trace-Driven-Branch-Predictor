import java.util.*;


public class TwoLevelCorrelatingPredictor {

	
	public static void correlating_m_nPredictor(List<String> tracelist,int m, int n){
		
		//This will be a map of Maps . First Map's key is PC's least significant bits, second
		//map's key is global history value, 
		Map<String,HashMap<String,Integer>> PHT = new LinkedHashMap<String,HashMap<String,Integer>>();
		
		int pred_value = 0; //value as given by n-bit counter in the Pattern history Table.
		int mispred_count = 0;
		int total_count = 0;
		int globhist_buffer = 0; // this will be decimal value in this variable but binary string in mapindex
		
		//Initialize PHT Map
		PHT=initialize_PHT(256,m);
		
		for(String entry:tracelist){
			String index_bht = entry.substring(4, 6);
			String new_global;
			Map<String,Integer> subMap= new HashMap<String,Integer>();
			Map<String,Integer> updatedMap= new HashMap<String, Integer>();
			
			subMap = PHT.get(index_bht);
			pred_value = subMap.get(Utils.decToBin(globhist_buffer,m));// indexing based on the global history value- gives
													 //value of saturating counter
			
			
			//n=1 is special case handle separate
			if(n > 1){
			
			if(entry.contains("T")){
				if(pred_value<(n/2)){//change this to pow(2) later
					mispred_count++;
					pred_value = (pred_value < (n-1))? pred_value+1 : n-1; //Saturating counter logic
					updatedMap = PHT.get(index_bht);// has to be done like this else all branches gets updated
					updatedMap.put(Utils.decToBin(globhist_buffer,m), pred_value );
					
					//Update global history buffer
					
					new_global = Utils.shiftreg(Utils.decToBin(globhist_buffer,m), 1);// this value is 1 because branch was supposed to be taken
					globhist_buffer = Utils.binToDec(new_global);
					
					//System.out.println("Global in shiftreg: "+ new_global+ "Global in buffer: "+globhist_buffer);
				}
				else{
					//counter still needs to increment- case of strongly taken
					pred_value = (pred_value < (n-1))? pred_value+1 : n-1; //Saturating counter logic
					
					//Update global history buffer- still
					new_global = Utils.shiftreg(Utils.decToBin(globhist_buffer,m), 1); // this value is 1 because branch was supposed to be taken
					globhist_buffer = Utils.binToDec(new_global);
				//	System.out.println("Global in shiftreg: "+ new_global+ "Global in buffer: "+globhist_buffer);
				}
				total_count++;
			}
			else{//entry.contains("N")
				if(pred_value>(n/2)){//this will be mis-prediction
					mispred_count++;
					pred_value = (pred_value ==0)? 0: pred_value-1; //Decrement counter but saturate at 0 
					subMap.put(Utils.decToBin(globhist_buffer,m), pred_value);
					
					//Update global history buffer
					
					new_global = Utils.shiftreg(Utils.decToBin(globhist_buffer,m), 0);// this value is 0 because branch was supposed to be taken
					globhist_buffer = Utils.binToDec(new_global);
					
					//System.out.println("Global in shiftreg: "+ new_global+ " Global in buffer(dec value): "+globhist_buffer);
					}
				else{
					//counter still needs to increment- case of weakly taken- still needs to decrement and remain at 0
					pred_value = (pred_value ==0)? 0: pred_value-1; //Saturating counter logic
					////Update global history buffer - Still
					new_global = Utils.shiftreg(Utils.decToBin(globhist_buffer,m), 0);
					globhist_buffer = Utils.binToDec(new_global);
					
					//System.out.println("Global in shiftreg: "+ new_global+ " Global in buffer(dec value): "+globhist_buffer);
					}
				total_count++;
				}
			
			}
			
			
			//n = 1 case
			else{
				if(entry.contains("T")){
					int actual_value = 1 ;
					if(pred_value != actual_value){//this will be mis-prediction
						mispred_count++;
						pred_value = actual_value; //update predicted value
						updatedMap = PHT.get(index_bht);// has to be done like this else all branches gets updated
						updatedMap.put(Utils.decToBin(globhist_buffer,m), pred_value );
						
						//Update global history buffer
						
						new_global = Utils.shiftreg(Utils.decToBin(globhist_buffer,m), 1);// this value is 1 because branch was supposed to be taken
						globhist_buffer = Utils.binToDec(new_global);
						
						
					}
					
					total_count++;
				}
				else{//entry.contains("N")
					int actual_value = 0;
					if(pred_value!= actual_value){//this will be mis-prediction
						mispred_count++;
						pred_value = actual_value; //update predicted value
						subMap.put(Utils.decToBin(globhist_buffer,m), pred_value);
						
						//Update global history buffer
						
						new_global = Utils.shiftreg(Utils.decToBin(globhist_buffer,m), 0);// this value is 0 because branch was supposed to be not taken
						globhist_buffer = Utils.binToDec(new_global);
						
						}
					total_count++;	
				
					}

				
				}
			
		}	
		Utils.printStatistics(total_count, mispred_count);
	}
	
	/** Initialize the two level predictor basic data structure-Pattern History Table(PHT)
	 *  The branch is always predicted not taken to begin with and so is the counter
	 *  Initialize both to zero 
	 * @param BHT_size
	 * @param m
	 * @return
	 */
	public static Map<String,HashMap<String,Integer>> initialize_PHT(int BHT_size,int m){
		
		
		Map<String,HashMap<String,Integer>> PHT = new LinkedHashMap<String,HashMap<String,Integer>>();
		
		for (int i=0; i < BHT_size; i++){
			HashMap<String,Integer> subMap = new LinkedHashMap<String,Integer>();
			for(int j=0;j<Math.pow(2,m);j++){// m-bit decides the number of global values
				subMap.put(Utils.decToBin(j,m), 0); //First time initialize every counter to 0(NT)
				
			}
			
		PHT.put(Utils.decToHex(i), subMap); //initialize the branch history table to Not Taken to start with
		}
		return PHT;
}
}

    
    