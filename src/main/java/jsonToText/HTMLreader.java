package jsonToText;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;


public class HTMLreader extends Application {
	
    private static String url ;
    private Browser browser;
    private Timer timer = new java.util.Timer();
    private static String html = "";
    
    

    //public static void main(String[] args){}
    
    public static String runme(String dealerPageURL){
    	url = dealerPageURL;
    	String[] args = null;
    	System.setProperty("jsse.enableSNIExtension", "false");      
    	launch(args);
    	return getHTML();    	
    }

    private static String getHTML(){
		return html;
    	
    }
    @Override
    public void start(Stage window) throws InterruptedException {
        window.setTitle(url);

        browser = new Browser(url);
        monitorPageStatus();        

        VBox layout = new VBox();
        layout.getChildren().addAll(browser);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setOnCloseRequest(we -> System.exit(0));
        //window.show();            
        //window.close();        
        
    }

    private void monitorPageStatus(){
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    if(browser.isPageLoaded()){
                        System.out.println("Page now loaded...");
                                               
                        html = browser.getDoc();
                        //System.out.println(html);
                        cancel();
                    }
                    else
                        System.out.println("Loading page...");
                });
            }
        }, 6000, 6000);
    }
  
}
