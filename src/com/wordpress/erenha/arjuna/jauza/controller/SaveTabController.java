/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFIndividual;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFIndividualProperty;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class SaveTabController implements Initializable {

    @FXML //  fx:id="IndividualDetailsPropertyColumn"
    private TableColumn<RDFIndividualProperty, String> individualDetailsPropertyColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="IndividualDetailsValueColumn"
    private TableColumn<RDFIndividualProperty, String> individualDetailsValueColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="IndividualIDColumn"
    private TableColumn<RDFIndividual, String> individualIDColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="IndividualTypeColumn"
    private TableColumn<RDFIndividual, String> individualTypeColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="individualDetailsTable"
    private TableView<RDFIndividualProperty> individualDetailsTable; // Value injected by FXMLLoader
    @FXML //  fx:id="individualTable"
    private TableView<RDFIndividual> individualTable; // Value injected by FXMLLoader
    private MainController mainController;
    private ObservableList<RDFIndividualProperty> individualDetails;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public TableView<RDFIndividual> getIndividualTable() {
        return individualTable;
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert individualDetailsPropertyColumn != null : "fx:id=\"IndividualDetailsPropertyColumn\" was not injected: check your FXML file 'saveTab.fxml'.";
        assert individualDetailsValueColumn != null : "fx:id=\"IndividualDetailsValueColumn\" was not injected: check your FXML file 'saveTab.fxml'.";
        assert individualIDColumn != null : "fx:id=\"IndividualIDColumn\" was not injected: check your FXML file 'saveTab.fxml'.";
        assert individualTypeColumn != null : "fx:id=\"IndividualTypeColumn\" was not injected: check your FXML file 'saveTab.fxml'.";
        assert individualDetailsTable != null : "fx:id=\"individualDetailsTable\" was not injected: check your FXML file 'saveTab.fxml'.";
        assert individualTable != null : "fx:id=\"individualTable\" was not injected: check your FXML file 'saveTab.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        initModel();
        initIndividual();
        initIndividualDetails();
    }

    private void initModel() {
        individualDetails = FXCollections.observableList(new ArrayList<RDFIndividualProperty>());
        individualDetailsTable.setItems(individualDetails);
        individualTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RDFIndividual>() {
            @Override
            public void changed(ObservableValue<? extends RDFIndividual> ov, RDFIndividual t, RDFIndividual t1) {
                individualDetails.clear();
                individualDetails.addAll(t1.getRdfIndividualProperty());
            }
        });

    }

    private void initIndividual() {
        individualIDColumn.setCellValueFactory(new PropertyValueFactory("uri"));
        individualTypeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RDFIndividual, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RDFIndividual, String> p) {
                return p.getValue().getRdfClass().labelProperty();
            }
        });
        individualTypeColumn.setCellFactory(new Callback<TableColumn<RDFIndividual, String>, TableCell<RDFIndividual, String>>() {
            @Override
            public TableCell<RDFIndividual, String> call(TableColumn<RDFIndividual, String> p) {
                Collections.sort(mainController.getCurrentClassesLabel());
                TableCell<RDFIndividual, String> cell = new ComboBoxTableCell<>(mainController.getCurrentClassesLabel());
                return cell;
            }
        });
//        individualSelectedColumn.setCellValueFactory(new PropertyValueFactory("selected"));
//        individualSelectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(individualSelectedColumn));
    }

    private void initIndividualDetails() {
//        individualDetailsIDColumn.setCellValueFactory(new PropertyValueFactory("id"));
        individualDetailsPropertyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RDFIndividualProperty, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RDFIndividualProperty, String> p) {
                return p.getValue().getRdfProperty().labelProperty();
            }
        });
        individualDetailsValueColumn.setCellValueFactory(new PropertyValueFactory("propertyValue"));
        individualDetailsPropertyColumn.setCellFactory(new Callback<TableColumn<RDFIndividualProperty, String>, TableCell<RDFIndividualProperty, String>>() {
            @Override
            public TableCell<RDFIndividualProperty, String> call(TableColumn<RDFIndividualProperty, String> p) {
                Collections.sort(mainController.getCurrentPropertiesLabel());
                TableCell<RDFIndividualProperty, String> cell = new ComboBoxTableCell<>(mainController.getCurrentPropertiesLabel());
                return cell;
            }
        });
    }

    public void saveAllIndividual(ActionEvent event) {
        // handle the event here
        mainController.getRDFController().saveAllIndividual();
    }
}
