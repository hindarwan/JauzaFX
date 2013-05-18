/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.model.ExtractedIndividual;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class FXMLController implements Initializable {

    private final String INSPECT_SCRIPT = "javascript:(function() { var s = document.createElement('div'); s.innerHTML = 'Loading...'; s.style.color = 'black'; s.style.padding = '5px'; s.style.margin = '5px'; s.style.position = 'fixed'; s.style.zIndex = '9999'; s.style.fontSize = '24px'; s.style.border = '1px solid black'; s.style.right = '5px'; s.style.top = '5px'; s.setAttribute('class', 'selector_gadget_loading'); s.style.background = 'white'; document.body.appendChild(s); s = document.createElement('script'); s.setAttribute('type', 'text/javascript'); s.setAttribute('src', 'http://localhost/selectorgadgetCustom/lib/selectorgadgetNotSuggestion.js?raw=true'); document.body.appendChild(s); })();";
    @FXML
    private WebView webx;
    @FXML
    private TextField txt;
    @FXML //  fx:id="progress"
    private ProgressBar progress; // Value injected by FXMLLoader
    @FXML //  fx:id="status"
    private Label status; // Value injected by FXMLLoader
    @FXML //  fx:id="progressText"
    private Label progressText; // Value injected by FXMLLoader
    @FXML //  fx:id="columnContent"
    private TableColumn<?, ?> columnContent; // Value injected by FXMLLoader
    @FXML //  fx:id="columnID"
    private TableColumn<?, ?> columnID; // Value injected by FXMLLoader
    @FXML //  fx:id="columnProperty"
    private TableColumn<?, ?> columnProperty; // Value injected by FXMLLoader
    @FXML //  fx:id="tableExtInd"
    private TableView<ExtractedIndividual> tableExtInd; // Value injected by FXMLLoader
    //NON FXML
    private ObservableList<ExtractedIndividual> extractedIndividual;
//    private boolean isSelectionMode = false;
//    private String selectedElement = "";
//    private String htmlDoc = "";
    private WebEngine engine;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert columnContent != null : "fx:id=\"columnContent\" was not injected: check your FXML file 'FXML.fxml'.";
        assert columnID != null : "fx:id=\"columnID\" was not injected: check your FXML file 'FXML.fxml'.";
        assert columnProperty != null : "fx:id=\"columnProperty\" was not injected: check your FXML file 'FXML.fxml'.";
        assert progress != null : "fx:id=\"progress\" was not injected: check your FXML file 'FXML.fxml'.";
        assert progressText != null : "fx:id=\"progressText\" was not injected: check your FXML file 'FXML.fxml'.";
        assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'FXML.fxml'.";
        assert txt != null : "fx:id=\"txt\" was not injected: check your FXML file 'FXML.fxml'.";
        assert webx != null : "fx:id=\"webx\" was not injected: check your FXML file 'FXML.fxml'.";
        assert tableExtInd != null : "fx:id=\"tableExtInd\" was not injected: check your FXML file 'FXML.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        // table
        columnContent.setCellValueFactory(new PropertyValueFactory("content"));
        columnID.setCellValueFactory(new PropertyValueFactory("id"));
        columnProperty.setCellValueFactory(new PropertyValueFactory("property"));
        ArrayList<ExtractedIndividual> l = new ArrayList<>();
        l.add(new ExtractedIndividual("Jacob", "Smith", "jacob.smith@example.com"));
        extractedIndividual = FXCollections.observableList(l);

        tableExtInd.setItems(extractedIndividual);
        //engine
        engine = webx.getEngine();
        engine.load("http://localhost/selectorgadget/");
        initEngine();
    }

    @FXML
    public void go(ActionEvent event) {
        engine.load(txt.getText());
        System.out.println("[INFO] browser: go to " + txt.getText());
    }

    @FXML
    public void back(ActionEvent event) {
        JSObject history = (JSObject) engine.executeScript("history");
        history.call("back");
        System.out.println("[INFO] browser: back");
    }

    @FXML
    public void forward(ActionEvent event) {
        JSObject history = (JSObject) engine.executeScript("history");
        history.call("forward");
        System.out.println("[INFO] browser: forward");
    }

    @FXML
    public void inspect(ActionEvent event) {
        System.out.println("[INFO] inspect mode");
        engine.executeScript(INSPECT_SCRIPT);
//        selectedElement = "";
    }

    @FXML //versi 2
    public void getSelectedElement(ActionEvent event) {
        Integer length = (Integer) engine.executeScript("document.querySelectorAll('[class=\\\"sg_selected\\\"]').length");
        System.out.println(length);
        if (length > 0) {
            ArrayList<ExtractedIndividual> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Element selectedElement = (Element) engine.executeScript("document.querySelectorAll('[class=\\\"sg_selected\\\"]').item(" + i + ")");
                String id = selectedElement.getAttribute("jfxid");
                String content = selectedElement.getTextContent();
                String link = selectedElement.getAttribute("href");
                System.out.println("id : " + id);
                System.out.println("content : " + content);
                System.out.println("link : " + link);
                NodeList links = selectedElement.getElementsByTagName("a");
                for (int j = 0; i < links.getLength(); i++) {
                    Element l = (Element) links.item(j);
                    System.out.println("link : " + l.getAttribute("href"));
                }
                ExtractedIndividual ei = new ExtractedIndividual(id, content.trim(), "");
                list.add(ei);
            }
            extractedIndividual = FXCollections.observableList(list);
            tableExtInd.setItems(extractedIndividual);
        }
    }

    private void initEngine() {
        engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(final WebEvent<String> event) {
                status.setText(event.getData());
            }
        });

        engine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                txt.setText(t1);
//                selectedElement = "";
            }
        });
        engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                double d = newValue.doubleValue();
                progress.setProgress(d / 100);
                progressText.setText((int) d + "%");
            }
        });

        engine.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {
            @Override
            public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
                if (engine.getLoadWorker().getState() == Worker.State.FAILED) {
                    status.setText("Loading Error!!!");
                }
            }
        });


        engine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> e) {
                //if make selection mode is completed
//                if (e.getData().equals("jauza:001")) {
                //document ready for selection
                //than parse the code to html string via jsoup
                // ouch but i can code with standard, goodbye jsoup C.U.
//                    System.out.println("[INFO] jauza:001");
//                } else {
                if (e.getData().equals("jauza:002")) { //clear selection
//                    selectedElement = "";
                    System.out.println("[INFO] clear selection");
                } else {
//                    selectedElement = e.getData();
//                    System.out.println("[INFO] selected element: " + selectedElement);
                }
            }
        });
    }
    
    @FXML
    public void test(ActionEvent event) {
        System.out.println("test");
        Integer length = (Integer) engine.executeScript("document.querySelectorAll('[class=\\\"sg_selected\\\"]').length");
        System.out.println(length);
        if (length > 0) {
            ArrayList<ExtractedIndividual> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Element selectedElement = (Element) engine.executeScript("document.querySelectorAll('[class=\\\"sg_selected\\\"]').item(" + i + ")");
                String id = selectedElement.getAttribute("jfxid");
                String content = selectedElement.getTextContent();
                String link = selectedElement.getAttribute("href");
                System.out.println("id : " + id);
                System.out.println("content : " + content);
                System.out.println("link : " + link);
                NodeList links = selectedElement.getElementsByTagName("a");
                for (int j = 0; i < links.getLength(); i++) {
                    Element l = (Element) links.item(j);
                    System.out.println("link : " + l.getAttribute("href"));
                }
                ExtractedIndividual ei = new ExtractedIndividual(id, content.trim(), "");
                list.add(ei);
            }
            extractedIndividual = FXCollections.observableList(list);
            tableExtInd.setItems(extractedIndividual);
        }

    }
    
//    @FXML
//    public void getSelectedElement1(ActionEvent event) {
        //with standard
//        if (!selectedElement.isEmpty()) {
//            String[] els = selectedElement.split(",");
//            ArrayList<ExtractedIndividual> list = new ArrayList<>();
//            for (String el : els) {
//                //with JS Standard++
//                Element a = (Element) engine.executeScript("document.querySelector('[jfxid=\"" + el + "\"]')");
//
//                System.out.println("id : " + el);
//                System.out.println("content : " + a.getTextContent());
//                System.out.println("link : " + a.getAttribute("href"));
//                NodeList link = a.getElementsByTagName("a");
//                for (int i = 0; i < link.getLength(); i++) {
//                    Element l = (Element) link.item(i);
//                    System.out.println("link : " + l.getAttribute("href"));
//                }
//                ExtractedIndividual ei = new ExtractedIndividual(el, a.getTextContent().trim(), "");
//                list.add(ei);

                //with Jquery
                //                JQueryUtil.executejQuery(engine,
                //                        "var txt = $('[jfxid=\"" + el +"\"]').text();"
                //                        + "alert(txt);");



                //with JS standard
                //                Element a = (Element) engine.executeScript("document.getElementById('" + el + "')");
                ////                                Element a = (Element) engine.executeScript("jQuery('#" + el + "').attr('jfxid')");
                //
                //                System.out.println("id : " + el);
                //                System.out.println(a.getTextContent());
                //                System.out.println("link : " + a.getAttribute("href"));
                //                NodeList link = a.getElementsByTagName("a");
                //                for (int i = 0; i < link.getLength(); i++) {
                //                    Element l = (Element) link.item(i);
                //                    System.out.println("link : " + l.getAttribute("href"));
                //                }
                //                ExtractedIndividual ei = new ExtractedIndividual(el, a.getTextContent().trim(), "");
                //                list.add(ei);
//            }
//            extractedIndividual = FXCollections.observableList(list);
//
//            tableExtInd.setItems(extractedIndividual);
//        }

        //with 3rd party lib
//            org.jsoup.nodes.Document doc = Jsoup.parse(htmlDoc);
//            String[] els = selectedElement.split(",");
//            for (String el : els) {
//                String text = doc.select("#" + el).text();
//                System.out.println(text);
//            }
//    }
    
//        @FXML
//    public void viewSource(ActionEvent event) {
////        System.out.println(getStringFromDocument(engine.getDocument()));
//    }
    //method to convert Document to String
//    private String getStringFromDocument(Document doc) {
//        try {
//            DOMSource domSource = new DOMSource(doc.getElementsByTagName("body").item(0));
//            StringWriter writer = new StringWriter();
//            StreamResult result = new StreamResult(writer);
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//            transformer.transform(domSource, result);
//            return writer.toString();
//        } catch (TransformerException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }

}