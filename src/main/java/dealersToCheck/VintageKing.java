package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class VintageKing extends CheckDealer{
	
	static String percentToNumber(String percent){
		if(!percent.isEmpty()){
			double result =  5 * (Double.parseDouble(percent)/100);
			return result+"";
		}
		return "";		
	}
		
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements devices = doc.select("li[class*=item]");
		//Elements boxesImage = devices.select("div[class=p-image]");
		//Elements boxesContent = devices.select("div[class=p-content]");
		//Elements boxesActions = devices.select("div[class=p-actions]");
			
		
		//Elements nReviews = devices.select("div[class=rating-links]");//text();		
		//Elements prices = devices.select("span[class=voor-prijs]");//(price.substring(0,price.length()-2));		
		//String nOfReviews = e.text().replace("(", "").replace(")", "");
		//Elements availability = doc.select("span[class=\"rs-layover-trigger-text\"]");
		//Elements urlPerDevice = devices.select("a"); //.attr("href");
		for (int i = 0; i<devices.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);	    		
	    		List<String> brandName = splitBrandName(devices.get(i).select("h2[class=product-name]").text());
	    		String brand = brandName.get(0);
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		else perProduct.add("");
	    		String device = brandName.get(1);
	    		if(!device.equals(null)) perProduct.add(device);
	    		else perProduct.add("");
	    		String nReviews = "";
	    		perProduct.add(nReviews);
	    		String available = "";
	    		perProduct.add(available);
	    		Double price = priceEditor(devices.get(i).select("span[id*=product-price]").text());
	    		if(!price.equals(null)) perProduct.add(price);
	    		else perProduct.add("");
	    		
	    		String devRating = "";
	    		perProduct.add(devRating);
	    		
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
