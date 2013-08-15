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
import com.wordpress.erenha.arjuna.jauza.util.StaticValue;
import com.wordpress.erenha.arjuna.jauza.view.wizard.DemoWizard;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

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
    @FXML //  fx:id="valueSourceBox"
    private ChoiceBox<String> valueSourceBox; // Value injected by FXMLLoader
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
        ObservableList<String> pilihan = FXCollections.observableArrayList(
                "current page",
                "user input",
                "sesame repository",
                "dbPedia");

        valueSourceBox.setItems(pilihan);
        valueSourceBox.getSelectionModel().selectFirst();
    }

//    public TableView<CurrentSelection> getCurrentSelectionTable() {
//        return currentSelectionTable;
//    }
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
                    mainController.getRDFController().getPropertiesByClass(t1.getRdfClass().getUri());
                }

            }
        });
    }

    private void initInv() {
        individualIDColumn.setCellValueFactory(new PropertyValueFactory("uri"));
        individualClassColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RDFIndividual, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RDFIndividual, String> p) {
                StringProperty label = p.getValue().getRdfClass().labelProperty();
                if (label.getValue().equals("<<Choose Class>>")) {
                    p.getValue().getRdfClass().setUri("rdfs:Class");
                } else {
                    p.getValue().getRdfClass().setUri(mainController.getRDFController().toNamespaceFull(label.getValue()));
                }
                return label;
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
    }

    private void initInvDetails() {
//        individualDetailsIDColumn.setCellValueFactory(new PropertyValueFactory("id"));
        individualDetailsPropertyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RDFIndividualProperty, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RDFIndividualProperty, String> p) {
                StringProperty label = p.getValue().getRdfProperty().labelProperty();
                p.getValue().getRdfProperty().setUri(label.getValue());

                if (label.getValue().equals("<<Choose Property>>")) {
                    p.getValue().getRdfProperty().setUri("rdf:Property");
                } else {
                    p.getValue().getRdfProperty().setUri(mainController.getRDFController().toNamespaceFull(label.getValue()));
                }
                return label;
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

    // TODO not handled exception
    @FXML
    public void createInvAction(ActionEvent event) {
        List<RDFIndividualProperty> l = new ArrayList<>();
        for (CurrentSelection currentSelection : mainController.getCurrentSelections()) {
            l.add(new RDFIndividualProperty(new RDFProperty("rdf:Property", "<<Choose Property>>"), currentSelection.getContent()));
        }
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
        Collections.sort(mainController.getCurrentClassesLabel());
        final ComboBox<String> type = new ComboBox<>(mainController.getCurrentClassesLabel());
        type.setPromptText("<<Choose Type>>");
        grid.add(type, 0, 5);

        final List<String> lst = new ArrayList<>();
        lst.add(0, "null");
        lst.add(1, "null");

        Callback<Void, Void> myCallback = new Callback<Void, Void>() {
            @Override
            public Void call(Void param) {
                String selectedItem = type.getSelectionModel().getSelectedItem();
                lst.add(0, selectedItem);
                if (givenID.isSelected() && !givenIDField.getText().isEmpty()) {
                    lst.add(1, givenIDField.getText());
                } else {
                    lst.add(1, selectedItem.substring(selectedItem.indexOf(":") + 1) + System.currentTimeMillis());
                }
                return null;
            }
        };
        Dialogs.showCustomDialog(mainController.getPrimaryStage(), grid, StaticValue.annotation_create_individual, StaticValue.annotation_create_individual_title, Dialogs.DialogOptions.OK, myCallback);
        if (!lst.get(0).equals("null") || !lst.get(1).equals("null")) {
            RDFIndividual rdfIndividual = new RDFIndividual(lst.get(1), new RDFClass(mainController.getRDFController().toNamespaceFull(lst.get(0)), lst.get(0)), l);
            mainController.getCurrentIndividuals().add(rdfIndividual);
            individualTable.getSelectionModel().select(rdfIndividual);
        }
    }

    @FXML
    public void addPropertyAction1(ActionEvent event) {
//        DemoWizard dw = new DemoWizard(null);
        VBox vBox = new VBox(5.0);
        GridPane grid1 = new GridPane();
        grid1.setHgap(10);
        grid1.setVgap(10);
        grid1.setPadding(new Insets(0, 10, 0, 10));
        Label typeLabel = new Label("Property");
        grid1.add(typeLabel, 0, 0);
        Label valueLabel = new Label("Value");
        grid1.add(valueLabel, 1, 0);
        Collections.sort(mainController.getCurrentPropertiesLabel());
        final ComboBox<String> type = new ComboBox<>(mainController.getCurrentPropertiesLabel());
        type.setMinWidth(160.0);
        type.setPromptText("<<Choose Property>>");
        grid1.add(type, 0, 1);

        ObservableList<String> pilihan = FXCollections.observableArrayList(
                "From user input",
                "From current page",
                "From sesame repository",
                "From dbPedia");

        final ComboBox<String> typeValue = new ComboBox<>(pilihan);
//        typeValue.getSelectionModel().selectFirst();
        typeValue.setMinWidth(160.0);

        grid1.add(typeValue, 1, 1);

        final VBox vBox2 = new VBox();
        final GridPane grid2 = new GridPane();
        grid2.setHgap(10);
        grid2.setVgap(10);
        grid2.setPadding(new Insets(0, 10, 0, 10));
        Label valueCustomLabel = new Label("User Input");
        grid2.add(valueCustomLabel, 0, 0);
//        final TextField givenIDField = new TextField();
//        grid2.add(givenIDField, 0, 1);

        final TextArea givenIDField = new TextArea();
        grid2.add(givenIDField, 0, 1);
//        vBox2.getChildren().add(grid2);

        final List<String> lst = new ArrayList<>();
        lst.add(0, "null");
        lst.add(1, "null");

        Callback<Void, Void> myCallback = new Callback<Void, Void>() {
            @Override
            public Void call(Void param) {
                String selectedItem = type.getSelectionModel().getSelectedItem();
                lst.add(0, selectedItem);
//                lst.add(1, givenIDField.getText());
                return null;
            }
        };

        typeValue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (!"From user input".equals(t1)) {
                    vBox2.getChildren().clear();
                } else {
                    vBox2.getChildren().clear();
                    vBox2.getChildren().add(grid2);
                }
            }
        });

        vBox.getChildren().addAll(grid1, vBox2);
//        Dialogs.showCustomDialog(mainController.getPrimaryStage(), dw, "Add Property", "Add Property", Dialogs.DialogOptions.OK, myCallback);
        Dialogs.showCustomDialog(mainController.getPrimaryStage(), vBox, "Add Property", "Add Property", Dialogs.DialogOptions.OK_CANCEL, myCallback);
        if (!lst.get(0).equals("null") || !lst.get(1).equals("null")) {
            RDFIndividualProperty rdfIndividualProperty = new RDFIndividualProperty(new RDFProperty(lst.get(0), lst.get(0)), lst.get(1));
            individualDetails.add(rdfIndividualProperty);
            individualDetailsTable.getSelectionModel().select(rdfIndividualProperty);
            individualTable.getSelectionModel().getSelectedItem().getRdfIndividualProperty().add(rdfIndividualProperty);

        }
    }

    @FXML
    public void addPropertyAction2(ActionEvent event) {
//        String property = Dialogs.showInputDialog(mainController.getPrimaryStage(), "Choose Property", "Add Property", "Add Property", null, mainController.getCurrentPropertiesLabel());
//        String method = Dialogs.showInputDialog(mainController.getPrimaryStage(), "From", "Add Property", "Add Property", null, mainController.getCurrentPropertiesLabel());
        GridPane grid1 = new GridPane();
        grid1.setHgap(10);
        grid1.setVgap(10);
        grid1.setPadding(new Insets(0, 10, 0, 10));
        Label typeLabel = new Label("Property");
        grid1.add(typeLabel, 0, 0);
        Label valueLabel = new Label("Value Source");
        grid1.add(valueLabel, 1, 0);
        Collections.sort(mainController.getCurrentPropertiesLabel());
        final ComboBox<String> type = new ComboBox<>(mainController.getCurrentPropertiesLabel());
        type.setMinWidth(160.0);
        type.setPromptText("<<Choose Property>>");
        grid1.add(type, 0, 1);

        ObservableList<String> pilihan = FXCollections.observableArrayList(
                "From current page",
                "From user input",
                "From sesame repository",
                "From dbPedia");

        final ComboBox<String> typeValue = new ComboBox<>(pilihan);
        typeValue.getSelectionModel().selectFirst();
        typeValue.setMinWidth(160.0);
        grid1.add(typeValue, 1, 1);


        final List<String> lst = new ArrayList<>();
        lst.add(0, "null");
        lst.add(1, "null");

        Callback<Void, Void> myCallback = new Callback<Void, Void>() {
            @Override
            public Void call(Void param) {
                String typeSel = type.getSelectionModel().getSelectedItem();
                lst.add(0, typeSel);
                String typeValueSel = typeValue.getSelectionModel().getSelectedItem();
                lst.add(1, typeValueSel);
                return null;
            }
        };
        Dialogs.DialogResponse respon = Dialogs.showCustomDialog(mainController.getPrimaryStage(), grid1, "Add Property", "Add Property", Dialogs.DialogOptions.OK_CANCEL, myCallback);
        if (respon == Dialogs.DialogResponse.OK && !lst.get(0).equals("null") || !lst.get(1).equals("null")) {
            switch (lst.get(1)) {
                case "From current page":
                    mainController.getAnnotationTabController().getBrowserController().initGetCurrentSelectedElement(lst.get(0));
                    break;
                case "From user input":
                    String userInput = Dialogs.showInputDialog(mainController.getPrimaryStage(), "User Input", "Give a suitable value for '" + lst.get(0) + "'property", "User Input");
                    if (!userInput.trim().isEmpty()) {
                        RDFIndividualProperty rdfIndividualProperty = new RDFIndividualProperty(new RDFProperty(lst.get(0), lst.get(0)), userInput.trim());
                        individualDetails.add(rdfIndividualProperty);
                        individualDetailsTable.getSelectionModel().select(rdfIndividualProperty);
                        individualTable.getSelectionModel().getSelectedItem().getRdfIndividualProperty().add(rdfIndividualProperty);
                    }
                    break;
                case "From sesame repository":
                    break;
                case "From dbPedia":
                    break;
            }
        }
    }

    @FXML
    public void addPropertyAction(ActionEvent event) {
        String property = Dialogs.showInputDialog(mainController.getPrimaryStage(), "Choose Property", "Choose property that you want to add", "Add Property", mainController.getCurrentPropertiesLabel().get(0), mainController.getCurrentPropertiesLabel());
        switch (valueSourceBox.getValue()) {
            case "current page":
                mainController.getAnnotationTabController().getBrowserController().initGetCurrentSelectedElement(property);
                break;
            case "user input":
                String userInput = Dialogs.showInputDialog(mainController.getPrimaryStage(), "User Input", "Give a suitable value for '" + property + "'property", "User Input");
                if (!userInput.trim().isEmpty()) {
                    RDFIndividualProperty rdfIndividualProperty = new RDFIndividualProperty(new RDFProperty(property, property), userInput.trim());
                    individualDetails.add(rdfIndividualProperty);
                    individualDetailsTable.getSelectionModel().select(rdfIndividualProperty);
                    individualTable.getSelectionModel().getSelectedItem().getRdfIndividualProperty().add(rdfIndividualProperty);
                }
                break;
            case "sesame repository":
                break;
            case "dbPedia":
                break;
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
