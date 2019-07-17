package jsonToText;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class HTMLToImage extends Application {

    private static String imageName, url;
    private Browser browser;
    private Timer timer = new java.util.Timer();

    public static void main(String[] args){
        imageName = "screenshot";
        url = "https://store.miroc.co.jp/p/search?criteria.limitCriteria.max=20&criteria.selectiveCategoryCriteria.categoriesToInclude=2013#20;20;DEFAULT;0|-1;";
        System.setProperty("jsse.enableSNIExtension", "false");
        //System.out.println("Creating screenshot for " + url);
        launch(args);
    }

    @Override
    public void start(Stage window) {
        window.setTitle(url);

        browser = new Browser(url);
        monitorPageStatus();        

        VBox layout = new VBox();
        layout.getChildren().addAll(browser);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setOnCloseRequest(we -> System.exit(0));
        window.show();
    }

    private void monitorPageStatus(){
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    if(browser.isPageLoaded()){
                        System.out.println("Page now loaded, taking screenshot...");
                       
                        //saveAsPng();
                        
                        System.out.println(browser.getDoc());
                        cancel();
                    }
                    else
                        System.out.println("Loading page...");
                });
            }
        }, 9000, 9000);
    }

    @FXML
    private void saveAsPng() {
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	WritableImage image = browser.snapshot(new SnapshotParameters(), null);
                    File file = new File("images/" + imageName + ".png");
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                        System.out.println("Screenshot saved as " + imageName + ".png");
                        System.exit(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                    cancel();
                });
            }
        }, 5000);
    } 

}
