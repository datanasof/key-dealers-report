package jsonToText;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

class Browser extends Region {

    private final WebView browser = new WebView();
    

    Browser(String url) {
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        getChildren().add(browser);
    }

    Boolean isPageLoaded(){
        return browser.getEngine().getDocument() != null;
    }
    
    String getDoc(){
    	WebEngine webengine = browser.getEngine();
    	return getStringFromDocument(webengine.getDocument());
    }
    
    //method to convert Document to String
    public String getStringFromDocument(Document doc)
    {
        try
        {
           DOMSource domSource = new DOMSource(doc);
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.transform(domSource, result);
           return writer.toString();
        }
        catch(TransformerException ex)
        {
           ex.printStackTrace();
           return null;
        }
    } 

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double width) {
        return 1280;
    }

    @Override
    protected double computePrefHeight(double height) {
        return 720;
    }

}