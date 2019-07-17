package jsonToText;

import java.util.Arrays;
import java.util.List;

public class Device {
	private String name;
	private String type;
	
	public Device(String name, String type){
		this.name = name;
		this.type = type;			
	}

	private String buildPattern(List<String> matchString){
		  StringBuilder sb = new StringBuilder(5);
		  sb.append("^");
		  for(String match: matchString){
			  String added = String.format("(?=.*%s)", match.toLowerCase());
			  sb.append(added);
		  }		
		  sb.append(".*$");
		  return sb.toString();		  
	}
	
	private String unmatchPatternString(List<String> unmatchString){
		  StringBuilder sb = new StringBuilder(10);
		  sb.append("("); 
		  for(int i=0; i<unmatchString.size()-1; i++){
			  String added = String.format("%s|", unmatchString.get(i).toLowerCase());
			  sb.append(added);
		  }		 
		  sb.append(unmatchString.get(unmatchString.size()-1).toLowerCase()+")"); 
		  return sb.toString();
	  }
	
	private List<String> splitString(String input){
		List<String> output = Arrays.asList(input.split(" ")); 
		String last = output.get(output.size() - 1);
		if( last.equals("+") ){
			output.set(output.size() - 1, "\\+");
		}
		return output;
	} 	
	
	public String getSearchString(){
		return buildPattern(splitString(name));
	}
	
	public String getUnmatchString(){
		return unmatchPatternString(splitString(name));
	}
	
	public String getType(){
		return type;
	}
	
	public String getName(){
		if(name.toLowerCase().contains("plus")){
			String output = name.substring(0, name.length()-5)+" +";
			return output;
		} else return name;		
	}
	
}
