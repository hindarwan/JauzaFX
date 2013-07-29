/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.model.CurrentSelection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
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
public class BrowserController implements Initializable {

    @FXML //  fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader
    @FXML //  fx:id="forwardButton"
    private Button forwardButton; // Value injected by FXMLLoader
    @FXML //  fx:id="goButton"
    private Button goButton; // Value injected by FXMLLoader
    @FXML //  fx:id="inspectButton"
    private ToggleButton inspectButton; // Value injected by FXMLLoader
    @FXML //  fx:id="progress"
    private ProgressBar progress; // Value injected by FXMLLoader
    @FXML //  fx:id="progressText"
    private Label progressText; // Value injected by FXMLLoader
    @FXML //  fx:id="status"
    private Label status; // Value injected by FXMLLoader
    @FXML //  fx:id="txt"
    private TextField txt; // Value injected by FXMLLoader
    @FXML //  fx:id="webx"
    private WebView webx; // Value injected by FXMLLoader
    //NON FXML
    private final String INSPECT_SCRIPT = "javascript:(function() { var s = document.createElement('div'); s.innerHTML = 'Loading...'; s.style.color = 'black'; s.style.padding = '5px'; s.style.margin = '5px'; s.style.position = 'fixed'; s.style.zIndex = '9999'; s.style.fontSize = '24px'; s.style.border = '1px solid black'; s.style.right = '5px'; s.style.top = '5px'; s.setAttribute('class', 'selector_gadget_loading'); s.style.background = 'white'; document.body.appendChild(s); s = document.createElement('script'); s.setAttribute('type', 'text/javascript'); s.setAttribute('src', 'http://localhost/selectorgadgetCustom/lib/selectorgadgetNotSuggestion.js?raw=true'); document.body.appendChild(s); })();";
    private String defaultAddress = "http://bps.go.id";
    private WebEngine engine;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'browser.fxml'.";
        assert forwardButton != null : "fx:id=\"forwardButton\" was not injected: check your FXML file 'browser.fxml'.";
        assert goButton != null : "fx:id=\"goButton\" was not injected: check your FXML file 'browser.fxml'.";
        assert inspectButton != null : "fx:id=\"inspectButton\" was not injected: check your FXML file 'browser.fxml'.";
        assert progress != null : "fx:id=\"progress\" was not injected: check your FXML file 'browser.fxml'.";
        assert progressText != null : "fx:id=\"progressText\" was not injected: check your FXML file 'browser.fxml'.";
        assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'browser.fxml'.";
        assert txt != null : "fx:id=\"txt\" was not injected: check your FXML file 'browser.fxml'.";
        assert webx != null : "fx:id=\"webx\" was not injected: check your FXML file 'browser.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        // engine
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
        if (inspectButton.isSelected()) {
            System.out.println("[INFO] inspect mode");
            engine.executeScript(INSPECT_SCRIPT);
            backButton.setDisable(true);
            forwardButton.setDisable(true);
            txt.setDisable(true);
            goButton.setDisable(true);
            webx.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    getSelectedElement();
                }
            });
        } else {
            System.out.println("[INFO] browser mode");
            engine.executeScript(INSPECT_SCRIPT);
            backButton.setDisable(false);
            forwardButton.setDisable(false);
            txt.setDisable(false);
            goButton.setDisable(false);
            webx.setOnMouseClicked(null);
        }
    }

    private void initEngine() {
        engine = webx.getEngine();
        engine.load(defaultAddress);
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
            }
        });
        engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                double d = newValue.doubleValue();
                progress.setProgress(d / 100);
                progressText.setText((int) d + "%");
                if (d == 100) {
                    progress.setVisible(false);
                    progressText.setVisible(false);
                }else{
                    progress.setVisible(true);
                    progressText.setVisible(true);
                }
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
                if (e.getData().equals("jauza:002")) { //clear selection
                    System.out.println("[INFO] clear selection");
                } else {
                }
            }
        });
    }

    @FXML
    public void test(ActionEvent event) {

        engine.executeScript("var a = document.querySelector('[jfxid=\"32\"]').getAttribute('class');"
                + "document.querySelector('[jfxid=\"32\"]').setAttribute('class', a + ' sg_selected');");
    }

    private void getSelectedElement() {
        mainController.getCurrentSelections().clear();
        Integer length = (Integer) engine.executeScript("document.querySelectorAll('[class*=\\\"sg_selected\\\"]').length");
        System.out.println("[INFO] selected : " + length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                Element selectedElement = (Element) engine.executeScript("document.querySelectorAll('[class*=\\\"sg_selected\\\"]').item(" + i + ")");
                String id = selectedElement.getAttribute("jfxid");
                String content = selectedElement.getTextContent();
                String link = selectedElement.getAttribute("href");
                System.out.println("id : " + id);
                System.out.println("content : " + content);
                System.out.println("link : " + link);
                NodeList links = selectedElement.getElementsByTagName("a");
                for (int j = 0; j < links.getLength(); j++) {
                    Element l = (Element) links.item(j);
                    System.out.println("child link : " + l.getAttribute("href"));
                }
                mainController.getCurrentSelections().add(new CurrentSelection(id, content.trim()));
            }
        }
    }

    private void selectElementByJFXID(int id) {
        engine.executeScript("var a = document.querySelector('[jfxid=\"" + id + "\"]').getAttribute('class');"
                + "document.querySelector('[jfxid=\"" + id + "\"]').setAttribute('class', a + ' sg_selected');");
    }
}
