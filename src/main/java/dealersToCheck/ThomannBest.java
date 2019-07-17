package dealersToCheck;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ThomannBest  extends CheckDealer{
		
	static String parseNumber(String s){
		String result = "";
		try{
		float value = Float.valueOf(s.replaceAll(".*?([\\d.]+).*", "$1"));
		float finalValue = (value/100)*5;
		
		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.CEILING);		
		result = df.format(finalValue) + " / 5";
			
		} catch(NumberFormatException e){
			e.printStackTrace();
		}
		return result;	
	}
		
	public static List<List<Object>> getReportPerURL(String dealer, String category, String pageNumber,String url, int startingPosition, int nPosPerCat, String country) throws IOException, InterruptedException{
		List<List<Object>> report = new ArrayList<List<Object>>();
		Document doc = connect(url);
		Elements devices = doc.select("div[class*=cell article]");
		
		for (int i = 0; i<devices.size(); i++){
			try{
				List<Object> perProduct = new ArrayList<Object>();
				perProduct.add(dealer);
				perProduct.add(category);
				int currentPosition = startingPosition + i + 1;
				perProduct.add(currentPosition);
	    		perProduct.add(pageNumber);
	    		
	    		List<String> brandName = splitBrandName(devices.get(i).select("div[class=title-block]").text());
	    		String brand = brandName.get(0);
	    		if(!brand.equals(null)) perProduct.add(brand);
	    		String device = brandName.get(1);
	    		if(!device.equals(null)) perProduct.add(device);	 	
	    		String nReviewsPerDevice = devices.get(i).select("span[class=count]").text();
	    		if(!nReviewsPerDevice.equals(null)) perProduct.add(nReviewsPerDevice);
	    		perProduct.add(" ");		
	    		String price = devices.get(i).select("span[class=article-basketlink]").text().replaceAll("[^0-9]","");
	    		if(!price.equals(null)) perProduct.add(price);
	    		String rating = devices.get(i).select("span[class=overlay-wrapper]").attr("style");	
	    		if(!rating.equals(null)) perProduct.add(parseNumber(rating));
	    		
	    		perProduct.add(" ");
    			perProduct.add("");
    			perProduct.add("");
    		
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
