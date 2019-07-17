package jsonToText;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dealersToCheck.CheckDealer;
import mailer.Email;

public class EmailScraper{
	static Logger logger = Logger.getAnonymousLogger();
	
	//public static Map<String,String>
	
	public static Queue<String> getUrlsList() throws InterruptedException{		
		Queue<String> urls = new LinkedList<String>();
		for (int i = 1;i<59;i++) {
			String url = "https://www.musiker-sucht.de/musikschule/index/page:"+i;
			Document doc = dealersToCheck.Miroc.connect(url);
			Elements myPart = doc.select("table[class=\"table table-striped table-hover classifieds \"]");
			Elements links = myPart.select("a");
			
			for(Element e:links){
		    	String link = e.attr("href");
		    	String name = e.text();
		    	if(!name.isEmpty()&&link.contains("/musikschule")){
		    		urls.add("https://www.musiker-sucht.de/"+link);		    		
		    	}
		    	
		    }
		    System.out.println("..reading page "+i);
		    Thread.sleep(50);
		}
		
		return urls;
	}	
	
	public static void readUrlsList(Queue<String> urls) throws InterruptedException{
		System.out.println("READING SCHOOLS URLS..");
		String info="School,Email,Website\n";
		int i=1;
		while(!urls.isEmpty()){
			String url = urls.poll();
			String urlInfo = readUrl(url);
			System.out.println(i+". "+urlInfo);
			info += urlInfo;
			i++;
			Thread.sleep(50);
		}
		write(info);
	}
	
	private static String readUrl(String url) throws InterruptedException{
		Document doc = dealersToCheck.Miroc.connect(url);
		String name = doc.select("h2").text().trim();	
		String website="";
		String email="";
		Elements urls = doc.select("a");
		for(Element e:urls){
			if(e.attr("href").contains("mailto:")) email=e.attr("href").substring(7);
			if(e.attr("href").contains("http://www")) website=e.attr("href");
		}
		
		return String.format("%s,%s,%s\n", name,email,website);
		
	}
	
	private static void write(String s){
		try (Writer writer = new FileWriter("otherdocs/emailList.txt")) {
			writer.write(s); 
		    writer.flush();
		    writer.close();
		    System.out.println("Success with writing to file!");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred. Can not write to dict file",e);
	    }
	}
	
			
	public static void main(String[] args) throws InterruptedException {
		readUrlsList(getUrlsList());
		
	}	
}
