package jsonToText;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ThreadScraper{
	static Logger logger = Logger.getAnonymousLogger();	
	static List<String> antelope = new ArrayList<String>(){{
		add("Antelope Audio");
	    add("Antelope");	    
	    add("Goliath");
	    add("Orion");
	    add("Zen Tour");
	    add("Zen Studio");
	    add("Satori");
	    add("Atomic clock");
	    add("Live clock");
	    add("10mx");
	    add("fpga");
	    add("Discrete 4");
	    add("Discrete 8");
	    add("OCX HD");
	    add("Antelope Trinity");
	    add("Pure 2");
	    add("Eclipse");	    
	}};
	static List<String> uad = new ArrayList<String>(){{		
	    add("Universal Audio");
	    add("UAD");	    
	    add("apollo twin");
	    add("apollo");
	    add("arrow");
	    add("UAD satellite");
	    add("UAD live rack");
	}};
	
	static List<String> rme = new ArrayList<String>(){{		
		add("RME");
		add("fireface");
		add("babyface");
		add("madiface");
	}};
	
	static List<String> avid = new ArrayList<String>(){{		
		add("AVID");
		add("avid hd");		
	}};
	
	static List<String> slate = new ArrayList<String>(){{		
		add("Slate Digital");
		add("Slate");
		add("vrs8");		
	}};
	
	static List<List<String>> allBrands = new ArrayList<List<String>>(){{		
		add(antelope);
		add(uad);
		add(rme);
		add(avid);
		add(slate);	
	}};
	
	
	
	public static String getThreads(int page) throws InterruptedException{		
		System.out.println("..reading page N "+page);
		StringBuilder sb = new StringBuilder();
		String url = "https://www.gearslutz.com/board/music-computers/";
		if (page > 1) url = String.format("https://www.gearslutz.com/board/music-computers/index%s.html",page);
		Document doc = dealersToCheck.Miroc.connect(url);
		Elements tBody = doc.select("tbody");
		Elements threadz = tBody.select("tr[id]");
		
		for(Element e:threadz){
			Element thrName = e.select("a").get(1);
			String name = thrName.text().replace(",", " ");
			String urld = thrName.attr("href");
			Elements thrView = e.select("td[class=thread-views]>span");
			String replies = thrView.first().text().replace(",", " ");
			String views = thrView.get(1).text().replace(",", " ");
			Elements commented = e.select("td[class=thread-last-post]>span");
			String lcomment = commented.text();
			String brand = getBrand(name);
	    	if(!brand.isEmpty()){
	    		sb.append(String.format("%s,%s,%s,%s,%s,%s\n", name,urld,replies,views,lcomment,brand));		    		
	    	}  		 		    
		}		
		return sb.toString();
	}	
	
	public static String getBrand(String input){
		for(List<String> wordsToFind:allBrands){
			for (String toLookFor : wordsToFind) {			
				Pattern matches = Pattern.compile(buildPattern(Arrays.asList(toLookFor.split(" "))));					
				if (matches.matcher(input.toLowerCase()).find()) {
					return wordsToFind.get(0);
				}				
			}		
		}		
		return "";		
	}
	
	private static String buildPattern(List<String> matchString){
		  StringBuilder sb = new StringBuilder(5);
		  sb.append("^");
		  for(String match: matchString){
			  String added = String.format("(?=.*%s)", match.toLowerCase());
			  sb.append(added);
		  }		
		  sb.append(".*$");
		  return sb.toString();		  
	}
	
	/**private static Boolean threadIsImportant(String input, List<String> wordsToFind){
		for (String toLookFor : wordsToFind) {			
			Pattern matches = Pattern.compile(buildPattern(Arrays.asList(toLookFor.split(" "))));					
			if (matches.matcher(input.toLowerCase()).find()) {
				return true;
			}				
		}		
		return false;		
	}**/
		
	
	public static void write(String s){
		try (Writer writer = new FileWriter("otherdocs/threadList.csv",true)) {
			writer.append(s); 
		    writer.flush();
		    writer.close();
		    System.out.println("Success with writing to file!");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred. Can not write to dict file",e);
	    }
	}
	
			
	public static void main(String[] args) throws InterruptedException {		
		StringBuilder sb = new StringBuilder();
		for(int i=1;i<47;i++){			
			sb.append(getThreads(i));
			if(i%10==0){
				write(sb.toString());
				sb = new StringBuilder();
			}
			Thread.sleep(50);
		}
		write(sb.toString());		
		
	}	
}
