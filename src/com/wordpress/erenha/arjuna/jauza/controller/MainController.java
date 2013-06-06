/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.model.CurrentSelection;
import com.wordpress.erenha.arjuna.jauza.rdf.RDFClass;
import com.wordpress.erenha.arjuna.jauza.rdf.RDFProperty;
import com.wordpress.erenha.arjuna.jauza.rdf.RDFController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class MainController implements Initializable {

    private ObservableList<CurrentSelection> currentSelections;
    private ObservableList<RDFClass> currentClasses;
    private ObservableList<RDFProperty> currentProperties;
    private RDFController rdfController;
    @FXML //  fx:id="annotationTab"
    private HBox annotationTab; // Value injected by FXMLLoader
    @FXML
    private AnnotationTabController annotationTabController;
    @FXML //  fx:id="individualTab"
    private VBox individualTab; // Value injected by FXMLLoader
    @FXML
    private IndividualTabController individualTabController;
    @FXML //  fx:id="ontologyTab"
    private VBox ontologyTab; // Value injected by FXMLLoader
    @FXML
    private OntologyTabController ontologyTabController;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert annotationTab != null : "fx:id=\"annotationTab\" was not injected: check your FXML file 'Main.fxml'.";
        assert individualTab != null : "fx:id=\"individualTab\" was not injected: check your FXML file 'Main.fxml'.";
        assert ontologyTab != null : "fx:id=\"ontologyTab\" was not injected: check your FXML file 'Main.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        //Global Model
        currentSelections = FXCollections.observableList(new ArrayList<CurrentSelection>());
        currentClasses = FXCollections.observableList(new ArrayList<RDFClass>());
        currentProperties = FXCollections.observableList(new ArrayList<RDFProperty>());

        annotationTabController.getBrowserController().setMainController(this);
        annotationTabController.getExtractionPanelController().setMainController(this);
        annotationTabController.getExtractionPanelController().getCurrentSelectionTable().setItems(currentSelections);

        ontologyTabController.setMainController(this);
        ontologyTabController.getClassesTable().setItems(currentClasses);
        ontologyTabController.getPropertiesTable().setItems(currentProperties);

        rdfController = new RDFController();
        rdfController.setMainController(this);
        rdfController.initRepository("data");
        rdfController.getClasses();
        rdfController.getProperties();
    }

    public ObservableList<CurrentSelection> getCurrentSelections() {
        return currentSelections;
    }

    public ObservableList<RDFClass> getCurrentClasses() {
        return currentClasses;
    }

    public ObservableList<RDFProperty> getCurrentProperties() {
        return currentProperties;
    }

    public RDFController getRepository() {
        return rdfController;
    }
}