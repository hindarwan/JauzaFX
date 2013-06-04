/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class AnnotationTabController implements Initializable {

    @FXML //  fx:id="browser"
    private VBox browser; // Value injected by FXMLLoader
    @FXML
    private BrowserController browserController;
    @FXML //  fx:id="extractionPanel"
    private VBox extractionPanel; // Value injected by FXMLLoader
    @FXML
    private ExtractionPanelController extractionPanelController;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert browser != null : "fx:id=\"browser\" was not injected: check your FXML file 'annotationTab.fxml'.";
        assert extractionPanel != null : "fx:id=\"extractionPanel\" was not injected: check your FXML file 'annotationTab.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

    }

    public BrowserController getBrowserController() {
        return browserController;
    }

    public ExtractionPanelController getExtractionPanelController() {
        return extractionPanelController;
    }
    
    
}
