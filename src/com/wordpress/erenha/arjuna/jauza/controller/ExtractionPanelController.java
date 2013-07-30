/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.model.CurrentSelection;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFClass;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFIndividual;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFIndividualProperty;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFProperty;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
public class ExtractionPanelController implements Initializable {

    @FXML //  fx:id="current"
    private TableView<CurrentSelection> currentSelectionTable; // Value injected by FXMLLoader
    @FXML //  fx:id="currentContent"
    private TableColumn<CurrentSelection, String> currentSelectionContentColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="currentID"
    private TableColumn<CurrentSelection, String> currentSelectionIDColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="inv"
    private TableView<RDFIndividual> individualTable; // Value injected by FXMLLoader
    @FXML //  fx:id="invClass"
    private TableColumn<RDFIndividual, String> individualClassColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="invID"
    private TableColumn<RDFIndividual, String> individualIDColumn; // Value injected by FXMLLoader
//    @FXML //  fx:id="invSelected"
//    private TableColumn<Individual, Boolean> individualSelectedColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetails"
    private TableView<RDFIndividualProperty> individualDetailsTable; // Value injected by FXMLLoader
//    @FXML //  fx:id="invDetailsID"
//    private TableColumn<RDFIndividualProperty, String> individualDetailsIDColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetailsProperty"
    private TableColumn<RDFIndividualProperty, String> individualDetailsPropertyColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetailsValue"
    private TableColumn<RDFIndividualProperty, String> individualDetailsValueColumn; // Value injected by FXMLLoader
    //NON FXML
    private MainController mainController;
    //tabel model
//    private ObservableList<CurrentSelection> currentSelections;
    private ObservableList<RDFIndividual> individuals;
    private ObservableList<RDFIndividualProperty> individualDetails;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // table
        initCurrent();
        initInv();
        initInvDetails();
        // table model
        initModel();

    }

    public TableView<CurrentSelection> getCurrentSelectionTable() {
        return currentSelectionTable;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void initModel() {
        individuals = FXCollections.observableList(new ArrayList<RDFIndividual>());
        individualTable.setItems(individuals);
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

    private void initCurrent() {
        currentSelectionIDColumn.setCellValueFactory(new PropertyValueFactory("id"));
        currentSelectionContentColumn.setCellValueFactory(new PropertyValueFactory("content"));
    }

    private void initInv() {
        individualIDColumn.setCellValueFactory(new PropertyValueFactory("uri"));
        individualClassColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RDFIndividual, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RDFIndividual, String> p) {
                return p.getValue().getRdfClass().labelProperty();
            }
        });
        individualClassColumn.setCellFactory(new Callback<TableColumn<RDFIndividual, String>, TableCell<RDFIndividual, String>>() {

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

    private void initInvDetails() {
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

    @FXML
    public void createInvAction(ActionEvent event) {
        int i = individuals.size();
        List<RDFIndividualProperty> l = new ArrayList<>();
        for (CurrentSelection currentSelection : mainController.getCurrentSelections()) {
            l.add(new RDFIndividualProperty(new RDFProperty("rdf:Property", "<<Choose Property>>"), currentSelection.getContent()));
        }
        individuals.add(new RDFIndividual("individual" + i,new RDFClass("rdfs:Class", "<<Choose Class>>"),l));
//        individuals.add(new Individual("individual" + i, "<<Choose Class>>", l));
    }
    private boolean step = false;
    private List<Integer> step1;

    @FXML
    public void suggestNextAction(ActionEvent event) {
//        if (!step) {
//            step1 = getStep();
//        }
//        List<IndividualDetail> individualDetail = individuals.get(individuals.size() - 1).getIndividualDetail();
        for (int i = 0; i < step1.size(); i++) {
//            selectElementByJFXID(step1.get(i) + Integer.valueOf(individualDetail.get(i).getId()));
        }
//        getSelectedElement();
        createInvAction(event);
    }

    private List<Integer> getStep() {
        List<Integer> step = new ArrayList<>();
        List<Integer> id1 = new ArrayList<>();
        List<Integer> id2 = new ArrayList<>();

//        boolean sufficient = false;
//        for (int i = 0; i < individuals.size(); i++) {
//            if (individuals.get(i).isSelected()) {
//                List<IndividualDetail> individualDetail = individuals.get(i).getIndividualDetail();
//                for (IndividualDetail individualDetail1 : individualDetail) {
//                    if (!sufficient) {
//                        id1.add(Integer.valueOf(individualDetail1.getId()));
//                    } else {
//                        id2.add(Integer.valueOf(individualDetail1.getId()));
//                    }
//                }
//                sufficient = true;
//            }
//        }
//        for (int i = 0; i < id1.size(); i++) {
//            step.add(id2.get(i) - id1.get(i));
//        }
//        this.step = true;
        return step;
    }
}
