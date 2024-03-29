package main;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import dealersToCheck.Credentials;

import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GsheetsService {

    private static final String APPLICATION_NAME =
        "Google Sheets API Java Quickstart";

    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/sheets.googleapis.com.json");

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = GsheetsService.class.getResourceAsStream(Credentials.clientSecret);
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();    
    }
    
    public static List<List<Object>> readSheet(Sheets service,String spreadsheetId,String range) throws IOException{
    	ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
        List<List<Object>> values = response.getValues();
        System.out.println("Reading page completed..");
        return values;
    }
    
    public static void writeSheet(Sheets service,String spreadsheetId,String range, List<List<Object>> values) throws IOException{
    	ValueRange vr = new ValueRange().setValues(values).setMajorDimension("ROWS");
        service.spreadsheets().values()
                .update(spreadsheetId, range, vr)
                .setValueInputOption("RAW")
                .execute();
    }
    
    public static boolean addSheet(Sheets service, String spreadsheetId, String nameOfSheet){
    	
    	List<Request> requests = new ArrayList<>();
	    requests.add(new Request().setAddSheet(new AddSheetRequest()
                .setProperties(new SheetProperties().setTitle(nameOfSheet))));
	  
        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        try{
        	service.spreadsheets().batchUpdate(spreadsheetId, body).execute();
        	
        } catch(Exception e){
        	return false;
        }
        return true;
    }
}
