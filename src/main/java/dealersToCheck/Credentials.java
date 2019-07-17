package dealersToCheck;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Credentials {
	public static final String proxyHost = "192.168.10.251";
	public static final String proxyPort = "1098";
	public static final String reportSpreadsheetId = "1FVnalhzruZKRZcUNOw6GXAIXr9YBGcEkmVzSMvQPA8Q";
	public static final String additionalReportSpreadsheetId = "1RvWG8aerleo03rSHBdS_NM9mkg0ZgjzatDhy1YvSFtw";
	public static final String infoSpreadsheetId = "1FVnalhzruZKRZcUNOw6GXAIXr9YBGcEkmVzSMvQPA8Q";	
	public static final String productsInfoSpreadsheetId = "1pJn9MCc7Ny8F8yKCstdi2Ut3ZMINXCWrJESoEcUzeuU";	
	public static final String clientSecret = "/client_secret.json";
	public static final Set<String> brandsForAdditioanalCheck = new HashSet<String>(Arrays.asList("antelope audio"
			, "antelope", "universal audio", "ua" 
			,"slate digital", "focusrite", "presonus", "apogee", "rme", "motu"));
	
	public static final String readproductStatus = "Reading product ";
	public static final String writeproductStatus = "Writing product ";
	public static final String catStatus = "Reading category page ";
	public static final String readStatus = "Checking for categories and pages to visit..";
	
	public static final String[] recipients = {"dimitar@antelopeaudio.com", "sstaynov@antelopeaudio.com", "ngeorgiev@antelopeaudio.com"};
	public static final String[] recipientsThomann = {"dimitar@antelopeaudio.com", "sstaynov@antelopeaudio.com", "ngeorgiev@antelopeaudio.com","tivanova@antelopeaudio.com"};
	public static final String[] promoRecipients = {"dimitar@antelopeaudio.com", "tdimitrov@antelopeaudio.com", "lnedyalkov@antelopeaudio.com", "kvelkov@antelopeaudio.com", "sstaynov@antelopeaudio.com", "ngeorgiev@antelopeaudio.com"};
	
	public static final Set<String> brandsToBeProcessed = new HashSet<String>(Arrays.asList("antelope","antelope audio","universal audio","ua","uad","avid","focusrite","apogee","motu","rme","presonus","slate digital","slate","zoom", "townsend labs"));
																									
	@SuppressWarnings("serial")
	public static final HashMap<String, Integer> brands = new HashMap<String, Integer>(){{
		put("Antelope Audio",15);
		put("Antelope",9);
		put("Universal Audio",16);
		put("Apogee",7);
		put("Focusrite",10);
		put("Presonus",9);
		put("Arturia",8);
		put("RME",4);
		put("Audient",8);
		put("M-audio",8);
		put("Tascam",7);
		put("Motu",5);
		put("Behringer",10);
		put("Alesis",7);
		put("Avid",5);
		put("Arturia",8);
		put("Slate Digital",14);
		put("Zoom",5);
		put("Steinberg",10);
		put("Roland",7);
		put("UA",3);
		put("Roland",7);
		put("Lynx",5);
		
	}};
	
	public static final Map<String, Integer> reportCodes = new HashMap<String, Integer>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		put("NEW PRODUCT/BUNDLE",1);
		put("PRICE DROP",2);		
	}};
	
	public static Set<String> trackDealersProductsAvailability = new HashSet<String>(Arrays.asList("Thomann", "VintageKing", "ZZsounds", "Bax" 
			, "Woodbrass", "MusicStore")); //LongMcQuade not added yet
	public static Set<String> productInStock = new HashSet<String>(Arrays.asList("Sofort lieferbar"
			, "In Stock", "Only 3 Left!", "Only 2 Left!", "Only 1 Left!"));	
	
	public static String getRepEmail(String dealerCode){
		String email;
		switch (dealerCode) {
        case "Thomann":  email = "dimitar@antelopeaudio.com";
                 break;          
        case "BAX":  email = "dimitar@antelopeaudio.com";
                 break;        
        case "MusicStore":  email = "dimitar@antelopeaudio.com";
                 break;        
        case "Gear4music":  email = "dimitar@antelopeaudio.com";
                 break;
        case "Andertons":  email = "dimitar@antelopeaudio.com";
        		 break;        		 
        case "LongMcQuade":  email = "dimitar@antelopeaudio.com";
		 		break;
        case "Woodbrass":  email = "dimitar@antelopeaudio.com";
                 break;  
        case "TurraMusic":  email = "dimitar@antelopeaudio.com";
        		 break;
        case "VintageKing":  email = "dimitar@antelopeaudio.com";
        		break;    
        case "Sweetwater":  email = "dimitar@antelopeaudio.com";
        		break;        
                 
        default: email = "dimitar@antelopeaudio.com";
        break;
		}		
		return email;		
	}

	@SuppressWarnings("serial")
	public static final HashMap<String, String> ThomannAvailCodes = new HashMap<String, String>(){{
		put("Sofort lieferbar","Available");
		put("Kurzfristig lieferbar (2-5 Tage)","Available shortly (2-5 days)");
		put("In ca. einer Woche lieferbar","In stock within a week!");
		put("In 1-2 Wochen lieferbar","In stock within 1-2 weeks!");
		put("In 2-3 Wochen lieferbar","In stock within 2-3 weeks!");
		put("In 5-6 Wochen lieferbar","In stock within 5-6 weeks!");
		put("In 7-8 Wochen lieferbar","In stock within 7-8 weeks!");
		put("Auf Anfrage","On request!");
		
	}};
}
