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
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFIndividual;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFNamespace;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFOntology;
import com.wordpress.erenha.arjuna.jauza.util.StaticValue;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private ObservableList<RDFClass> currentClassesLabel;
    private ObservableList<RDFProperty> currentProperties;
    private ObservableList<RDFProperty> currentPropertiesLabel;
    private ObservableList<RDFProperty> currentPropertiesToShow;
    private ObservableList<RDFIndividual> currentIndividuals;
    
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
    @FXML
    private SaveTabController saveTabController;
    private Stage primaryStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
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
        currentClassesLabel = FXCollections.observableList(new ArrayList<RDFClass>());
        currentProperties = FXCollections.observableList(new ArrayList<RDFProperty>());
        currentPropertiesLabel = FXCollections.observableList(new ArrayList<RDFProperty>());
        currentIndividuals = FXCollections.observableList(new ArrayList<RDFIndividual>());
//        currentIndividualProperties = FXCollections.observableList(new ArrayList<RDFIndividualProperty>());

        annotationTabController.getBrowserController().setMainController(this);
//        annotationTabController.getBrowserController().load(Config.defaultWebAddress);
        annotationTabController.getBrowserController().load(StaticValue.defaultWebAddress);
        annotationTabController.getExtractionPanelController().setMainController(this);
//        annotationTabController.getExtractionPanelController().getCurrentSelectionTable().setItems(currentSelections);
        annotationTabController.getExtractionPanelController().getIndividualTable().setItems(currentIndividuals);


        ontologyTabController.setMainController(this);
//        ontologyTabController.getOntologyTable().setItems(currentContext);
        ontologyTabController.getOntologyTable().setItems(currentOntologies);
        ontologyTabController.getNamespaceTable().setItems(currentNamespaces);
        ontologyTabController.getClassesTable().setItems(currentClasses);
        ontologyTabController.getPropertiesTable().setItems(currentProperties);

        saveTabController.setMainController(this);
        saveTabController.getIndividualTable().setItems(currentIndividuals);
        
        
        rdfController = new RDFController();
        rdfController.setMainController(this);
//        rdfController.initRepository("data"); //init repo in client
//        String sesameServer = Config.sesameServer;
//        String repositoryID = Config.sesameRepositoryID;
        String sesameServer = StaticValue.sesameServer;
        String repositoryID = StaticValue.sesameRepositoryID;
//        String sesameServer = "http://localhost:8080/openrdf-sesame";
//        String repositoryID = "opendata";
//        String repositoryID = "jauzafx-db";
//        String repositoryID = "data-test";
        rdfController.initRepository(sesameServer, repositoryID); //init repo server
//        rdfController.getClasses(); //load class in start
//        rdfController.getProperties();
//        rdfController.getContext();
        rdfController.getOntologies();
        rdfController.getNamespaces();
        rdfController.getClasses(); //load class in start
//        rdfController.getClassesInNSdefined();
//        rdfController.getPropertiesInNSdefined();//set all properties
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

    public ObservableList<RDFClass> getCurrentClassesLabel() {
        return currentClassesLabel;
    }

    public ObservableList<RDFProperty> getCurrentPropertiesLabel() {
        return currentPropertiesLabel;
    }

    public ObservableList<RDFProperty> getCurrentPropertiesToShow() {
        return currentPropertiesToShow;
    }
    
    public ObservableList<RDFIndividual> getCurrentIndividuals() {
        return currentIndividuals;
    }

    public RDFController getRDFController() {
        return rdfController;
    }

    public AnnotationTabController getAnnotationTabController() {
        return annotationTabController;
    }
    
}