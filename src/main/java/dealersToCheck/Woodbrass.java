package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Woodbrass extends CheckDealer {
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements devices = doc.select("div[class=row pt30 gallery bgBlanc]");
		Elements boxes = devices.select("div[class=col-6 col-lg-6 col-sm-12 col-xs-12]");
		
				
		for (int i = 0; i<boxes.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);	    		
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		//String deviceUrl = boxes.get(i).select("a").attr("href");
	    		
	    		String brand = boxes.get(i).select("div[class=fwb tup lh1-0 lh2-1-sm fs32-sm]").text();
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String device = boxes.get(i).select("div[class=prod-name fs26-sm lh1-0 lh1-8-sm mt5-sm fw500-sm]").text();
	    		if(!device.equals(null)) perProduct.add(device);
	    		String nReviewsPerDevice = "";	
	    		perProduct.add(nReviewsPerDevice);
	    		
	    		String available = boxes.get(i).select("div[class=fs12 fs24-sm flexAlignEnd]").first().select("span").attr("class");		
	    		if(available.equals("bgVert stock-clr")) {
	    			perProduct.add("In Stock");
	    		}else if(available.equals("bgOrange stock-clr")){ 
	    			perProduct.add("Coming Soon");
	    		}else if(available.equals("bgRouge stock-clr")){ 
	    			perProduct.add("Upon Request");
	    		} else perProduct.add("");
	    		
	    		String getPrice = boxes.get(i).select("div[class=fs26 fs42-sm pt10 pt0-sm clrBleu tal fwb]").text();	
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
		Thread.sleep(500);
		return report;		
	}	


}
