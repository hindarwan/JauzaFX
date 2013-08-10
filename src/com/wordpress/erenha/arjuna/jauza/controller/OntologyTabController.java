/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFClass;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFContext;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFNamespace;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFOntology;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFProperty;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class OntologyTabController implements Initializable {

    @FXML //  fx:id="namespacePrefixColumn"
    private TableColumn<RDFNamespace, String> namespacePrefixColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="namespacePrefixField"
    private TextField namespacePrefixField; // Value injected by FXMLLoader
    @FXML //  fx:id="namespaceURIColumn"
    private TableColumn<RDFNamespace, String> namespaceURIColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="namespaceURIField"
    private TextField namespaceURIField; // Value injected by FXMLLoader
    @FXML //  fx:id="namespaceTable"
    private TableView<RDFNamespace> namespaceTable; // Value injected by FXMLLoader
    @FXML //  fx:id="ontologyLabelColumn"
    private TableColumn<RDFOntology, String> ontologyLabelColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="ontologyTable"
    private TableView<RDFOntology> ontologyTable; // Value injected by FXMLLoader
    @FXML //  fx:id="ontologyURIColumn"
    private TableColumn<RDFOntology, String> ontologyURIColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="classesLabelColumn"
    private TableColumn<RDFClass, String> classesLabelColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="classesTable"
    private TableView<RDFClass> classesTable; // Value injected by FXMLLoader
    @FXML //  fx:id="classesURIColumn"
    private TableColumn<RDFClass, String> classesURIColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="propertiesLabelColumn"
    private TableColumn<RDFProperty, String> propertiesLabelColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="propertiesTable"
    private TableView<RDFProperty> propertiesTable; // Value injected by FXMLLoader
    @FXML //  fx:id="propertiesURIColumn"
    private TableColumn<RDFProperty, String> propertiesURIColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="urlFileImport"
    private TextField urlFileImport; // Value injected by FXMLLoader
    //NON FXML
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert ontologyLabelColumn != null : "fx:id=\"ontologyLabelColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert ontologyTable != null : "fx:id=\"ontologyTable\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert ontologyURIColumn != null : "fx:id=\"ontologyURIColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert namespacePrefixColumn != null : "fx:id=\"namespacePrefixColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert namespacePrefixField != null : "fx:id=\"namespacePrefixField\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert namespaceURIColumn != null : "fx:id=\"namespaceURIColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert namespaceURIField != null : "fx:id=\"namespaceURIField\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert namespaceTable != null : "fx:id=\"namespaceTable\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert classesLabelColumn != null : "fx:id=\"classesLabelColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert classesTable != null : "fx:id=\"classesTable\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert classesURIColumn != null : "fx:id=\"classesURIColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert propertiesLabelColumn != null : "fx:id=\"propertiesLabelColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert propertiesTable != null : "fx:id=\"propertiesTable\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert propertiesURIColumn != null : "fx:id=\"propertiesURIColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert urlFileImport != null : "fx:id=\"urlFileImport\" was not injected: check your FXML file 'ontologyTab.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        initTableColumn();
        initTableModel();
    }

    private void initTableColumn() {
        ontologyLabelColumn.setCellValueFactory(new PropertyValueFactory("label"));
        ontologyURIColumn.setCellValueFactory(new PropertyValueFactory("uri"));
        namespaceURIColumn.setCellValueFactory(new PropertyValueFactory("namespace"));
        namespacePrefixColumn.setCellValueFactory(new PropertyValueFactory("prefix"));
        classesLabelColumn.setCellValueFactory(new PropertyValueFactory("label"));
        classesURIColumn.setCellValueFactory(new PropertyValueFactory("uri"));
        propertiesLabelColumn.setCellValueFactory(new PropertyValueFactory("label"));
        propertiesURIColumn.setCellValueFactory(new PropertyValueFactory("uri"));
    }
    
    private void initTableModel(){
        ontologyTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RDFOntology>() {

            @Override
            public void changed(ObservableValue<? extends RDFOntology> ov, RDFOntology t, RDFOntology t1) {
                mainController.getRDFController().getClassesByNS(t1.getUri());
                mainController.getRDFController().getPropertiesByNS(t1.getUri());
            }
        });
        
        namespaceTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RDFNamespace>() {

            @Override
            public void changed(ObservableValue<? extends RDFNamespace> ov, RDFNamespace t, RDFNamespace t1) {
                mainController.getRDFController().getClassesByNS(t1.getNamespace());
                mainController.getRDFController().getPropertiesByNS(t1.getNamespace());
            }
        });
        
    }

    public TableView<RDFOntology> getOntologyTable() {
        return ontologyTable;
    }

    public TableView<RDFNamespace> getNamespaceTable() {
        return namespaceTable;
    }

    public TableView<RDFClass> getClassesTable() {
        return classesTable;
    }

    public TableView<RDFProperty> getPropertiesTable() {
        return propertiesTable;
    }

    @FXML
    public void importOntology(ActionEvent event) {
        if (!urlFileImport.getText().trim().isEmpty()) {
            if (urlFileImport.getText().startsWith("http")) {
                try {
                    mainController.getRDFController().add(new URL(urlFileImport.getText()));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(OntologyTabController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                mainController.getRDFController().add(new File(urlFileImport.getText()));
            }
            mainController.getRDFController().getOntologies();
            mainController.getRDFController().getNamespaces();
            mainController.getRDFController().getClasses();
//            mainController.getRDFController().getProperties();
        }
    }

    @FXML
    public void browseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file;

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("OWL files ", "*.rdf", "*.ttl", "*.n3", "*.owl", "*.nt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show open file dialog
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            urlFileImport.setText(file.toString());
        }
    }
    
    @FXML
    public void addNamespace(ActionEvent event) {
        String prefix = namespacePrefixField.getText().trim();
        String ns = namespaceURIField.getText().trim();
        if (!ns.isEmpty() || !prefix.isEmpty()) {
            mainController.getRDFController().addNamespace(prefix, ns);
        }
    }
}
