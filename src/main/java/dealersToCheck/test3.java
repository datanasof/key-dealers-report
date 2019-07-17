package dealersToCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class test3 {
	private static String getAvailability(String deviceURL){
		Document doc = GuitarCenter.connectProxy(deviceURL);
		return doc.text();
	}
		
	public static void main(String[] args) throws IOException, InterruptedException {
		//String deviceURL = "https://www.guitarcenter.com/Antelope-Audio/Discrete-4-with-Basic-FX-Collection.gc";
		String pageURL2="https://store.miroc.co.jp/category/201";
		//String available = getAvailability(deviceURL);
		Document doc = Miroc.connect(pageURL2);
		Elements banners = doc.select("div[class=slick-track]");
		int i = 1;
		for(Element e:banners){
			System.out.println(i);
			System.out.println(e.select("a").attr("href"));
			i++;
		}
		
		
		
	}
}
