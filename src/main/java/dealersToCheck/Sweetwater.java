package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Sweetwater extends CheckDealer{
	
	static Document connect(String url) {
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
	
	@SuppressWarnings("resource")
	private static int parseInt(String value) {
	    try {
	    	Scanner in = new Scanner(value).useDelimiter("[^0-9]+");
	    	int result = in.nextInt();
	    	in.close();
	    	return result;
	    } catch (NumberFormatException nfe) {
	    	return 0;
	    }
	}
	
	@SuppressWarnings("resource")
	private static double parseDouble(String value) {
	    try {
	    	Scanner in = new Scanner(value).useDelimiter("[^0-9]+");
	    	double result = in.nextDouble();
	    	in.close();
	    	return result;
	    } catch (NumberFormatException nfe) {
	    	return 0;
	    }
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
	
	private static List<String> getSubstring(List<String> baseText){
		List<String> text = baseText;
		
		if(!text.get(0).contains("ec:addImpression")){
			return text;
		} else {
			int start = text.get(0).indexOf("ec:addImpression")+18;
			int end = text.get(0).indexOf("});")+1;
			text.add(text.get(0).substring(start, end).replace('\'', '"'));
			text.set(0, text.get(0).substring(end+1)); 
			return getSubstring(text);		
		}		
	}	
	
	private static int getNumberOfReviews(Document doc, String reviewsNumberTag){
		try{
			String nReviews = doc.select(reviewsNumberTag).first().text();
			return parseInt(nReviews);
		} catch (NullPointerException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		return 0;		
	}
	
	private static double getRating(Document doc, String ratingTag){		
		try{
			String rating = doc.select(ratingTag).first().attr("data-rating");
			return parseDouble(rating);
		} catch (NullPointerException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		return 0;				
	}	
			
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		System.out.println("Sleeping at Sweetwater..");
		Thread.sleep(1000);
		
		//GET PREVIOUS PROMOS LIST (MAP)
		//Map <String, Integer> previousPromos = TextJson.getPromosPerProduct();
		
		//
		
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements scriptElements = doc.getElementsByTag("script");
		//List <String> promos = new ArrayList<String>();
		//List <String> newProducts = new ArrayList<String>();
		
		for (Element element :scriptElements ){ 
	        for (DataNode node : element.dataNodes()) {
	            if(node.getWholeData().contains("function(i,s,o,g,r,a,m)")){
	            	String productRating = node.getWholeData();
	            	
	            	List<String> data = new ArrayList<String>();
	            	data.add(productRating);
	            	data = getSubstring(data);
	            		            	
	            	Elements devices = doc.select("div[class=grid prodgrid productGridList]");
	        		Elements boxes = devices.select("div[class=product-card]");
	        		
	            	for(int i=1; i<data.size(); i++){
	            		List<Object> perProduct = new ArrayList<Object>();
	            		//String deviceURL = "--";
	            		boolean promoTag = false;
	            		boolean promoTag2 = false;
	            		boolean newTag = false;
	            		boolean newTag2 = false;
	            		try{
	            			Element e = boxes.get(i-1);
	            			//deviceURL = "https://www.sweetwater.com"+e.select("a").attr("href");//
	            			promoTag = e.select("img").attr("data-ll-src").contains("b-pricedrop"); //TAG FOR PROMOTION
	            			promoTag2 = e.select("img").attr("src").contains("b-pricedrop"); 		//TAG FOR PROMOTION
	            			newTag = e.select("img").attr("src").contains("b-new");					//TAG FOR NEW PRODUCT	            			
	            			newTag2 = e.select("img").attr("data-ll-src").contains("b-new"); 		//TAG FOR NEW PRODUCT 2	 
	            			
	            		} catch(Exception e){
            				logger.log(Level.SEVERE, "An exception occurred. Can not fetch product url..", e);
            				e.printStackTrace();	            				
            			}
	            		
	            		JSONObject p = jsonProduct(data.get(i));	//
	            		String reviewsNumberTag = String.format("a[aria-label=\"See reviews for %s\"] > span[class=\"rating__count\"]", p.get("name"));
	            		String ratingTag = String.format("a[aria-label=\"See reviews for %s\"] > span[data-rating]", p.get("name"));
	            		System.out.println(Credentials.readproductStatus);
	            		perProduct.add(dealer);
	            		perProduct.add(category);
	            		//perProduct.add(startingPosition+((Long)p.get("position")).intValue());
	            		//perProduct.add(startingPosition + Integer.parseInt(String.valueOf( p.get("position"))));
	            		int currentPosition = startingPosition + i;//
	            		perProduct.add(currentPosition);
	            		perProduct.add(pageNumber);
	            		//String id = (String) p.get("id");
	            		String brand = (String) p.get("brand");
	            		perProduct.add(brand);
	            		String name = (String) p.get("name");
	            		perProduct.add(name);	            		
	            		perProduct.add(getNumberOfReviews(doc, reviewsNumberTag));
	            		String available = "";
	            		perProduct.add(available);
	            		String price = "";
	            		perProduct.add(price);
	            		perProduct.add(getRating(doc, ratingTag));
	            		
	            		//KEY TO SEARCH IN THE PREVIOUS PROMOS LIST (MAP)
	            		//String key = dealer+brand+name;
	            		//
	            		boolean isTherePromo = promoTag||promoTag2;
	            		boolean isThereNewProduct = newTag||newTag2;
	            		
	            		/**if(isTherePromo&&!previousPromos.containsKey(key)) { //add "promoTag&&!previousPromos.containsKey(key)"
        					promos.add(brand + " " + name + " - PRICE DROP;");        					
        					System.out.println("Ongoing PROMO detected: PriceDrop");
        				}	     
	            		if(isThereNewProduct&&!previousPromos.containsKey(key)) { //add "isThereNewProduct&&!previousPromos.containsKey(key)"
        					newProducts.add(brand + " " + name + " - NEW PRODUCT;");        					
        					System.out.println("New PRODUCT detected");
        				}	**/  	            		
	            		
	            		String categoryRank = "";
	            		if(isTherePromo) {
	            			categoryRank = "PRICE DROP";
	            		} else if(isThereNewProduct){
	            			categoryRank = "NEW PRODUCT/BUNDLE";
	            		}
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
	            	}	    
	            	
	            	/**if(!promos.isEmpty()||!newProducts.isEmpty()){ //send emails
	            		try{
	            			String subject = "Antelope Tracker PROMOs Alert";
		    				String emailText = mailer.Email.buildTextBody(promos, newProducts, dealer);
		    				String[] recipients = Credentials.recipients;
		    				mailer.Email.send(subject, emailText, recipients);
	            		}catch(Exception e){
            				logger.log(Level.SEVERE, "An exception occurred. Can send email..", e);
            				e.printStackTrace();	            				
            			}
	    				
	    			}**/
	            	return report;
	            }
	        }		        
		 }
		return null;
	}		

}
