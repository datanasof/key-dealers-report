package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.google.api.services.sheets.v4.model.TextToColumnsRequest;

import jsonToText.DeviceList;
import jsonToText.HTMLreader;
import jsonToText.TextJson;
import main.GsheetsManager;

public class test2 {
	
		
	public static void main(String[] args) throws IOException, InterruptedException {
		String pageURL = "https://vintageking.com/recording/computer-audio/interfaces/thunderbolt-interfaces";
		Document doc = VintageKing.connect(pageURL);
		Elements devices = doc.select("li[class*=item]");
		Elements boxesImage = devices.select("div[class=p-image]");
		Elements boxesContent = devices.select("div[class=p-content]");
		Elements boxesActions = devices.select("div[class=p-actions]");
		int i = 1;
		for (Element e:devices){
			System.out.println(i);			
			List<String> brandName =  VintageKing.splitBrandName(e.select("h2[class=product-name]").text());
			//String brandName = e.select("h2[class=product-name]").text();
			String price = e.select("span[id*=product-price]").text();
			System.out.println(brandName);
			System.out.println(VintageKing.priceEditor(price));
			
			i++;
		}
		
		
	}
}
