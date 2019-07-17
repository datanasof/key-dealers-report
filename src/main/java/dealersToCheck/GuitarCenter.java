package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GuitarCenter extends CheckDealer {
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connectProxy(url);
		Elements devices = doc.select("div[id=leaf-products]");
		Elements boxes = devices.select("div[class=span-21]");
				
		for (int i = 0; i<boxes.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);	    		
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		//String deviceUrl = boxes.get(i).select("a").attr("href");
	    		List<String> brandName = splitBrandName(boxes.get(i).select("a").text());	
	    		String brand = brandName.get(0);
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String device = brandName.get(1);
	    		if(!device.equals(null)) perProduct.add(device);
	    		String nReviewsPerDevice = boxes.get(i).select("a>span").text();	
	    		String nReviews=parseNumber(nReviewsPerDevice);
	    		if(!nReviews.isEmpty()) {
	    			perProduct.add(nReviews);
	    		} else perProduct.add("");
	    		String available = boxes.get(i).select("div[class=span-6]").text();		
	    		if(!available.isEmpty()) {
	    			perProduct.add(available);
	    		} else perProduct.add("");
	    		
	    		String getPrice = boxes.get(i).select("span[class=span-6 price last clear]").text();	
	    		String price="";
	    		if(!getPrice.isEmpty()) price = getPrice;
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
	    		perProduct.add("");
    		
	    		report.add(perProduct);
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return report;		
	}	


}
