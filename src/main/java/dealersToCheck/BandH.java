package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BandH extends CheckDealer {
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = BandH.connect(url);
		Elements devBox = doc.select("div[class=items full-width list-view elevn c2]");
		Elements devices = devBox.select("div[data-selenium=itemDetail]");
		//Logger logger = Logger.getAnonymousLogger();
		
		for (int i = 0; i<devices.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);	    		
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		//String deviceUrl = devices.get(i).select("a").attr("href");	    		
	    		String brand = devices.get(i).select("span[itemprop=brand]").text();	
	    		perProduct.add(brand);
	    		String device = devices.get(i).select("span[itemprop=name]").text();	
	    		perProduct.add(device);
	    		String nReviewsPerDevice = "";	
	    		try{
	    			nReviewsPerDevice=parseNumber(devices.get(i).select("a").text().substring(1, 4));
					}catch(Exception e){
						
					}
	    		perProduct.add(nReviewsPerDevice);
	    		String available = devices.get(i).select("span[data-selenium=popoverContent]").text();		
	    		perProduct.add(available);
	    		String price=devices.get(i).select("span[data-selenium=price]").text();	    		
	    		perProduct.add(price.replace(",", ""));	    		
	    		//String devRating = boxes.get(i).select("div[class*=rateStars star]").attr("class");
	    		String rating = "";	    		
	    		perProduct.add(rating);
	    		String categoryRank = "";	    			    		
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
	    		perProduct.add("");
    		
	    		report.add(perProduct);
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return report;		
	}	


}
