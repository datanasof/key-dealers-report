package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class SoundHouse extends CheckDealer {
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = SoundHouse.connect(url);
		Elements devices = doc.select("ul[class=item_list gridtype]>li[class]");
		//Logger logger = Logger.getAnonymousLogger();
		
		for (int i = 0; i<devices.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);	    		
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		//String deviceUrl = boxes.get(i).select("a").attr("href");
	    		
	    		String brand = devices.get(i).select("ul[class=detail]>li[class=maker]").text();
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String device = devices.get(i).select("ul[class=detail]>li[class=prod_name]").text();
	    		if(!device.equals(null)) perProduct.add(device);
	    		String info = devices.get(i).select("div[class=other_detail]").text();	
	    		String nReviewsPerDevice = parseNumber(info);	
	    		perProduct.add(nReviewsPerDevice);
	    		String available = "Not available";	
	    		if(info.contains("お取寄せ")||info.contains("在庫あり")) available = "In Stock";
	    		perProduct.add(available);
	    		String price=devices.get(i).select("ul[class=detail]>li[class=price]").text().substring(4, 11).replaceAll("[^0-9]","");
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
