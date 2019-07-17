package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Luthman extends CheckDealer {
	
	static JSONObject jsonProduct(String text){
		JSONParser parser = new JSONParser();
		JSONObject json = null;
    	try {
			json = (JSONObject) parser.parse(text);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
    	return json;
	}
	
	static String getSubstring(String text){
		
			int start = text.indexOf("var products")+16;
			int end = text.indexOf("}];")+2;
			
			return text.substring(start, end);		
				
	}
	
	static List<String> getSubstring2(List<String> baseText){
		List<String> text = baseText;
				
		if(text.get(0).length()<5){
			return text;
		} else {
			int start = text.get(0).indexOf("{");
			int end = text.get(0).indexOf("}");
			text.add(text.get(0).substring(start, end));
			text.set(0, text.get(0).substring(end+1)); 
			return getSubstring2(text);		
		}		
	}
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		
		return report;		
	}	


}
