package dealersToCheck;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Thomann  extends CheckDealer{
	
	public static String getRating(String url){        
		String result;
		List<String> values = new ArrayList<String>();
		try{
			Document doc = connect(url); 
			Elements myin = doc.select("div.ranking");
			Pattern p = Pattern.compile("-?\\d+");
			Matcher m = p.matcher(myin.text());
			while (m.find()) {
				values.add(m.group());
			}
			//result = ("Category ranking: " + values.get(0) + ", Website ranking: " + values.get(1));
			result = values.get(0);
		} catch(Exception e){
			result = "NO ranking found";
		}		
		return result;
	}
	
	static String parseNumber(String s){		
		float value = Float.valueOf(s.replaceAll(".*?([\\d.]+).*", "$1"));
		float finalValue = (value/100)*5;
		
		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.CEILING);
		
		String result = df.format(finalValue) + " / 5";
		return result;		
	}
		
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements devices = doc.select("span[class=\"model\"]");
		Elements brands = doc.select("span[class=\"manufacturer\"]");
		Elements nReviews = doc.select("span[class=\"count\"]");
		Elements availability = doc.select("span[class=\"rs-layover-trigger-text\"]");
		Elements prices = doc.select("span[class=\"article-basketlink\"]");
		Elements valueOfReviews = doc.select("span[class=\"overlay-wrapper\"]");
		Elements urlPerDevice = doc.select("div[class=\"product-image image-block\"] > a");
		for (int i = 0; i<devices.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		String brand = brands.get(i).text();
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String device = devices.get(i).text();
	    		if(!device.equals(null)) perProduct.add(device);
	    		String nReviewsPerDevice = nReviews.get(i).text();
	    		if(!nReviewsPerDevice.equals(null)) perProduct.add(nReviewsPerDevice);
	    		String available = availability.get(i).text();
	    		if("Sofort lieferbar".equals(available)) available = "In Stock";
	    		if(available.length()>3) perProduct.add(available);
	    		else perProduct.add("");
	    		String price = prices.get(i).text();
	    		if(!price.equals(null)) perProduct.add(price);
	    		String rating = valueOfReviews.get(i+5).attr("style");
	    		if(!rating.equals(null)) perProduct.add(parseNumber(rating));
	    		
	    		if(Credentials.brandsForAdditioanalCheck.contains(brand.toLowerCase())){
	    			String deviceURL = urlPerDevice.get(i).attr("href");
	    			System.out.println("checking Sales Rating of " + device);
	    			perProduct.add(getRating(deviceURL));
	    			Thread.sleep(500);
	    		} else{
	    			perProduct.add(" ");
	    		}
    		
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
				//System.out.println((i+1)+". "+"brand: "+brands.get(i).text()+", device: "+devices.get(i).text()+", reviews: "+ nReviews.get(i).text());
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return report;		
	}	

}
