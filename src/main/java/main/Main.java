package main;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dealersToCheck.Credentials;
import dealersToCheck.Splitter;
import jsonToText.DeviceList;
import jsonToText.TextJson;

public class Main {	
	static Logger logger = Logger.getAnonymousLogger();
	
	public static String getDate(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		return dtf.format(localDate); 
	}	
	
	private static int getAllPositions(Map<String, Integer> myMap, String dealer, String category){
		String key = dealer+category;
		if (myMap.containsKey(key)) {
			   return myMap.get(key); 
		}
		return 0;
	}
	
	private static List<List<Object>> addDateToReport(List<List<Object>> myReport){
		List<List<Object>> report = myReport;		
		String date = getDate();
		for(List<Object> row:report){
			row.add(0, date);
		}
		return report;		
	}
	
	static void writeSummaryReport(GsheetsManager gmanager, List<List<Object>> myReport){
		try {
			List<List<Object>> oldReport = gmanager.readSheet(Credentials.infoSpreadsheetId, "Summary!A1:P99999");
			List<List<Object>> currentReport = addDateToReport(myReport);
			List<List<Object>> newReport = Stream.concat(oldReport.stream(), currentReport.stream()).collect(Collectors.toList());
			gmanager.writeSheet(Credentials.reportSpreadsheetId, "Summary!A1:P99999", newReport);
			TextJson.textToFile(currentReport);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred. Can not read/write latest report", e);			
		}		
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		GsheetsManager gmanager = new GsheetsManager();	
		System.out.println(Credentials.readStatus);     	
		List<List<Object>> dealerProducts = gmanager.readSheet(Credentials.infoSpreadsheetId, "Checking!A2:P999");
		Map<String, Integer> previousReportPositionsN = TextJson.getNpositionsPerCategory();
		List<List<Object>> report = new ArrayList<List<Object>>();
		List<List<Object>> reportAdditional = new ArrayList<List<Object>>();
		List<List<Object>> archive = new ArrayList<List<Object>>();
		DeviceList devList = new DeviceList();
		List <String> promos = new ArrayList<String>();
		List <String> newProducts = new ArrayList<String>();
		List <String> notAvailable = new ArrayList<String>();
		
		//GET PREVIOUS PROMOS LIST (MAP)
			Map <String, Integer> previousPromos = TextJson.getPromosPerProduct();
		//GET PREVIOUS AVAILABILITY AT THHOMANN
			HashSet<String> notAvailableAtThomann = TextJson.notAvailableAtThomann();
		
		
		report.add(Arrays.asList("Dealer","Category", "Position", "Page", "Brand", "Device", "N reviews", "Availability", "Price", "Reviews rate", "Sales rank in cat.",
				"Relative Position","Cat.Shelf % ps","Country","Units sold"));
		reportAdditional.add(Arrays.asList("Dealer","Category", "Position", "Page", "Brand", "Device", "Availability", "Country"));
		
		//add device positions for the next page
		String previousCategory = "";	
		String previousDealer = "";
		int previousLastPosition = 0;		
		
		for(List<Object>row:dealerProducts){
			int startingPosition;
			String dealer = (String) row.get(0);
			String category = (String) row.get(1);
			String pageNumber = (String) row.get(2);
			String pageURL = (String) row.get(3);
			String country = (String) row.get(4);
			int nPosPerCat = getAllPositions(previousReportPositionsN, dealer, category);
			
			if(category.equals(previousCategory)&&dealer.equals(previousDealer)){
				startingPosition = previousLastPosition;
			} else {
				startingPosition = 0;
				
			}
			
			System.out.println(dealer+" - "+Credentials.catStatus+" "+category);
			List<List<Object>> reportPerURL = new ArrayList<List<Object>>();
			try{
				reportPerURL = Splitter.getReportPerURL(dealer, category, pageNumber, pageURL, startingPosition, nPosPerCat, country);
			}catch(Exception e){
				logger.log(Level.WARNING, "An exception occurred. Can not reach dealers page - "+pageURL, e);
			}
			
			
			previousCategory = category;	//update last category
			previousDealer = dealer;
						
			if(reportPerURL != null){
				previousLastPosition = startingPosition+reportPerURL.size();		//update last position
				System.out.println("PREVIOUS LAST POSITION: "+previousLastPosition);
				for(List<Object> product:reportPerURL){
					if(Credentials.brandsToBeProcessed.contains(((String)product.get(4)).toLowerCase())){
						String productName = String.valueOf(product.get(5)).replace(",", " "); //remove "," symbol
						product.set(5, devList.getProductInfoFromStr(productName)[0]); //process PRODUCT NAME
						report.add(product);
						String brand = (String)product.get(4);
						if(brand.contains("Antelope")){
							if(country.contains("US")||country.contains("Canada")){
								List<Object> product1 = new ArrayList<Object>();
								product1.add(product.get(0));
								product1.add(product.get(1));
								product1.add(product.get(2));
								product1.add(product.get(3));
								product1.add(product.get(4));
								product1.add(product.get(5));
								product1.add(product.get(7));
								product1.add(product.get(13));							
													
								reportAdditional.add(product1);
							}
							
						}
						
						//CHECK PROMO TAG FOR MAILING 
						//KEY TO SEARCH IN THE PREVIOUS PROMOS LIST (MAP)
						String tag = (String)product.get(10);	
	            		String name = (String)product.get(5);
	            		String key = dealer + brand + name;
	            			            		
	            		boolean isTherePromo = tag.contains("PRICE DROP");
	            		boolean isThereNewProduct = tag.contains("NEW PRODUCT/BUNDLE");  
	            		//boolean isThomann = dealer.equals("Thomann");
	            		boolean wasNotAvailableAtThomann = notAvailableAtThomann.contains(key);
	            		boolean isDealerAvailabilityTracked = Credentials.trackDealersProductsAvailability.contains(dealer);
	            		
	            		
	            		//PREPARE AVAILABILITY WARNING EMAIL LIST (currently for Thomann only)
	            		if(isDealerAvailabilityTracked&&brand.contains("Antelope")){
	            			String available = (String)product.get(7);
	            			if(!Credentials.productInStock.contains(available)&&!wasNotAvailableAtThomann){
	            				
	            				notAvailable.add("dealer: "+dealer+", product: "+brand + " " + name + ", availability: "+available);
	            			}
	            		}
	            		//PREPARE PROMO & NEW TAGS LIST
	            		if(isTherePromo&&!previousPromos.containsKey(key)) { //add "promoTag&&!previousPromos.containsKey(key)"
	    					promos.add("dealer: "+dealer+", product: "+brand + " " + name + " - PRICE DROP;");        					
	    					System.out.println("Ongoing PROMO detected: PriceDrop");
	    				}	     
	            		if(isThereNewProduct&&!previousPromos.containsKey(key)) { //add "isThereNewProduct&&!previousPromos.containsKey(key)"
	    					newProducts.add("dealer: "+dealer+", product: "+brand + " " + name + " - NEW PRODUCT;");        					
	    					System.out.println("New PRODUCT detected");
	    				}	  	            		
					}					
					archive.add(product);
					
            		
				}
			}
			System.out.println("..1-2 seconds sleep");
			Thread.sleep(1000);
		}
		System.out.println("Writing the report..");
		try{
			gmanager.addSheet(Credentials.reportSpreadsheetId, getDate());
		} catch(Exception e) {
			logger.log(Level.WARNING, String.format("A report from this date: %s already exsists!", getDate()), e);	        
	        e.printStackTrace();
	    }				
		gmanager.writeSheet(Credentials.reportSpreadsheetId, String.format("%s!A1:P9999", getDate()), report);
		
		//WRITE ADDITIONAL REPORT "TSETSO" TABLE
		
		try{
			gmanager.addSheet(Credentials.additionalReportSpreadsheetId, "product positions");
		} catch(Exception e) {
			logger.log(Level.WARNING, "A report at the additional table already exsists!", e);	        
	        e.printStackTrace();
	    }
				
		gmanager.writeSheet(Credentials.additionalReportSpreadsheetId, "product positions!A1:P9999", reportAdditional);
		
		//send emails ANTELOPE TRACKER
		if(!promos.isEmpty()||!newProducts.isEmpty()){ 
    		try{
    			String subject = "Antelope Tracker PROMOs Alert";
				String emailText = mailer.Email.buildTextBody(promos, newProducts);
				String[] recipients = Credentials.promoRecipients;
				mailer.Email.send(subject, emailText, recipients);
    		}catch(Exception e){
				logger.log(Level.SEVERE, "An exception occurred. Can send email..", e);
				e.printStackTrace();	            				
			}
			
		}
		if(!notAvailable.isEmpty()){ 
    		try{
    			String subject = "Antelope Tracker Product Availability Alert";
				String emailText = mailer.Email.buildTextBodyAvailability(notAvailable);
				String[] recipients = Credentials.recipients;				
				mailer.Email.send(subject, emailText, recipients);
    		}catch(Exception e){
				logger.log(Level.SEVERE, "An exception occurred. Can send email..", e);
				e.printStackTrace();	            				
			}
			
		}
		
		TextJson.listToFile(archive);
		report.remove(0);
		writeSummaryReport(gmanager,report);	
	}

}
