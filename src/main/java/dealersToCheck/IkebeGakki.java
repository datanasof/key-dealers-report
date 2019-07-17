package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IkebeGakki extends CheckDealer {
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
		Elements devices = doc.select("div[class=c5]");
		Elements boxes = devices.select("p[class=item]");
		Elements prices = devices.select("p[class=price]>span[class=price]");
		
		for (int i = 0; i<boxes.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		
	    		Element prinfo = boxes.get(i).select("a").first();
	    		
	    		List<String> brandName = splitBrandName(prinfo.text());
	    		String brand = brandName.get(0);
	    		if(!brand.isEmpty()) {
	    			perProduct.add(brand);
	    		} else perProduct.add("");
	    		String dev = brandName.get(1);
	    		if(!dev.isEmpty()){
		    		try{
		    			String device = dev.substring(0, dev.indexOf("【"));
		    			perProduct.add(device);
					}catch(Exception e){
						logger.log(Level.INFO, "An exception occurred. Can not parse device name - "+dev, e);
						perProduct.add(dev);	  
					}	
		    		  	
	    		}else perProduct.add("");	    	
	    			    		
	    		String nReviewsPerDevice = "";//nReviews.get(i).text();
	    		if(!nReviewsPerDevice.equals(null)) perProduct.add(nReviewsPerDevice);
	    		String available = "";//availability.get(i).text();
	    		if(!available.equals(null)) perProduct.add(available);
	    		String price = prices.get(i).text();	    		
				try{
					price = price.substring(0, price.indexOf(",")+4);
				}catch(Exception e){
					logger.log(Level.INFO, "An exception occurred. Can not parse price - "+price, e);
				}	    		
	    		if(!price.equals(null)) {
	    			perProduct.add(price);
	    		} else perProduct.add("");
	    		String rating = "";//valueOfReviews.get(i+5).attr("style");
	    		if(!rating.equals(null)) perProduct.add(rating);
	    		String categoryRank = "";
	    		
	    		if(!categoryRank.equals(null)) perProduct.add(categoryRank);
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
