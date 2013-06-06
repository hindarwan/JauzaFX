/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.rdf.RDFClass;
import com.wordpress.erenha.arjuna.jauza.rdf.RDFProperty;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
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
        assert classesLabelColumn != null : "fx:id=\"classesLabelColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert classesTable != null : "fx:id=\"classesTable\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert classesURIColumn != null : "fx:id=\"classesURIColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert propertiesLabelColumn != null : "fx:id=\"propertiesLabelColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert propertiesTable != null : "fx:id=\"propertiesTable\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert propertiesURIColumn != null : "fx:id=\"propertiesURIColumn\" was not injected: check your FXML file 'ontologyTab.fxml'.";
        assert urlFileImport != null : "fx:id=\"urlFileImport\" was not injected: check your FXML file 'ontologyTab.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        initTableColumn();

    }

    private void initTableColumn() {
        classesLabelColumn.setCellValueFactory(new PropertyValueFactory("label"));
        classesURIColumn.setCellValueFactory(new PropertyValueFactory("uri"));
        propertiesLabelColumn.setCellValueFactory(new PropertyValueFactory("label"));
        propertiesURIColumn.setCellValueFactory(new PropertyValueFactory("uri"));
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
            mainController.getRepository().add(new File(urlFileImport.getText()));
            mainController.getRepository().getClasses();
            mainController.getRepository().getProperties();
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
}
