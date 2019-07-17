package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import jsonToText.TextJson;

public class Gear4music extends CheckDealer {
	
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
		Elements devices = doc.select("ul[class=result-list instant-search-result-list]");
		Elements boxes = devices.select("li[class=restricted-inv-notice-row]");
				
		for (int i = 0; i<boxes.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);	    		
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		
	    		List<String> brandName = splitBrandName(boxes.get(i).select("div[class=description-text-wrap]>h3").text());	
	    		String brand = brandName.get(0);
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String device = brandName.get(1);
	    		if(!device.equals(null)) perProduct.add(device);
	    			
	    		String nReviews="";
	    		if(!nReviews.isEmpty()) {
	    			perProduct.add(nReviews);
	    		} else perProduct.add("");
	    		String available = parseNumber(boxes.get(i).select("p[class=instock in-stock]").text());		
	    		if(!available.isEmpty()) {
	    			perProduct.add(available);
	    		} else perProduct.add("");	    		
	    		String getPrice = boxes.get(i).select("span[class=price no-rrp]>span").first().text();	
	    		double price=0;
	    		if(!getPrice.isEmpty()) price = priceEditor(getPrice);
	    		perProduct.add(price);	    		
	    		//String devRating = boxes.get(i).select("div[class*=rateStars star]").attr("class");
	    		String rating = "";
	    		//if(!devRating.isEmpty()) rating = devRating.substring(devRating.length()-1)+"/5";
	    		perProduct.add(rating);
	    		String categoryRank = "";	    		
	    		//if(!categoryRank.equals(null)) perProduct.add(categoryRank);
	    		perProduct.add(categoryRank);
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
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return report;		
	}	


}
