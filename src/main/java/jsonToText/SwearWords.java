package jsonToText;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dealersToCheck.CheckDealer;
import dealersToCheck.Miroc;
import mailer.Email;

public class SwearWords{
	static Logger logger = Logger.getAnonymousLogger();
	
	//public static Map<String,String>
	
	@SuppressWarnings("unchecked")
	public static void getSwearDict() throws InterruptedException{
		JSONObject obj = new JSONObject();
		
		for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
			String url = "https://www.noswearing.com/dictionary/"+alphabet;
			Document doc = Miroc.connect(url);
			Elements myPart = doc.select("td[valign=\"top\"]");
			Elements words = myPart.select("b");
			List<String> wordsMeaning = getWordsMeaning(myPart);
		    for(int i=0;i<words.size()-1;i++){
		    	obj.put(words.get(i).text(), wordsMeaning.get(i));		
		    }
		    System.out.println("..reading \""+alphabet+"\"");
		    Thread.sleep(50);
		}
		write(obj);
	}
	
	private static List<String> getWordsMeaning(Elements myPart){
		String meaning = "";
		for(Element e:myPart){
			meaning += e.ownText();
		}
		List<String> splitMeaning = new ArrayList<String>(Arrays.asList(meaning.split("- ")));
		splitMeaning.remove(0);
		for(Integer i=0;i<splitMeaning.size();i++){
			String s = splitMeaning.get(i).trim();
			//System.out.println(s);
			splitMeaning.set(i, s);
		}
		return splitMeaning;
	}
	
	private static void write(JSONObject obj){
		try (Writer writer = new FileWriter("otherdocs/sweardict.txt")) {
			writer.write(obj.toJSONString()); 
		    writer.flush();
		    writer.close();
		    System.out.println("Success with writing to dict file!");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "An exception occurred. Can not write to dict file",e);
	    }
	}
	
			
	public static void main(String[] args) throws InterruptedException {
		getSwearDict();
		


		/**String url = "https://www.noswearing.com/dictionary/"+"a";
		Document doc = CheckDealer.connect(url);
		Elements myPart = doc.select("td[valign=\"top\"]");
		Elements words = myPart.select("b");
		for(Element e:words.subList(0, words.size()-1)){
			String s = e.text();
			System.out.println(s);
		}**/
	}
	
	
	

}
