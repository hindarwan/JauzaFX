/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFValue;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class DbPediaLookupController implements Initializable {

    @FXML //  fx:id="resultListView"
    private ListView<String> resultListView; // Value injected by FXMLLoader
    @FXML //  fx:id="search_field"
    private TextField search_field; // Value injected by FXMLLoader
    private Stage stage;
    private MainController mainController;
    private ObservableList<String> resultList;
    private ObservableList<RDFValue> resultListRDF;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert search_field != null : "fx:id=\"search_field\" was not injected: check your FXML file 'dbPediaLookup.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

        resultList = FXCollections.observableList(new ArrayList<String>());
        resultListRDF = FXCollections.observableList(new ArrayList<RDFValue>());
        resultListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                final ListCell cell = new ListCell() {
                    private Text text;

                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        if (!isEmpty()) {
                            text = new Text(item.toString());
                            text.setWrappingWidth(resultListView.getPrefWidth() - 20);
                            setGraphic(text);
                        }
                    }
                };
                return cell;
            }
        });
        resultListView.setItems(resultList);
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void matchAction(ActionEvent event) {
        // handle the event here
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

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void searchAction(ActionEvent event) {
        // handle the event here
        search(search_field.getText());
    }

    public TextField getSearch_field() {
        return search_field;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void search(String keyword) {
        resultList.clear();
        resultListRDF.clear();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL("http://lookup.dbpedia.org/api/search/KeywordSearch?QueryString=" + keyword).openStream());
            NodeList result = doc.getElementsByTagName("Result");
            for (int i = 0; i < result.getLength(); i++) {
                Element item = (Element) result.item(i);
                String uri = item.getElementsByTagName("URI").item(0).getTextContent();
                String label = item.getElementsByTagName("Label").item(0).getTextContent();
                resultListRDF.add(new RDFValue(uri, label));
                resultList.add("Label : " + label + "\n"
                        + "URI : " + uri + "\n"
                        + "Description :\n" + item.getElementsByTagName("Description").item(0).getTextContent().trim());

            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DbPediaLookupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
