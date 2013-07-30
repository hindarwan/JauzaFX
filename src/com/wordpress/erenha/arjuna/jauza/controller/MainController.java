/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.model.CurrentSelection;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFClass;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFContext;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFProperty;
import com.wordpress.erenha.arjuna.jauza.rdf.RDFController;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFNamespace;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFOntology;
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
    private ObservableList<RDFContext> currentContext;
    private ObservableList<RDFNamespace> currentNamespaces;
    private ObservableList<RDFOntology> currentOntologies;
    private ObservableList<RDFClass> currentClasses;
    private ObservableList<String> currentClassesLabel;
    private ObservableList<RDFProperty> currentProperties;
    private ObservableList<String> currentPropertiesLabel;
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
        currentContext = FXCollections.observableList(new ArrayList<RDFContext>());
        currentNamespaces = FXCollections.observableList(new ArrayList<RDFNamespace>());
        currentOntologies = FXCollections.observableList(new ArrayList<RDFOntology>());
        currentClasses = FXCollections.observableList(new ArrayList<RDFClass>());
        currentClassesLabel = FXCollections.observableList(new ArrayList<String>());
        currentProperties = FXCollections.observableList(new ArrayList<RDFProperty>());
        currentPropertiesLabel = FXCollections.observableList(new ArrayList<String>());

        annotationTabController.getBrowserController().setMainController(this);
        annotationTabController.getExtractionPanelController().setMainController(this);
        annotationTabController.getExtractionPanelController().getCurrentSelectionTable().setItems(currentSelections);

        ontologyTabController.setMainController(this);
//        ontologyTabController.getOntologyTable().setItems(currentContext);
        ontologyTabController.getOntologyTable().setItems(currentOntologies);
        ontologyTabController.getNamespaceTable().setItems(currentNamespaces);
        ontologyTabController.getClassesTable().setItems(currentClasses);
        ontologyTabController.getPropertiesTable().setItems(currentProperties);

        rdfController = new RDFController();
        rdfController.setMainController(this);
//        rdfController.initRepository("data"); //init repo in client
        String sesameServer = "http://localhost:8080/openrdf-sesame";
        String repositoryID = "opendata";
//        String repositoryID = "jauzafx-db";
//        String repositoryID = "data-test";
        rdfController.initRepository(sesameServer, repositoryID); //init repo server
//        rdfController.getClasses(); //load class in start
//        rdfController.getProperties();
//        rdfController.getContext();
        rdfController.getOntologies();
        rdfController.getNamespaces();
        rdfController.getClassesInNSdefined();
        rdfController.getPropertiesInNSdefined();
    }

    public ObservableList<CurrentSelection> getCurrentSelections() {
        return currentSelections;
    }

    public ObservableList<RDFContext> getCurrentContext() {
        return currentContext;
    }

    public ObservableList<RDFNamespace> getCurrentNamespaces() {
        return currentNamespaces;
    }

    public ObservableList<RDFOntology> getCurrentOntologies() {
        return currentOntologies;
    }
    
    public ObservableList<RDFClass> getCurrentClasses() {
        return currentClasses;
    }

    public ObservableList<RDFProperty> getCurrentProperties() {
        return currentProperties;
    }

    public ObservableList<String> getCurrentClassesLabel() {
        return currentClassesLabel;
    }

    public ObservableList<String> getCurrentPropertiesLabel() {
        return currentPropertiesLabel;
    }
    
    

    public RDFController getRDFController() {
        return rdfController;
    }
}