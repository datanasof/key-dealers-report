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

public class Miroc extends CheckDealer {
	
	public static Document connect(String url) {
	    Document doc = null;
	    if(!url.equals("--")){
	    
		    try {
		    	Response response= Jsoup.connect(url)
		    	           .ignoreContentType(true)
		    	           .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")  
		    	           .referrer("http://www.google.com")   
		    	           .timeout(30000) 
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
		Document doc = connect(url);//Jsoup.parse(HTMLreader.runme(url));
		Elements content = doc.select("ul[id=SearchResultItems");
		Elements brands = doc.select("span[class=ItemMaker]"); //.substring(5);
		Elements devices = content.select("em"); //.substring(4);
		Elements prices = doc.select("span[class=ItemPrice]");//.substring(2);
		
		
		for (int i = 0; i<devices.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);

				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		String brand = brands.get(i).text().substring(5);
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String device = devices.get(i).text().substring(4);
	    		if(!device.equals(null)) perProduct.add(device);
	    		String nReviewsPerDevice = "";//nReviews.get(i).text();
	    		if(!nReviewsPerDevice.equals(null)) perProduct.add(nReviewsPerDevice);
	    		String available = "";//availability.get(i).text();
	    		if(!available.equals(null)) perProduct.add(available);
	    		String price = prices.get(i).text().substring(2);
	    		if(!price.equals(null)) perProduct.add(price);
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
