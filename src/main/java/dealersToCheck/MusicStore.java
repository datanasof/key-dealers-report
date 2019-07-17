package dealersToCheck;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MusicStore extends CheckDealer{
	
	public static List<String> getRatingReview(String url){ 
		List<String> ratingReview = new ArrayList<String>();
		Document doc = connect(url);
		Element meta = doc.select("meta[itemprop=ratingValue]").first();
		String content = meta.attr("content");
		ratingReview.add(content);
		
		meta = doc.select("meta[itemprop=reviewCount]").first();
		content = meta.attr("content");
		ratingReview.add(content);
		
		String available = doc.select("span[class*=padlr0-xsl]").text();
		System.out.println(available);
		if(available.length()>=8 && "in stock".equals(available.substring(0,8))){
			ratingReview.add("In Stock");
		} else ratingReview.add("Not Available");
		
		return ratingReview;
	}
	
	private static JSONObject jsonProduct(String text){
		JSONParser parser = new JSONParser();
		JSONObject json = null;
    	try {
			json = (JSONObject) parser.parse(text);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
    	return json;
	}
	
	private static String getBasicPageURL(Document doc){ 		
		Element input = doc.select("input[id=RedirectURL]").first();
		String s = input.attr("value");
		try {
			URL url = new URL(s);
			String host = url.getHost();
			String path = url.getPath();
			String[] newpath = path.split("/");
			s = host+"/"+newpath[1]+"/"+newpath[2]+"/";
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, "An exception occurred. Can not fetch BasicPage URL..", e);
			e.printStackTrace();
		}			
		return s;
	}
	
	private static String addURLpath(String brand, String name, String number){
		name = name.replace(" ", "-");		
		return brand+"-"+name+"/"+"art-"+number;		
	}
	
	private static String getSubstring(String text){
		
		int start = text.indexOf("'impressions': [")+18;
		int mid = text.indexOf("});");
		int end = text.indexOf("});",mid+1)-5;
		//System.out.println(start +","+end);
		return text.substring(start, end);		
				
	}
	
			
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements scriptElements = doc.getElementsByTag("script");
		for (Element element :scriptElements ){ 
	        for (DataNode node : element.dataNodes()) {
	            if(node.getWholeData().contains("'impressions': [")){
	            	String productRating = node.getWholeData();
	            	//System.out.println(productRating );
	            	//System.out.println(MusicStore.getSubstring(productRating) );
	            	
	            	String data = getSubstring(productRating);
	            	data = data.replace("'", "\"");
	            	//System.out.println(data);
	            	List<String> myList = new ArrayList<String>(Arrays.asList(data.split("\n,")));
	            	for(String s:myList){
	            		List<Object> perProduct = new ArrayList<Object>();
	            		JSONObject p = jsonProduct(s);	            		            		
	            		System.out.println(Credentials.readproductStatus);
	            		perProduct.add(dealer);
	            		perProduct.add(category);
	            		Integer currentPosition = ((Long) p.get("position")).intValue();;
	            		perProduct.add(currentPosition);
	            		perProduct.add(pageNumber);
	            		String b = (String) p.get("brand");
	            		String brand = b.replace(" ", "-");
	            		perProduct.add(b);
	            		String n = (String) p.get("name");
	            		String name = n.replace(";", "");
	            		name = name.replace("+ ", "");
	            		
	            		perProduct.add(n);
	            		String number = (String) p.get("id");
	            		String rating = " ";
	            		String reviews = " ";
	            		String available = " ";
	            		if(Credentials.brandsForAdditioanalCheck.contains(b.toLowerCase())){
	            			try{

		    	    			String deviceURL = "https://"+getBasicPageURL(doc)+addURLpath(brand, name, number);
		    	    			List<String> ratingReviews = getRatingReview(deviceURL);
		    	    			rating = ratingReviews.get(0)+"/5";
		    	    			reviews = ratingReviews.get(1);	
		    	    			available = ratingReviews.get(2);
		    	    			System.out.println(deviceURL);
		    	    			Thread.sleep(500);
	            			} catch(Exception e){
	            				logger.log(Level.SEVERE, "An exception occurred. Can not reach product page..", e);
	            				e.printStackTrace();	            				
	            			}
	    	    		} 
	            		
	            		perProduct.add(reviews);
	            		perProduct.add(available);
	            		perProduct.add(p.get("price"));
	            		perProduct.add(rating);
	            		perProduct.add(" ");	
	            		
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
	            	}	          		            	
	            	return report;
	            }
	        }		        
		 }
		return null;
	}		

}
