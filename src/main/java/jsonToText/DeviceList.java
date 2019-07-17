package jsonToText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import dealersToCheck.Credentials;
import main.GsheetsManager;

public class DeviceList {
	List<Device> productList;
	private Pattern unmatches; 
	
	public DeviceList() throws IOException{
		this.productList = new ArrayList<Device>();
		String spreadsheetId = Credentials.productsInfoSpreadsheetId;
	    String range = "devices!A2:B199";
	    GsheetsManager manager = new GsheetsManager();
	    List<List<Object>> values = manager.readSheet(spreadsheetId, range);
	    readQualifyValues(values);
	    this.unmatches = Pattern.compile(getUnmatch());
	}
	
	private void readQualifyValues(List<List<Object>> values){
		if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } 
		else {
        	for (List<?> row : values) {
        		try {
        			String name = (String) row.get(0);
        			String type = (String) row.get(1);
        			Device e = new Device(name,type);
        			productList.add(e);
        			}  
        		catch (Exception e) {
        			System.out.println(e);
        			break;
        		}       	  
        	} 
        }		
	}
	
	private String getUnmatch(){
		String output = null;				
		for (Device product : productList) {
			if (product.getType().equals("unmatch")) {
				output = product.getUnmatchString();							
			}		
		}
		return output;	
	}
	
	public String[] getProductInfoFromStr(String input) {
		String name = input;	
		String type = null;
		String[] deviceInfo = new String[]{name, type};
		
				
		try{input=input.substring(0,40);		
		} catch(Exception e){}
		
		for (Device device : productList) {			
			Pattern matches = Pattern.compile(device.getSearchString());					
			if (matches.matcher(input.toLowerCase()).find() && !unmatches.matcher(input.toLowerCase()).find()) {
				name = device.getName();		
				type = device.getType();
				deviceInfo[0]=name;
				deviceInfo[1]=type;
			}				
		}		
		return deviceInfo;		
	}
	
	
}
