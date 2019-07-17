package dealersToCheck;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class CheckDealer {
	static Logger logger = Logger.getAnonymousLogger();
	
	static Document connect(String url) {
	    Document doc = null;
	    if(!url.equals("--")){
	    
		    try {
		    	Response response= Jsoup.connect(url)
		    	           .ignoreContentType(true)
		    	           .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")  
		    	           .referrer("http://www.google.com")   
		    	           .timeout(10000) 
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
	
	static Document connectProxy(String url) {
	    Document doc = null;
	    if(!url.equals("--")){
	    		    
		    try {
		    	//System.setProperty("http.proxyHost", "104.248.51.135");
		    	//System.setProperty("http.proxyPort", "8080");
		    	Response response= Jsoup.connect(url)	
		    			.proxy("104.248.51.135", 8080)
		    	           .ignoreContentType(true)		    	           
		    	           .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")  
		    	           .header("Content-Language", "en-US")
		    	           .referrer("http://www.google.com")   
		    	           .timeout(10000) 
		    	           .followRedirects(true)
		    	           .execute();
	
		    	doc = response.parse();
		    } catch (NullPointerException e) {
		    	logger.log(Level.SEVERE, "An exception occurred.", e);
		    } catch (HttpStatusException e) {
		    	logger.log(Level.SEVERE, "An exception occurred. Can not reach dealers page - "+url, e);
		    } catch (IOException e) {
		    	logger.log(Level.SEVERE, "An exception occurred.", e);
		    } /*finally{
		    	try {
			    	System.clearProperty("http.proxyHost");
			    	System.clearProperty("http.proxyPort");
		    	} catch (Exception e) {
			    	logger.log(Level.SEVERE, "An exception occurred.", e);
			    }
		    }*/
	    }
	    return doc;
	}	
		
	static double calculateShelfPercentageOfPosition(int pos, int allpos) {
		double points = (100-pos*100.0/allpos)*2/allpos;	
		
		if(points>0) {
			return round(points,2);	
		} else return 0;
	}
	
	static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	static double calcRelativePosition(int pos, int allpos) {
		int relativePosition = pos*100/allpos;
		if (relativePosition>100) return 100;
		return relativePosition;
	}
	
	
	static String parseNumber(String forParsing){
		try{
			@SuppressWarnings("resource")
			Scanner in = new Scanner(forParsing).useDelimiter("[^0-9]+");
			String number = in.next();
			in.close();
			return number;
		}catch(Exception e){			
			logger.log(Level.INFO, "An exception occurred. Can not parse String="+forParsing, e);
			return "";
		}
		
	}
	
	static List<String> splitBrandName(String name){
		List<String> brandName = new ArrayList<String>();
		Iterator<Entry<String, Integer>> it = Credentials.brands.entrySet().iterator();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
	        String brand = ((String) pair.getKey());       
	        int i = (int) pair.getValue();
	        	        
    		if(name.toLowerCase().contains(brand.toLowerCase())){
    			brandName.add(brand);
    			brandName.add(name.substring(i));
    			break;
    		}
	    }
	    if(brandName.size()==0){	    	
			brandName.add(" ");
			brandName.add(name);    		
	    }
		return brandName;		
	}
	
	static double priceEditor(String price){		
		double newPrice1 = priceEditorDotThousandSeparator(price);
		double newPrice2 = priceEditorCommaThousandSeparator(price);
		if(newPrice1 > newPrice2 && newPrice1 < 10000) {
			return newPrice1;
		}
		else if(newPrice2 < 10000){
			return newPrice2;
		}
		else return newPrice1;
	}
	
	private static double priceEditorDotThousandSeparator(String price){
		String parsed = price.replaceAll("[^\\d,]+", "").replace(",", ".");
		double newPrice = Double.parseDouble(parsed);
		return newPrice;
	}
	
	private static double priceEditorCommaThousandSeparator(String price){
		String parsed = price.replaceAll("[^\\d.]+", "");
		double newPrice = Double.parseDouble(parsed);
		return newPrice;
	}

}
