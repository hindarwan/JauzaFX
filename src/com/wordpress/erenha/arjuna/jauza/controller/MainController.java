/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.model.CurrentSelection;
import com.wordpress.erenha.arjuna.jauza.model.Individual;
import com.wordpress.erenha.arjuna.jauza.model.IndividualDetail;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class MainController implements Initializable {

    @FXML //  fx:id="current"
    private TableView<CurrentSelection> current; // Value injected by FXMLLoader
    @FXML //  fx:id="currentContent"
    private TableColumn<CurrentSelection, String> currentContent; // Value injected by FXMLLoader
    @FXML //  fx:id="currentID"
    private TableColumn<CurrentSelection, String> currentID; // Value injected by FXMLLoader
    @FXML //  fx:id="inv"
    private TableView<Individual> inv; // Value injected by FXMLLoader
    @FXML //  fx:id="invClass"
    private TableColumn<Individual, String> invClass; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetails"
    private TableView<IndividualDetail> invDetails; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetailsID"
    private TableColumn<IndividualDetail, String> invDetailsID; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetailsProperty"
    private TableColumn<IndividualDetail, String> invDetailsProperty; // Value injected by FXMLLoader
    @FXML //  fx:id="invDetailsValue"
    private TableColumn<IndividualDetail, String> invDetailsValue; // Value injected by FXMLLoader
    @FXML //  fx:id="invID"
    private TableColumn<Individual, String> invID; // Value injected by FXMLLoader
    @FXML //  fx:id="invSelected"
    private TableColumn<Individual, Boolean> invSelected; // Value injected by FXMLLoader
    //tabel model
    private ObservableList<CurrentSelection> currentSelections;
    private ObservableList<Individual> individuals;
    private ObservableList<IndividualDetail> individualDetails;
    //
    private final String INSPECT_SCRIPT = "javascript:(function() { var s = document.createElement('div'); s.innerHTML = 'Loading...'; s.style.color = 'black'; s.style.padding = '5px'; s.style.margin = '5px'; s.style.position = 'fixed'; s.style.zIndex = '9999'; s.style.fontSize = '24px'; s.style.border = '1px solid black'; s.style.right = '5px'; s.style.top = '5px'; s.setAttribute('class', 'selector_gadget_loading'); s.style.background = 'white'; document.body.appendChild(s); s = document.createElement('script'); s.setAttribute('type', 'text/javascript'); s.setAttribute('src', 'http://localhost/selectorgadgetCustom/lib/selectorgadgetNotSuggestion.js?raw=true'); document.body.appendChild(s); })();";
    @FXML
    private WebView webx;
    @FXML
    private TextField txt;
    @FXML //  fx:id="progress"
    private ProgressBar progress; // Value injected by FXMLLoader
    @FXML //  fx:id="status"
    private Label status; // Value injected by FXMLLoader
    @FXML //  fx:id="progressText"
    private Label progressText; // Value injected by FXMLLoader
    //NON FXML
    private WebEngine engine;
    //Class
    private ObservableList<String> classes = FXCollections.observableArrayList("Dataset", "Catalog", "Distribution", "Observation");
    private ObservableList<String> properties = FXCollections.observableArrayList("provinsi", "tahun", "jenisKelamin", "persentasePenduduk");

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert current != null : "fx:id=\"current\" was not injected: check your FXML file 'Main.fxml'.";
        assert currentContent != null : "fx:id=\"currentContent\" was not injected: check your FXML file 'Main.fxml'.";
        assert currentID != null : "fx:id=\"currentID\" was not injected: check your FXML file 'Main.fxml'.";
        assert inv != null : "fx:id=\"inv\" was not injected: check your FXML file 'Main.fxml'.";
        assert invClass != null : "fx:id=\"invClass\" was not injected: check your FXML file 'Main.fxml'.";
        assert invDetails != null : "fx:id=\"invDetails\" was not injected: check your FXML file 'Main.fxml'.";
        assert invDetailsID != null : "fx:id=\"invDetailsID\" was not injected: check your FXML file 'Main.fxml'.";
        assert invDetailsProperty != null : "fx:id=\"invDetailsProperty\" was not injected: check your FXML file 'Main.fxml'.";
        assert invDetailsValue != null : "fx:id=\"invDetailsValue\" was not injected: check your FXML file 'Main.fxml'.";
        assert invID != null : "fx:id=\"invID\" was not injected: check your FXML file 'Main.fxml'.";
        assert invSelected != null : "fx:id=\"invSelected\" was not injected: check your FXML file 'Main.fxml'.";
        assert progress != null : "fx:id=\"progress\" was not injected: check your FXML file 'Main.fxml'.";
        assert progressText != null : "fx:id=\"progressText\" was not injected: check your FXML file 'Main.fxml'.";
        assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'Main.fxml'.";
        assert txt != null : "fx:id=\"txt\" was not injected: check your FXML file 'Main.fxml'.";
        assert webx != null : "fx:id=\"webx\" was not injected: check your FXML file 'Main.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        // table
        initCurrent();
        initInv();
        initInvDetails();
        // table model
        initModel();
        // engine
        initEngine();

    }

    private void initModel() {
        currentSelections = FXCollections.observableList(new ArrayList<CurrentSelection>());
        current.setItems(currentSelections);
        individuals = FXCollections.observableList(new ArrayList<Individual>());
        inv.setItems(individuals);
        individualDetails = FXCollections.observableList(new ArrayList<IndividualDetail>());
        invDetails.setItems(individualDetails);
        inv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Individual>() {
            @Override
            public void changed(ObservableValue<? extends Individual> ov, Individual t, Individual t1) {
                individualDetails.clear();
                individualDetails.addAll(t1.getIndividualDetail());
            }
        });

    }

    private void initCurrent() {
        currentID.setCellValueFactory(new PropertyValueFactory("id"));
        currentContent.setCellValueFactory(new PropertyValueFactory("content"));
    }

    private void initInv() {
        invID.setCellValueFactory(new PropertyValueFactory("id"));
        invClass.setCellValueFactory(new PropertyValueFactory("typeOf"));
        invClass.setCellFactory(new Callback<TableColumn<Individual, String>, TableCell<Individual, String>>() {
            @Override
            public TableCell<Individual, String> call(TableColumn<Individual, String> p) {
                TableCell<Individual, String> cell = new ComboBoxTableCell<>(classes);
                return cell;
            }
        });
        invSelected.setCellValueFactory(new PropertyValueFactory("selected"));
        invSelected.setCellFactory(CheckBoxTableCell.forTableColumn(invSelected));
    }

    private void initInvDetails() {
        invDetailsID.setCellValueFactory(new PropertyValueFactory("id"));
        invDetailsProperty.setCellValueFactory(new PropertyValueFactory("property"));
        invDetailsValue.setCellValueFactory(new PropertyValueFactory("value"));
        invDetailsProperty.setCellFactory(new Callback<TableColumn<IndividualDetail, String>, TableCell<IndividualDetail, String>>() {
            @Override
            public TableCell<IndividualDetail, String> call(TableColumn<IndividualDetail, String> p) {
                TableCell<IndividualDetail, String> cell = new ComboBoxTableCell<>(properties);
                return cell;
            }
        });
    }

    @FXML
    public void go(ActionEvent event) {
        engine.load(txt.getText());
        System.out.println("[INFO] browser: go to " + txt.getText());
    }

    @FXML
    public void back(ActionEvent event) {
        JSObject history = (JSObject) engine.executeScript("history");
        history.call("back");
        System.out.println("[INFO] browser: back");
    }

    @FXML
    public void forward(ActionEvent event) {
        JSObject history = (JSObject) engine.executeScript("history");
        history.call("forward");
        System.out.println("[INFO] browser: forward");
    }

    @FXML
    public void inspect(ActionEvent event) {
        System.out.println("[INFO] inspect mode");
        engine.executeScript(INSPECT_SCRIPT);
    }

    //versi 2
    private void getSelectedElement() {
        currentSelections.clear();
        Integer length = (Integer) engine.executeScript("document.querySelectorAll('[class*=\\\"sg_selected\\\"]').length");
        System.out.println("[INFO] selected : " + length);
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                Element selectedElement = (Element) engine.executeScript("document.querySelectorAll('[class*=\\\"sg_selected\\\"]').item(" + i + ")");
                String id = selectedElement.getAttribute("jfxid");
                String content = selectedElement.getTextContent();
                String link = selectedElement.getAttribute("href");
                System.out.println("id : " + id);
                System.out.println("content : " + content);
                System.out.println("link : " + link);
                NodeList links = selectedElement.getElementsByTagName("a");
                for (int j = 0; j < links.getLength(); j++) {
                    Element l = (Element) links.item(j);
                    System.out.println("child link : " + l.getAttribute("href"));
                }
                currentSelections.add(new CurrentSelection(id, content.trim()));
            }
        }
    }

    @FXML
    public void onMouseClick(MouseEvent event) {
        getSelectedElement();
    }

    private void initEngine() {
        engine = webx.getEngine();
        engine.load("http://localhost/selectorgadget/");
        engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(final WebEvent<String> event) {
                status.setText(event.getData());
            }
        });

        engine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                txt.setText(t1);
//                selectedElement = "";
            }
        });
        engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                double d = newValue.doubleValue();
                progress.setProgress(d / 100);
                progressText.setText((int) d + "%");
            }
        });

        engine.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {
            @Override
            public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
                if (engine.getLoadWorker().getState() == Worker.State.FAILED) {
                    status.setText("Loading Error!!!");
                }
            }
        });


        engine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> e) {
                if (e.getData().equals("jauza:002")) { //clear selection
                    System.out.println("[INFO] clear selection");
                } else {
                }
            }
        });
    }

    @FXML
    public void test(ActionEvent event) {

        engine.executeScript("var a = document.querySelector('[jfxid=\"32\"]').getAttribute('class');"
                + "document.querySelector('[jfxid=\"32\"]').setAttribute('class', a + ' sg_selected');");
    }

    private void selectElementByJFXID(int id) {
        engine.executeScript("var a = document.querySelector('[jfxid=\"" + id + "\"]').getAttribute('class');"
                + "document.querySelector('[jfxid=\"" + id + "\"]').setAttribute('class', a + ' sg_selected');");
    }

    @FXML
    public void createInvAction(ActionEvent event) {
        int i = individuals.size();
        List<IndividualDetail> l = new ArrayList<>();
        for (CurrentSelection currentSelection : currentSelections) {
            l.add(new IndividualDetail(currentSelection.getId(), currentSelection.getContent(), ""));
        }
        individuals.add(new Individual("inv" + i, "", l));
    }
    private boolean step = false;
    private List<Integer> step1;

    @FXML
    public void suggestNextAction(ActionEvent event) {
        if (!step) {
            step1 = getStep();
        }
        List<IndividualDetail> individualDetail = individuals.get(individuals.size() - 1).getIndividualDetail();
        for (int i = 0; i < step1.size(); i++) {
            selectElementByJFXID(step1.get(i) + Integer.valueOf(individualDetail.get(i).getId()));
        }
        getSelectedElement();
        createInvAction(event);
    }

    private List<Integer> getStep() {
        List<Integer> step = new ArrayList<>();
        List<Integer> id1 = new ArrayList<>();
        List<Integer> id2 = new ArrayList<>();

        boolean sufficient = false;
        for (int i = 0; i < individuals.size(); i++) {
            if (individuals.get(i).isSelected()) {
                List<IndividualDetail> individualDetail = individuals.get(i).getIndividualDetail();
                for (IndividualDetail individualDetail1 : individualDetail) {
                    if (!sufficient) {
                        id1.add(Integer.valueOf(individualDetail1.getId()));
                    } else {
                        id2.add(Integer.valueOf(individualDetail1.getId()));
                    }
                }
                sufficient = true;
            }
        }
        for (int i = 0; i < id1.size(); i++) {
            step.add(id2.get(i) - id1.get(i));
        }
        this.step = true;
        return step;
    }
}