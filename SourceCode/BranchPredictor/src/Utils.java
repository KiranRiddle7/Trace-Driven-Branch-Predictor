import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Properties;

public class Utils {

	static String hexToBin(String s) {
		  return new BigInteger(s, 16).toString(2);
		}
	
	static String decToHex(Integer i) {
		if(i<16)
			return "0"+Integer.toHexString(i);// need this hack or else when indexing into the BHT 01 will be 1 
											 //and return null pointer exception
		else
		  return Integer.toHexString(i);
		}
	static String decToBin(Integer i, int max){
		String result = String.format("%"+max+"s", Integer.toBinaryString(i)).replace(' ', '0');
		return result;
		}
	static int binToDec(String bin){
		return Integer.parseInt(bin, 2);
	}
	
	static String shiftreg(String old_global, int new_value){
    	
    	StringBuilder sb = new StringBuilder(String.valueOf(old_global));
    	sb.append(new_value); // append new value
    	String new_global = sb.substring(1); //shift left once
    	
    	return new_global;
    }
	
	public static Properties readProperties(String file_path) throws Exception{
		Properties prop = new Properties();
		FileInputStream fistream = null;
		InputStreamReader inputstream = null;
		
		
		fistream = new FileInputStream(file_path);
		inputstream = new InputStreamReader(fistream,"UTF-8");
		
		
		prop.load(inputstream);
		return prop;
	}
	
	public static void printStatistics(int num_branches,int num_mispred){
		
		float mispred_rate = (float)num_mispred/num_branches;
		
		System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
		System.out.println("*  ");
		System.out.println("*  Total number of Branch Instructions found in trace: "+ num_branches);
		System.out.println("*  ");
		System.out.println("*  Total number of Mispredicted Branch Instructions found in trace: "+ num_mispred);
		System.out.println("*  ");
		System.out.println("*  Misprediction Rate: "+ mispred_rate*100 +"% ");
		System.out.println("*  ");
		System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
		
	}
}
