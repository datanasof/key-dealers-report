package jsonToText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import dealersToCheck.Credentials;

public class TextJson {
	static Logger logger = Logger.getAnonymousLogger();
		
	public static void listToFile(List<List<Object>> myList){		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String fileNameDate=dateFormat.format(previousDay(0));	
		
		try (Writer writer = new FileWriter(String.format("json-reports/%s.json",fileNameDate))) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(myList, writer);
		    writer.flush();
		    writer.close();
		    System.out.println("Success with writing to file!");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred. Can not write to file named "+ fileNameDate,e);
	    }
	}
	
	public static <T> void textToFile(List<List<Object>> newReport) throws IOException{
		String oldReport = readCSV("csv-reports/summary.csv");
		String myText = oldReport.concat(listToCSV(newReport));
		//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//String fileNameDate=dateFormat.format(previousDay(0));	
		writeCSVfile(myText, "csv-reports/summary.csv");		
	}
	
	public static void fixProductNames(String csvFile) throws IOException{
		DeviceList devList = new DeviceList();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
        	
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
            	StringBuilder sbline = new StringBuilder();
                String[] product = line.split(cvsSplitBy);
                product[6] = devList.getProductInfoFromStr(product[6])[0];
                for(int i=0;i<product.length-1;i++){
                	sbline.append(product[i] + ",");                	
                }
                sbline.append(product[product.length-1] + "\n");
                sb.append(sbline.toString());
            }
            writeCSVfile(sb.toString(), csvFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }		
	}
	
	public static List<List<String>> fileToList(){
		java.lang.reflect.Type REVIEW_TYPE = new TypeToken<List<List<String>>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		}.getType();
		Gson gson = new Gson();
		JsonReader reader;
		for(String str:getListOfLastWeekDates()){
			try {			
				reader = new JsonReader(new FileReader(String.format("json-reports/%s.json",str)));
				List<List<String>> data = gson.fromJson(reader, REVIEW_TYPE);
				return data;
			} catch (FileNotFoundException e) {
				logger.log(Level.WARNING, "An exception occurred. There is NO report from "+ str,e);				
			}	
		}
		return null;
	}
	
	private static void writeCSVfile(String text, String filename){
		try (Writer writer = new FileWriter(filename)) {
			writer.write(text); 
		    writer.flush();
		    writer.close();
		    System.out.println("Success with writing to CSV file - "+filename);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred. Can not write to file named "+filename,e);
	    }
	}
	
	private static <T> String listToCSV(List<List<T>> toBeEdited){
		StringBuilder sb = new StringBuilder();
		for(List<T> row: toBeEdited){
			for(int i=0;i<row.size()-1;i++){
				Object o = row.get(i);
				String s = String.valueOf(o);
				if(i==5) s = s.replace(",", " ");
				sb.append(s + ",");				
			}
			sb.append("\n");
		}		
		return sb.toString();		
	}
	
	
	public static Map<String, Integer> getNpositionsPerCategory(){
		try{
			List<List<String>> report = fileToList();
			List<String> firstProductReport = report.get(0);
			String dealer = firstProductReport.get(0);
			String category = firstProductReport.get(1);
			Map <String, Integer> positions = new HashMap<String, Integer>();
			int categoryPositions=0;
			for(List<String> row:report){
				String currentDealer = (String) row.get(0);			
				String currentCattegory = (String) row.get(1);	
				if(currentDealer.equals(dealer) && currentCattegory.equals(category)){
					categoryPositions ++;
				} else {
					positions.put(dealer+category, categoryPositions);
					categoryPositions=0;
				}
				dealer=currentDealer;
				category=currentCattegory;
			}			
			return positions;	
		} catch (Exception e){
			logger.log(Level.SEVERE, "An exception occurred. Can not read previous positions from the report",e);
			return null;		
		}
		
	}
	
	public static Map<String, Integer> getPromosPerProduct(){
		try{
			List<List<String>> report = fileToList();
			
			Map <String, Integer> promos = new HashMap<String, Integer>();
			
			for(List<String> row:report.subList(1, report.size()-1)){
				
				String promo = (String) row.get(10);
				if(Credentials.reportCodes.containsKey(promo)){
					String dealer = (String) row.get(0);
					String brand = (String) row.get(4);
					String device = (String) row.get(5);
					promos.put(dealer+brand+device, Credentials.reportCodes.get(promo));
				} 
			}			
			return promos;	
		} catch (Exception e){
			logger.log(Level.SEVERE, "An exception occurred. Can not get the list of promotions from the report",e);
			return null;		
		}
		
	}
	
	public static HashSet<String> notAvailableAtThomann(){
		HashSet <String> notAvail = new HashSet<String>();
		try{
			List<List<String>> report = fileToList();
			for(List<String> row:report.subList(1, report.size()-1)){
				String dealer = (String) row.get(0);
				String avail = (String) row.get(7);
				if(Credentials.trackDealersProductsAvailability.contains(dealer)&&!Credentials.productInStock.contains(avail)){					
					String brand = (String) row.get(4);
					String device = (String) row.get(5);
					notAvail.add(dealer+brand+device);
				} 
			}					
		} catch (Exception e){
			logger.log(Level.SEVERE, "An exception occurred. Can not get the list of promotions from the report",e);
			return null;		
		}
		return notAvail;			
	}
	
	public static String readCSV (String filename) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader (filename));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}
	
	private static Date previousDay(int back) {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -back);
	    return cal.getTime();
	}	

	private static List<String> getListOfLastWeekDates() {
			List<String> dates = new ArrayList<String>();
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        for(int i=0;i<8;i++){
	        	dates.add(dateFormat.format(previousDay(i)));
	        }
	        return dates;
	}
	
	
	 

}
