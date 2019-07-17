package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import jsonToText.TextJson;

public class Andertons extends CheckDealer{
	
	private static String getAvailability(String deviceURL){
		Document doc = connect(deviceURL);
		String available = "";
		available = doc.select("span[itemprop=\"availability\"]").text();		
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
		Elements devices = doc.select("div[class=\"catalog-entry-list\"]");
		Elements brandNames = devices.select("div[class=amc-product__name]");
		Elements prices = devices.select("span[class=product-price]");		
		Elements nReviews = doc.select("div[class=amc-product__rating]>div[class=amc-product__rating]");
		Elements tags = doc.select("div[class=\"amc-product__inner\"]");
		for (int i = 0; i<brandNames.size(); i++){
			
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
    		
    		String nReviewsPerDevice = "";
    		try{		
    			nReviewsPerDevice = nReviews.get(i).text().replace("(", "").replace(")", "");
    		}catch(Exception e){
				logger.log(Level.WARNING, "An exception occurred. Can not parce reviews tag..", e);
				e.printStackTrace();	            				
			}
    		perProduct.add(nReviewsPerDevice);
    		String available = "";
    		String deviceURL = brandNames.get(i).select("a").attr("href");
    		
    		if(Credentials.brandsForAdditioanalCheck.contains(brand.toLowerCase())){
    			
    			System.out.println("(ANDERTONS) checking AVAILABILITY for " + device);
    			available = getAvailability(deviceURL);
    			Thread.sleep(1000);
    		} 
    		perProduct.add(available);
    		double price = priceEditor(prices.get(i).text());
    		perProduct.add(price);	    		
    		String devRating = nReviews.get(i).select("span[class=feefo-stars feefo-stars--yellow]").attr("style");
    		String rating = "";
    		if(!devRating.isEmpty()) rating=devRating.replaceAll("[\\s+a-zA-Z : ;]","")+"/5";
    		if(!rating.equals(null)) perProduct.add(rating);
    		
    		Boolean isPromoAvailable = tags.get(i).select("div[class=amc-product__ribbon]>img").attr("ix-src").contains("NEW_LOW_PRICE");
    		String promoTag = "";
    		if(isPromoAvailable) promoTag =  "PRICE DROP"; 
    		perProduct.add(promoTag);
		
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
    		int currentAvail = 0;
    		int lastAvail = getLastAvailable(oldReport,dealer+brand+device);
    		try{
				currentAvail = Integer.parseInt(available);
			} catch(Exception e){
				logger.log(Level.WARNING, String.format("An exception occurred. Can not parse current availability for %s!", device), e);
				e.printStackTrace();	            				
			}				
    		perProduct.add(howMuchSold(lastAvail, currentAvail));
    				
    		report.add(perProduct);
				
			
		}
		return report;		
	}	
	

}
