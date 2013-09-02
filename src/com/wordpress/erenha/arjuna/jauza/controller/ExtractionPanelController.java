/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFClass;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFIndividual;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFIndividualProperty;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFProperty;
import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFValue;
import com.wordpress.erenha.arjuna.jauza.util.StaticValue;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import jauzafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class ExtractionPanelController implements Initializable {

    @FXML //  fx:id="inv"
    private TableView<RDFIndividual> individualTable; // Value injected by FXMLLoader
    @FXML //  fx:id="invClass"
    private TableColumn<RDFIndividual, String> individualClassColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="invID"
    private TableColumn<RDFIndividual, String> individualIDColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetails"
    private TableView<RDFIndividualProperty> individualDetailsTable; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetailsProperty"
    private TableColumn<RDFIndividualProperty, String> individualDetailsPropertyColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetailsValue"
    private TableColumn<RDFIndividualProperty, String> individualDetailsValueColumn; // Value injected by FXMLLoader
    //NON FXML
    private MainController mainController;
    //tabel model
    private ObservableList<RDFIndividualProperty> individualDetails;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // table
        initInv();
        initInvDetails();
        // table model
        initModel();

    }

    public TableView<RDFIndividual> getIndividualTable() {
        return individualTable;
    }

    public TableView<RDFIndividualProperty> getIndividualDetailsTable() {
        return individualDetailsTable;
    }

    public ObservableList<RDFIndividualProperty> getIndividualDetails() {
        return individualDetails;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void initModel() {
        individualDetails = FXCollections.observableList(new ArrayList<RDFIndividualProperty>());
        individualDetailsTable.setItems(individualDetails);
        individualTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RDFIndividual>() {
            @Override
            public void changed(ObservableValue<? extends RDFIndividual> ov, RDFIndividual t, RDFIndividual t1) {
                individualDetails.clear();
                if (t1 != null) {
                    individualDetails.addAll(t1.getRdfIndividualProperty());
                    mainController.getRDFController().getPropertiesByClass(t1.getRdfClass());
                }

            }
        });
    }

    private void initInv() {
        individualIDColumn.setCellValueFactory(new PropertyValueFactory("uri"));
//        individualClassColumn.setCellValueFactory(new PropertyValueFactory("label"));
        individualClassColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RDFIndividual, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RDFIndividual, String> p) {
                StringProperty label = p.getValue().getRdfClass().labelProperty();
                return label;
            }
        });
    }

    private void initInvDetails() {
        individualDetailsPropertyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RDFIndividualProperty, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RDFIndividualProperty, String> p) {
                StringProperty label = p.getValue().getRdfProperty().labelProperty();
                return label;
            }
        });
//        individualDetailsValueColumn.setCellValueFactory(new PropertyValueFactory("propertyValue"));
        individualDetailsValueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RDFIndividualProperty, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RDFIndividualProperty, String> p) {
                StringProperty label = p.getValue().getRdfValue().labelProperty();
                return label;
            }
        });
        individualDetailsValueColumn.setCellFactory(new Callback<TableColumn<RDFIndividualProperty, String>, TableCell<RDFIndividualProperty, String>>() {
            @Override
            public TableCell<RDFIndividualProperty, String> call(TableColumn<RDFIndividualProperty, String> p) {
                StringConverter converter = new DefaultStringConverter();
                TableCell<RDFIndividualProperty, String> cell = new TextFieldTableCell<>(converter);
                return cell;
            }
        });
    }

    // TODO not handled exception
    @FXML
    public void createInvAction(ActionEvent event) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        Label idLabel = new Label("Identifier :");
        grid.add(idLabel, 0, 0);
        ToggleGroup groupID = new ToggleGroup();
        RadioButton genID = new RadioButton("Use generated ID");
        final RadioButton givenID = new RadioButton("Use given ID");
        genID.setToggleGroup(groupID);
        genID.selectedProperty().set(true);
        givenID.setToggleGroup(groupID);
        grid.add(genID, 0, 1);
        grid.add(givenID, 0, 2);
        final TextField givenIDField = new TextField();
        givenIDField.disableProperty().set(true);
        grid.add(givenIDField, 0, 3);

        givenID.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (givenID.isSelected()) {
                    givenIDField.disableProperty().set(false);
                } else {
                    givenIDField.disableProperty().set(true);
                }
            }
        });
        Label typeLabel = new Label("Type of Individual :");
        grid.add(typeLabel, 0, 4);
//        Collections.sort(mainController.getCurrentClassesLabel());
//        final ComboBox<String> type = new ComboBox<>(mainController.getCurrentClassesLabel());
        final ComboBox<RDFClass> type = new ComboBox<>(mainController.getCurrentClassesLabel());
        type.setPromptText("<<Choose Type>>");
        grid.add(type, 0, 5);

        final List<Object> lst = new ArrayList<>();
        lst.add(0, "null");
        lst.add(1, "null");

        Callback<Void, Void> myCallback = new Callback<Void, Void>() {
            @Override
            public Void call(Void param) {
                //                String selectedItem = type.getSelectionModel().getSelectedItem();
                RDFClass selectedItem = type.getSelectionModel().getSelectedItem();
                lst.add(0, selectedItem);
                if (givenID.isSelected() && !givenIDField.getText().isEmpty()) {
                    lst.add(1, givenIDField.getText());
                } else {
//                    lst.add(1, selectedItem.substring(selectedItem.indexOf(":") + 1) + System.currentTimeMillis());
                    lst.add(1, selectedItem.toString().replaceAll(" ", "_") + UUID.randomUUID().toString().replaceAll("-", ""));
                }
                return null;
            }
        };
        Dialogs.showCustomDialog(mainController.getPrimaryStage(), grid, StaticValue.annotation_create_individual, StaticValue.annotation_create_individual_title, Dialogs.DialogOptions.OK, myCallback);
        if (!lst.get(0).equals("null") || !lst.get(1).equals("null")) {
            //            RDFClass rdfClass = new RDFClass(mainController.getRDFController().toNamespaceFull(lst.get(0)), lst.get(0));
            RDFClass rdfClass = (RDFClass) lst.get(0);
            List<RDFIndividualProperty> l = new ArrayList<>();
            mainController.getRDFController().getPropertiesByClass(rdfClass);
//            for (String string : mainController.getCurrentPropertiesLabel()) {
//                RDFIndividualProperty rdfIndividualProperty = new RDFIndividualProperty(new RDFProperty(mainController.getRDFController().toNamespaceFull(string), string), "");
//                l.add(rdfIndividualProperty);
//            }
            for (RDFProperty string : mainController.getCurrentPropertiesLabel()) {
                RDFIndividualProperty rdfIndividualProperty = new RDFIndividualProperty(string, new RDFValue("", ""));
                l.add(rdfIndividualProperty);
            }
            RDFIndividual rdfIndividual = new RDFIndividual(lst.get(1).toString(), rdfClass, l);
            mainController.getCurrentIndividuals().add(rdfIndividual);
            individualTable.getSelectionModel().select(rdfIndividual);
//            mainController.getAnnotationTabController().getBrowserController().initGetCurrentSelectedElement();
        }
    }

    @FXML
    public void addPropertyAction(ActionEvent event) {
        ObservableList<String> pilihan = FXCollections.observableArrayList("From sesame repository", "From dbPedia");
        String source = Dialogs.showInputDialog(mainController.getPrimaryStage(), "Choose Resource Repository", "Match value with existing resource", "Match value", pilihan.get(0), pilihan);
        switch (source) {
            case "From sesame repository":
                sesameRepository();
                break;
            case "From dbPedia":
                dbPedia();
                break;
        }

    }

    private void sesameRepository() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sesameRepoLookup.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            final SesameRepoLookupController controller = fxmlLoader.getController();
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(mainController.getPrimaryStage());
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.setResizable(false);
            controller.setStage(stage);
            controller.setMainController(mainController);
            stage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    controller.search();
                }
            });

            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(ExtractionPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void dbPedia() {
        RDFIndividualProperty selectedItem = individualDetailsTable.getSelectionModel().getSelectedItem();
        String valueToMatch = selectedItem.getRdfValue().getLabel();
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dbPediaLookup.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            final DbPediaLookupController controller = fxmlLoader.getController();
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(mainController.getPrimaryStage());
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.setResizable(false);
            controller.getSearch_field().setText(valueToMatch);
            controller.setStage(stage);
            controller.setMainController(mainController);
            stage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    controller.searchAction(null);
                }
            });

            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(ExtractionPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void userInput() {
        final GridPane grid2 = new GridPane();
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(0, 10, 0, 10));
        Label valueCustomLabel = new Label("User Input");
        grid2.add(valueCustomLabel, 0, 0);
//        final TextField givenIDField = new TextField();
//        grid2.add(givenIDField, 0, 1);

        final TextArea givenIDField = new TextArea();
        final List<String> lst = new ArrayList<>();
        lst.add(0, "null");

        Callback<Void, Void> myCallback = new Callback<Void, Void>() {
            @Override
            public Void call(Void param) {
                lst.add(0, givenIDField.getText());
                return null;
            }
        };
        grid2.add(givenIDField, 0, 1);
        Dialogs.showCustomDialog(mainController.getPrimaryStage(), grid2, "Enter value fo", "Add Property", Dialogs.DialogOptions.OK_CANCEL, myCallback);
    }

    public void deleteSelectedIndividualAction(ActionEvent event) {
        RDFIndividual selectedItem = individualTable.getSelectionModel().getSelectedItem();
//        individualTable.getSelectionModel().  
        mainController.getCurrentIndividuals().remove(selectedItem);

    }

    public void deleteSelectedIndividualDetailsAction(ActionEvent event) {
        RDFIndividualProperty selectedItem = individualDetailsTable.getSelectionModel().getSelectedItem();
        individualDetails.remove(selectedItem);
        individualTable.getSelectionModel().getSelectedItem().getRdfIndividualProperty().remove(selectedItem);

    }
    /*
     * Suggestion
     */
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
