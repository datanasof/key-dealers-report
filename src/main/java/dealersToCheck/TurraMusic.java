package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TurraMusic extends CheckDealer {
	static Document connect(String url) {
	    Document doc = null;
	    if(!url.equals("--")){
	    
		    try {
		    	Response response= Jsoup.connect(url)
		    	           .ignoreContentType(true)
		    	           .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")  
		    	           .referrer("http://www.google.com")   
		    	           .timeout(25000) 
		    	           .followRedirects(true)
		    	           .execute();
	
		    	doc = response.parse();
		    } catch (NullPointerException e) {
		    	logger.log(Level.SEVERE, "An exception occurred.", e);
		    } catch (HttpStatusException e) {
		    	logger.log(Level.SEVERE, "An exception occurred. Can not reach dealers page - "+url, e);
		    } catch (IOException e) {
		    	logger.log(Level.SEVERE, "An exception occurred.", e);
		    }
	    }
	    return doc;
	}	
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements devices = doc.select("div[class=products wrapper grid columns4 flex-grid products-grid]");
		Elements boxes = devices.select("div[class=product-item-info]");
				
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
	    		String price1 = boxes.get(i).select("span[class=price]").text();;
				String price2 = boxes.get(i).select("div[class=product attribute rrp]>span").text();
				if(price1.isEmpty()) {
					if(!price2.isEmpty()) price2 = price2.substring(6);					
					price1=price2;
				}
				perProduct.add(price1);	   
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
