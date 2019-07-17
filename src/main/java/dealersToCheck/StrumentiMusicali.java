package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class StrumentiMusicali extends CheckDealer {
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements devices = doc.select("tr[class=productListing-even]");
		
				
		for (int i = 0; i<devices.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);	    		
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		String deviceUrl = devices.get(i).select("a").attr("href");
	    		List<String> brandName = splitBrandName(devices.get(i).select("a>b").text());	
	    		String brand = brandName.get(0);
	    		if(!brand.isEmpty()) perProduct.add(brand);
	    		else perProduct.add("");
	    		String device = brandName.get(1);
	    		if(!device.isEmpty()) perProduct.add(device);
	    		else perProduct.add("");
	    		
	    		String nReviews="";
	    		String rating = "";
	    		if(Credentials.brandsForAdditioanalCheck.contains(brand.toLowerCase())){
	    			
	    			System.out.println("checking Sales Rating of " + device);
	    			try{
	    				Document doc2 = connect(deviceUrl); 
	    				rating = doc2.select("meta[itemprop=ratingValue]").attr("content")+"/5";
	    				nReviews = doc2.select("span[itemprop=reviewCount]").text();
	    				
	    			} catch(Exception e){
	    				logger.log(Level.SEVERE, "An exception occurred. Can not reach product page - "+deviceUrl, e);
	    			}			    			
	    			Thread.sleep(500);
	    		} 
	    		
	    		perProduct.add(nReviews);	    		
	    		String available = devices.get(i).select("div>font>b").text();
	    		if(available.equals("Ultimo disponibile")||available.equals("Disponibilità immediata")) {
	    			perProduct.add("In Stock");
	    		} else perProduct.add("Not available");
	    		String price1 = devices.get(i).select("td+td+td").select("strong").text();
				String price2 = devices.get(i).select("td+td+td").select("strong>span").text();
				if(price2.isEmpty()) price2=price1;
				perProduct.add(price2);	   
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
