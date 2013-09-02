/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFProperty;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFValue;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import jauzafx.scene.control.Dialogs;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class SesameRepoLookupController implements Initializable {

    private Stage stage;
    private MainController mainController;
    @FXML //  fx:id="resultListView"
    private ListView<RDFValue> resultListView; // Value injected by FXMLLoader
    private ObservableList<RDFValue> resultListRDF;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resultListRDF = FXCollections.observableList(new ArrayList<RDFValue>());
        resultListView.setItems(resultListRDF);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void search() {
        resultListRDF.clear();
        RDFProperty rdfProperty = mainController.getAnnotationTabController().getExtractionPanelController().getIndividualDetailsTable().getSelectionModel().getSelectedItem().getRdfProperty();
        List<RDFValue> allIndividual = mainController.getRDFController().getAllIndividualByTypeFromProperty(rdfProperty);
        if (allIndividual.isEmpty()) {
            stage.close();
            Dialogs.showInformationDialog(mainController.getPrimaryStage(), "No individual/value match with '" + rdfProperty.getLabel() + "' property from current sesame repository", "No individual/value match", "Value matching");
        } else {
            for (RDFValue value : allIndividual) {
                resultListRDF.add(new RDFValue(value.getUri(), value.getLabel()));
            }
        }
    }

    public void matchAction(ActionEvent event) {
        if (!resultListView.getSelectionModel().isEmpty()) {
            RDFValue value = resultListRDF.get(resultListView.getSelectionModel().getSelectedIndex());
            mainController.getAnnotationTabController().getExtractionPanelController().getIndividualDetailsTable().getSelectionModel().getSelectedItem().getRdfValue().setLabel(value.getLabel());
            mainController.getAnnotationTabController().getExtractionPanelController().getIndividualDetailsTable().getSelectionModel().getSelectedItem().getRdfValue().setUri(value.getUri());
        } else {
//            mainController.getAnnotationTabController().getExtractionPanelController().getIndividualDetailsTable().getSelectionModel().getSelectedItem().getRdfValue().setLabel("aaaaaaaaa");
//            mainController.getAnnotationTabController().getExtractionPanelController().getIndividualDetailsTable().getSelectionModel().getSelectedItem().getRdfValue().setUri("http://sesuatu.com/aaaaaa");
        }
        stage.close();
    }
}
