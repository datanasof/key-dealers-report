package main;


import java.io.IOException;
import java.util.List;
import com.google.api.services.sheets.v4.Sheets;


public class GsheetsManager {
	private static Sheets service;
	
	public GsheetsManager() throws IOException{
		GsheetsManager.service = GsheetsService.getSheetsService();
		System.out.println("Initializing Google Sheets Manager..");
	}
	
	public List<List<Object>> readSheet(String spreadsheetId,String range) throws IOException{
		System.out.println("Reading page..");
		return GsheetsService.readSheet(service, spreadsheetId, range);
	}
		    
	public void writeSheet(String spreadsheetId,String range, List<List<Object>> values) throws IOException{
		GsheetsService.writeSheet(service, spreadsheetId, range, values);
	}
	    
	public boolean addSheet(String spreadsheetId, String nameOfSheet){
		return GsheetsService.addSheet(service, spreadsheetId, nameOfSheet);
	}
	
	
}
