package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import jsonToText.TextJson;

public class Bax extends CheckDealer{
	
	private static String getAvailability(String deviceURL){
		Document doc = connect(deviceURL);
		String available = "";
		available = doc.select("span[class=\"stock-content\"]").text();		
		return parseNumber(available);		
	}
	
	private static Integer getLastAvailable(List<List<String>> oldReport,String deviceInfo){
		int lastAvailable = 0;
		for(List<String> row:oldReport){
			String dealerBrandDevice = row.get(0)+row.get(4)+row.get(5);
			if(deviceInfo.equals(dealerBrandDevice)){
				try{
					lastAvailable = Integer.parseInt(row.get(7));
				} catch(Exception e){
					logger.log(Level.WARNING, String.format("An exception occurred. Can not parse last availability for %s!", dealerBrandDevice), e);
					e.printStackTrace();	            				
				}				
			}
		}		
		return lastAvailable;		
	}
	
	private static Integer howMuchSold(int lastAvailable, int available){
		int sold = lastAvailable - available;		
		if(sold>0) return sold;			
		return 0;
	}
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		List<List<String>> oldReport = TextJson.fileToList();
		
		Document doc = connect(url);
		Elements devices = doc.select("div[class=product-results grid-75 result-container bigimg]");
		Elements brandNames = devices.select("h2>a");//.text()
		Elements prices = devices.select("span[class=voor-prijs]");//(price.substring(0,price.length()-2));		
		Elements boxes = devices.select("div[class=result]");
		for (int i = 0; i<boxes.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);
				
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		
	    		perProduct.add(pageNumber);
	    		List<String> brandName = splitBrandName(brandNames.get(i).text());
	    		
	    		String brand = brandName.get(0);
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String device = brandName.get(1);
	    		if(!device.equals(null)) perProduct.add(device);
	    		String nReviewsPerDevice = boxes.get(i).select("div[class*=rateStars star]").text();
	    		String nReviews = "";
	    		if(!nReviewsPerDevice.isEmpty()) nReviews=nReviewsPerDevice.substring(0,1);
				perProduct.add(nReviews);
	    		String available = boxes.get(i).select("div[class=instockIconsWrapper full-stock-text]").text();
	    		int soldUnits = 0;
	    		
	    		if(available.contains("Op voorraad")) {
	    			if(Credentials.brandsForAdditioanalCheck.contains(brand.toLowerCase())){
		    			String deviceURL = brandNames.get(i).attr("href");
		    			System.out.println("checking for availability of " + device);
		    			available = getAvailability(deviceURL);
		    			Thread.sleep(500);
		    			
		    			int currentAvail = 0;
			    		int lastAvail = getLastAvailable(oldReport,dealer+brand+device);
			    		try{
							currentAvail = Integer.parseInt(available);
						} catch(Exception e){
							logger.log(Level.WARNING, String.format("An exception occurred. Can not parse current availability for %s!", device), e);
							e.printStackTrace();	            				
						}				
			    		soldUnits = howMuchSold(lastAvail, currentAvail);
		    			
		    		} else{
		    			available = "In Stock";
		    		}
	    			perProduct.add(available);
	    		}
	    		else perProduct.add("Not Available");
	    		
	    		String getPrice = prices.get(i).text();
	    		String price = "";
	    		if(!getPrice.isEmpty()) price = getPrice.substring(0,getPrice.length()-2);
	    		perProduct.add(price);	    		
	    		String devRating = boxes.get(i).select("div[class*=rateStars star]").attr("class");
	    		String rating = "";
	    		if(!devRating.isEmpty()) rating = devRating.substring(devRating.length()-1)+"/5";
	    		perProduct.add(rating);
	    		String categoryRank = "";
	    		
	    		if(!categoryRank.equals(null)) perProduct.add(categoryRank);
    		
	    		if(nPosPerCat!=0){
	    			double relativePosition = calcRelativePosition(currentPosition, nPosPerCat);
	    			double shelfPercentagePoints = calculateShelfPercentageOfPosition(currentPosition, nPosPerCat);
	    			perProduct.add(relativePosition);
	    			perProduct.add(shelfPercentagePoints);
	    		} else {
	    			perProduct.add("");
	    			perProduct.add("");
	    		}
	    		perProduct.add(country);
	    			    		
	    		perProduct.add(soldUnits);
	    		
	    		report.add(perProduct);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return report;		
	}	
	

}
