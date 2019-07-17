package main;
import java.io.IOException;
import dealersToCheck.Credentials;


public class Tester {	
	
	
	public static void main(String[] args) throws IOException {
		GsheetsManager gmanager = new GsheetsManager();	
		java.util.List<java.util.List<Object>> dealerProducts = gmanager.readSheet(Credentials.infoSpreadsheetId, "2018-08-02!A3:X9999");
		Main.writeSummaryReport(gmanager, dealerProducts);			

	}

}
