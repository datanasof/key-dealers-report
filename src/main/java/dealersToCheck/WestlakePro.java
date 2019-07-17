package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WestlakePro extends CheckDealer {
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = WestlakePro.connect(url);
		Elements devices = doc.select("ul[class=products columns-4]>li[class]");
		Logger logger = Logger.getAnonymousLogger();
		
		for (int i = 0; i<devices.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);	    		
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		//String deviceUrl = boxes.get(i).select("a").attr("href");
	    		List<String> brandName = splitBrandName(devices.get(i).select("h2[class=woocommerce-loop-product__title]").text());	
	    		String brand = brandName.get(0);
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String device = brandName.get(1);
	    		if(!device.equals(null)) perProduct.add(device);
	    		String nReviewsPerDevice = "";	
	    		perProduct.add(nReviewsPerDevice);
	    		String available = "";		
	    		perProduct.add(available);
	    		String price="";
	    		try{
	    			String[] getPrice=devices.get(i).select("span[class=woocommerce-Price-amount amount]").text().split(" ");
					price=getPrice[getPrice.length-1].replace(",", "");
	    		} catch(Exception e){
	    			logger.log(Level.WARNING, String.format("Can NOT parse a price at %s", url), e);	    	        
	    		}	    		
	    		perProduct.add(price);	    		
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
