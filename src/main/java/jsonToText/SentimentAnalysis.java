package jsonToText;
import com.algorithmia.*;
import com.algorithmia.algo.*;


public class SentimentAnalysis {
	
	public static String getSentAnalysis(String text){
		AlgorithmiaClient client = Algorithmia.client("simy4r+USgpwewRBOqTg0zc4urI1");
		Algorithm algo = client.algo("nlp/SentimentAnalysis/1.0.5");
		AlgoResponse result = null;		
		
		String input = "{\"document\": \""				
				+ text
				+ "\"}";
		
		try {
			result = algo.pipeJson(input);
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return result.asJsonString();
		} catch (AlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String compareImages(String imagePath1, String imagePath2) throws APIException, AlgorithmException{
		String input = String.format("[%s,%s]", imagePath1, imagePath2);
			
		AlgorithmiaClient client = Algorithmia.client("simy4r+USgpwewRBOqTg0zc4urI1");
		Algorithm algo = client.algo("zskurultay/ImageSimilarity/0.1.4");
		AlgoResponse result = algo.pipeJson(input);		
		return result.asJsonString();
	}
	
	public static void main(String[] args) throws APIException, AlgorithmException {
		String text = "After two weeks of call support to my vendor and Antelope Audio my HD still has USB problems and I'm forced to return the Unite to my vendor."
				+ "Asus Z170-A - four crore/eight threads i7 Windows 10 64bit - On the same system my Orion32 works just fine for years."
				+ "Antelope what going on! Sent the unit back got a replacement that work just as expected. Apparently the problem was linked to the firmware version make sure yours is at least 1.43 ";
		
		String s = getSentAnalysis(text);
		//JsonObject obj = new JsonParser().parse(s).getAsJsonObject();		
		System.out.println(s);
	}

}
