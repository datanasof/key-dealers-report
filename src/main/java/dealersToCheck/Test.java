package dealersToCheck;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jsonToText.TextJson;



public class Test {
	
	public static void main(String[] args) throws MalformedURLException {
		Logger logger = Logger.getAnonymousLogger();
		List<List<Object>> reportPerURL = new ArrayList<List<Object>>();
		String dealer = "VintageKing";
		String category = "TH";
		String pageNumber = "1";
		String pageURL = "https://vintageking.com/recording/computer-audio/interfaces/thunderbolt-interfaces";
		String pageURL2 = "https://vintageking.com/recording/computer-audio/interfaces/thunderbolt-interfaces?p=2";
		int startingPosition = 1;
		int nPosPerCat = 89;
		String country = "USA";
		
		try{
			reportPerURL = Splitter.getReportPerURL(dealer, category, pageNumber, pageURL2, startingPosition, nPosPerCat, country);
			int i=1;
			for(List<Object> dev:reportPerURL) {
				System.out.println(i+". "+dev);
				i++;
			}
		}catch(Exception e){
			logger.log(Level.WARNING, "An exception occurred. Can not reach dealers page - "+pageURL, e);
		}
		
		
		
		
	}
		
}
