package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class StudioEconomik extends CheckDealer {
	
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements devices = doc.select("div[class=center_container w75]");
		Elements boxes = devices.select("div[class=cell tile_view_cell w25 brd bggrey center]");
				
		for (int i = 0; i<boxes.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);	    		
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		//String deviceUrl = "http://www.economik.com" + boxes.get(i).select("a").attr("href");
	    		
	    		List<String> brandName = splitBrandName(boxes.get(i).select("a").text());	
	    		String brand = brandName.get(0);
	    		if(!brand.isEmpty()) perProduct.add(brand);
	    		else perProduct.add("");
	    		String device = brandName.get(1);
	    		if(!device.isEmpty()) perProduct.add(device);
	    		else perProduct.add("");
	    		
	    		String nReviews="";
	    		String rating = "";
	    		/*if(Credentials.brandsForAdditioanalCheck.contains(brand.toLowerCase())){
	    			
	    			System.out.println("checking Sales Rating of " + device);
	    			try{
	    				Document doc2 = connect(deviceUrl); 
	    				rating = doc2.select("meta[itemprop=ratingValue]").attr("content")+"/5";
	    				nReviews = doc2.select("span[itemprop=reviewCount]").text();
	    				
	    			} catch(Exception e){
	    				logger.log(Level.SEVERE, "An exception occurred. Can not reach product page - "+deviceUrl, e);
	    			}			    			
	    			Thread.sleep(500);
	    		} */
	    		
	    		perProduct.add(nReviews);	    		
	    		String available = "";
	    		perProduct.add(available);
	    		String price1 = boxes.get(i).select("div[class*=price_bold").text();				
				perProduct.add(priceEditor(price1));	   
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
