package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LongMcQuade extends CheckDealer{
	
	public static String getAvailability(String url){        
		String result="";		
		try{
			Document doc = connect(url); 
			result = doc.select("span[class=label label-success]").text();
		}catch(Exception e){
			result = "NO ranking found";
		}		
		return result;
	}
	
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements devices = doc.select("div[class=row products-row m-container]");
		Elements boxes = devices.select("div[class=products-item-descr]");
				
		for (int i = 0; i<boxes.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);	 
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		//String deviceUrl = boxes.get(i).select("a").first().attr("href");	
	    		String brand = boxes.get(i).select("h5").text();
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String devname1 = boxes.get(i).toString();		
				String[] a = devname1.split("</h5>");
				String[] b = a[1].split("<br>");
	    		String device = b[0];
	    		perProduct.add(device.replace("\n", " "));
	    		
	    		String nReviewsPerDevice = boxes.get(i).select("a").first().text();
	    		if(!nReviewsPerDevice.isEmpty()){ 
	    			String nReviews=parseNumber(nReviewsPerDevice);
	    			if(!nReviews.equals("0")) {
	    				perProduct.add(nReviews);
	    			} else perProduct.add("");
	    		} else {
	    			perProduct.add("");
	    		}
	    		
	    		/**if(Credentials.brandsForAdditioanalCheck.contains(brand.toLowerCase())){
	    			
	    			System.out.println(String.format("checking Availabilityg of %s at %s", device,dealer));
	    			perProduct.add(getAvailability(deviceUrl));
	    			Thread.sleep(500);
	    		} else{
	    			perProduct.add(" ");
	    		}**/
				
	    		String available = "";//availability.get(i).text();
	    		if(!available.equals(null)) perProduct.add(available);
	    		String getPrice = boxes.get(i).select("p[class=products-item-price]").text().split(".00")[0];	
	    		double price = 0;
	    		try{
	    			price = priceEditor(getPrice);
	    		}catch(NumberFormatException e){
	    			System.out.println("Can not parse price for "+ device);
	    		}finally{
	    			perProduct.add(price);	    
	    		}	      				
	    		//String devRating = boxes.get(i).select("div[class*=rateStars star]").attr("class");
	    		String rating = "";
	    		//if(!devRating.isEmpty()) rating = devRating.substring(devRating.length()-1)+"/5";
	    		perProduct.add(rating);
	    		String categoryRank = "";	    		
	    		//if(!categoryRank.equals(null)) perProduct.add(categoryRank);
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
