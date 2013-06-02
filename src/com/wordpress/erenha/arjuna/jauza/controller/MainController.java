/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.model.CurrentSelection;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class MainController implements Initializable {

    @FXML
    private VBox browser;
    @FXML
    private BrowserController browserController;
    @FXML
    private VBox extractionPanel;
    @FXML
    private ExtractionPanelController extractionPanelController;
    private ObservableList<CurrentSelection> currentSelections;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        assert browser != null : "fx:id=\"browser\" was not injected: check your FXML file 'Main.fxml'.";
        assert extractionPanel != null : "fx:id=\"extractionPanel\" was not injected: check your FXML file 'Main.fxml'.";

        currentSelections = FXCollections.observableList(new ArrayList<CurrentSelection>());
        System.out.println(browser);
        System.out.println(browserController);

        System.out.println(extractionPanel);
        System.out.println(extractionPanelController);
        browserController.setMainController(this);
        extractionPanelController.setMainController(this);
        extractionPanelController.getCurrent().setItems(currentSelections);
        
    }

    public ObservableList<CurrentSelection> getCurrentSelections() {
        return currentSelections;
    }

}