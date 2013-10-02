/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.controller;

import com.wordpress.erenha.arjuna.jauza.rdf.model.RDFProperty;
import com.wordpress.erenha.arjuna.jauza.util.StaticValue;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import jauzafx.scene.control.Dialogs;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;

/**
 * FXML Controller class
 *
 * @author Hindarwan
 */
public class BrowserController implements Initializable {

    @FXML //  fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader
    @FXML //  fx:id="forwardButton"
    private Button forwardButton; // Value injected by FXMLLoader
    @FXML //  fx:id="goButton"
    private Button goButton; // Value injected by FXMLLoader
    @FXML //  fx:id="inspectButton"
    private ToggleButton inspectButton; // Value injected by FXMLLoader
    @FXML //  fx:id="progress"
    private ProgressBar progress; // Value injected by FXMLLoader
    @FXML //  fx:id="progressText"
    private Label progressText; // Value injected by FXMLLoader
    @FXML //  fx:id="status"
    private Label status; // Value injected by FXMLLoader
    @FXML //  fx:id="txt"
    private TextField txt; // Value injected by FXMLLoader
    @FXML //  fx:id="webx"
    private WebView webx; // Value injected by FXMLLoader
    //NON FXML
    private final String INSPECT_SCRIPT = "javascript:(function() { var s = document.createElement('div'); s.innerHTML = 'Loading...'; s.style.color = 'black'; s.style.padding = '5px'; s.style.margin = '5px'; s.style.position = 'fixed'; s.style.zIndex = '9999'; s.style.fontSize = '24px'; s.style.border = '1px solid black'; s.style.right = '5px'; s.style.top = '5px'; s.setAttribute('class', 'selector_gadget_loading'); s.style.background = 'white'; document.body.appendChild(s); s = document.createElement('script'); s.setAttribute('type', 'text/javascript'); s.setAttribute('src', '" + StaticValue.baseURL + "/selectorgadgetCustom/lib/selectorgadgetNotSuggestion.js?raw=true'); document.body.appendChild(s); })();";
    private WebEngine engine;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public TextField getTxt() {
        return txt;
    }

    public ToggleButton getInspectButton() {
        return inspectButton;
    }

    public void load(String url) {
        engine.load(url);
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'browser.fxml'.";
        assert forwardButton != null : "fx:id=\"forwardButton\" was not injected: check your FXML file 'browser.fxml'.";
        assert goButton != null : "fx:id=\"goButton\" was not injected: check your FXML file 'browser.fxml'.";
        assert inspectButton != null : "fx:id=\"inspectButton\" was not injected: check your FXML file 'browser.fxml'.";
        assert progress != null : "fx:id=\"progress\" was not injected: check your FXML file 'browser.fxml'.";
        assert progressText != null : "fx:id=\"progressText\" was not injected: check your FXML file 'browser.fxml'.";
        assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'browser.fxml'.";
        assert txt != null : "fx:id=\"txt\" was not injected: check your FXML file 'browser.fxml'.";
        assert webx != null : "fx:id=\"webx\" was not injected: check your FXML file 'browser.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        // engine
//        Config.read();
        initEngine();

    }
    private String id;
    private String content;

    @FXML
    public void mouseClick(MouseEvent event) {
        if (popup.isShowing()) {
            popup.hide();
            clearSelectElement();
        }
        if (inspectButton.isSelected()) {
            if (!content.isEmpty()) {
                showPopupProperty(id, content, event);
            }else{
                clearSelectElement();
            }
        }
    }

    @FXML
    public void go(ActionEvent event) {
        URL url = null;
        try {
            url = new URL(txt.getText());
        } catch (MalformedURLException ex) {
            try {
                url = new URL("http://" + txt.getText());
            } catch (MalformedURLException ex1) {
                Logger.getLogger(BrowserController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        if (url != null) {
            engine.load(url.toString());
            System.out.println("[INFO] browser: go to " + url.toString());
        }
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
        if (popup.isShowing()) {
            popup.hide();
            clearSelectElement();
        }
        if (inspectButton.isSelected()) {
            System.out.println("[INFO] inspect mode");
            engine.executeScript(INSPECT_SCRIPT);
            backButton.setDisable(true);
            forwardButton.setDisable(true);
            txt.setDisable(true);
            goButton.setDisable(true);
            inspectButton.setText("Finish Annotation");
        } else {
            if (!mainController.getCurrentIndividuals().isEmpty()) {
                Dialogs.DialogResponse confirm = Dialogs.showConfirmDialog(mainController.getPrimaryStage(), "Do you want to save individual to repository?", "Save Individual", "Save Individual", Dialogs.DialogOptions.YES_NO);
                if (confirm == Dialogs.DialogResponse.YES) {
                    mainController.getRDFController().saveAllIndividual();
                }
                mainController.getCurrentIndividuals().clear();
            }

            System.out.println("[INFO] browser mode");
            engine.executeScript(INSPECT_SCRIPT);
            backButton.setDisable(false);
            forwardButton.setDisable(false);
            txt.setDisable(false);
            goButton.setDisable(false);
            inspectButton.setText("Start Annotation");
        }
    }

    public void hidePopup(MouseEvent event) {
        if (popup.isShowing()) {
            popup.hide();
            clearSelectElement();
        }
    }

    private void initEngine() {
        engine = webx.getEngine();
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
            }
        });
        engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                double d = newValue.doubleValue();
                progress.setProgress(d / 100);
                progressText.setText((int) d + "%");
                if (d == 100) {
                    progress.setVisible(false);
                    progressText.setVisible(false);
                } else {
                    progress.setVisible(true);
                    progressText.setVisible(true);
                }
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
            //Annotation begin here
            @Override
            public void handle(WebEvent<String> e) {
                if (e.getData().equals("jauza:002")) { //clear selection
                    System.out.println("[INFO] clear selection");
                } else {
                    System.out.println("[INFO] jfxid selected:" + e.getData());
                    id = e.getData();
                    content = contentInJFXID(e.getData());
                }
            }
        });
    }

    @FXML
    public void test(ActionEvent event) {
        clearSelectElement();
    }

    private void selectElementByJFXID(int id) {
        engine.executeScript("var a = document.querySelector('[jfxid=\"" + id + "\"]').getAttribute('class');"
                + "document.querySelector('[jfxid=\"" + id + "\"]').setAttribute('class', a + ' sg_selected');");
    }

    private void deSelectElementByJFXID(int id) {
        engine.executeScript("var a = document.querySelector('[jfxid=\"" + id + "\"]').getAttribute('class').replace('sg_selected','');"
                + "document.querySelector('[jfxid=\"" + id + "\"]').setAttribute('class', a);");
    }

    private void clearSelectElement() {
        engine.executeScript("var node = document.querySelectorAll('[class*=\\\"sg_selected\\\"]');"
                + "for(var i = 0, l = node.length; i < l; i++ ){"
                + "    node[i].classList.remove(\"sg_selected\");"
                + "}");
    }

    public String contentInJFXID(String id) {
        Element selectedElement = (Element) engine.executeScript("document.querySelector('[jfxid=\"" + id + "\"]')");
        String contents = selectedElement.getTextContent();
        if (contents != null) {
            return contents.trim();
        } else {
            return "";
        }
    }
    final Popup popup = new Popup();

    private void showPopupProperty(final String id, final String value, MouseEvent mouseEvent) {
        if (mainController.getAnnotationTabController().getExtractionPanelController().getIndividualTable().getSelectionModel().isEmpty()) {
            Dialogs.showInformationDialog(mainController.getPrimaryStage(), "You must select an individual or 'Create Individual' first", "Please select an individual first", "Annotation");
            clearSelectElement();
        } else {
            if (mainController.getCurrentPropertiesLabel().isEmpty()) {
                Dialogs.showInformationDialog(mainController.getPrimaryStage(), "Individual type doesn't have property.", "No property found", "Annotation");
                clearSelectElement();
            } else {
                if (popup.isShowing()) {
                    popup.hide();
                    clearSelectElement();
                    selectElementByJFXID(Integer.valueOf(id));
                }
                final ListView<RDFProperty> listView = new ListView<>(mainController.getCurrentPropertiesLabel());
                listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
                        mainController.getAnnotationTabController().getExtractionPanelController().getIndividualDetails().get(selectedIndex).getRdfValue().setLabel(value);
                        popup.hide();
                        deSelectElementByJFXID(Integer.valueOf(id));
                    }
                });

                popup.getContent().add(listView);
                popup.setX(mouseEvent.getScreenX());
                popup.setY(mouseEvent.getScreenY());
                popup.show(mainController.getPrimaryStage());
            }
        }
    }
    //original inspect
//    public void inspectMode(MouseEvent t) {
//        try {
//            List<String> list = new ArrayList<>();
//            Element selectedElement = (Element) engine.executeScript("document.querySelectorAll('[class*=\\\"sg_selected\\\"]').item(" + 0 + ")");
//            String id = selectedElement.getAttribute("jfxid");
//            System.out.println("id: " + id);
//            String content = selectedElement.getTextContent();
//            if (content != null && !content.trim().isEmpty()) {
//                list.add(content.trim());
//            }
//
//            URL base = new URL(selectedElement.getBaseURI());
//            if (selectedElement.getAttribute("href") != null) {
//                URL url = new URL(base, selectedElement.getAttribute("href"));
//                String link = url.toString();
//                if (link != null && !link.trim().isEmpty()) {
//                    list.add(link.trim());
//                }
//            }
//
//            NodeList links = selectedElement.getElementsByTagName("a");
//            for (int j = 0; j < links.getLength(); j++) {
//                Element l = (Element) links.item(j);
//                URL baseInner = new URL(l.getBaseURI());
//                if (l.getAttribute("href") != null) {
//                    URL urlInner = new URL(baseInner, l.getAttribute("href"));
//                    String linkInner = urlInner.toString();
//                    if (linkInner != null && !linkInner.trim().isEmpty()) {
//                        list.add(linkInner.trim());
//                    }
//                }
//            }
//            int a = list.size();
//            a = 1;//TODO  :)
//            if (a > 1) {
//                showPopupPropertyMoreOne(id, list, t);
//            } else {
//                showPopupProperty(id, content.trim(), t);
//            }
//
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(BrowserController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    final Popup popup = new Popup();
//
//    private void showPopupProperty(final String id, final String value, MouseEvent t) {
//        if (mainController.getAnnotationTabController().getExtractionPanelController().getIndividualTable().getSelectionModel().isEmpty()) {
//            Dialogs.showInformationDialog(mainController.getPrimaryStage(), "You must select an individual or 'Create Individual' first", "Please select an individual first", "Annotation");
//            clearSelectElement();
//        } else {
//            if (mainController.getCurrentPropertiesLabel().isEmpty()) {
//                Dialogs.showInformationDialog(mainController.getPrimaryStage(), "Individual type doesn't have property.", "No property found", "Annotation");
//                clearSelectElement();
//            } else {
//                if (popup.isShowing()) {
//                    popup.hide();
//                    clearSelectElement();
//                    selectElementByJFXID(Integer.valueOf(id));
//                }
//                final ListView<RDFProperty> listView = new ListView<>(mainController.getCurrentPropertiesLabel());
//                listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent t) {
//                        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
//                        mainController.getAnnotationTabController().getExtractionPanelController().getIndividualDetails().get(selectedIndex).getRdfValue().setLabel(value);
//                        popup.hide();
//                        deSelectElementByJFXID(Integer.valueOf(id));
//
//                    }
//                });
//
//                popup.getContent().add(listView);
//                popup.setX(t.getScreenX());
//                popup.setY(t.getScreenY());
//                popup.show(mainController.getPrimaryStage());
//            }
//        }
//    }
//
//    private void showPopupPropertyMoreOne(final String id, List<String> list, MouseEvent t) {
//        final String value = Dialogs.showInputDialog(mainController.getPrimaryStage(), "Choose element", "More than one element detected. \nSelect content extracted that you want.", "Content Extracted", list.get(0), list);
//        if (value.isEmpty()) {
//            //hapus selected
//            deSelectElementByJFXID(Integer.valueOf(id));
//        } else {
//            if (!mainController.getCurrentPropertiesLabel().isEmpty()) {
//                if (popup.isShowing()) {
//                    popup.hide();
//                    clearSelectElement();
//                    selectElementByJFXID(Integer.valueOf(id));
//                }
//                final ListView<RDFProperty> listView = new ListView<>(mainController.getCurrentPropertiesLabel());
//                listView.autosize();
//                listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent t) {
//                        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
//                        mainController.getAnnotationTabController().getExtractionPanelController().getIndividualDetails().get(selectedIndex).getRdfValue().setLabel(value);
//                        popup.hide();
//                        deSelectElementByJFXID(Integer.valueOf(id));
//                    }
//                });
//
//                popup.getContent().add(listView);
//                popup.setX(t.getScreenX());
//                popup.setY(t.getScreenY());
//                popup.show(mainController.getPrimaryStage());
//            }
//        }
//
//    }
//    private void getSelectedElement() {
////        mainController.getCurrentSelections().clear();
//        ObservableList<CurrentSelection> currentSelections = mainController.getCurrentSelections();
//        List<String> idListInTable = new ArrayList<>();
//        for (CurrentSelection currentSelection : currentSelections) {
//            idListInTable.add(currentSelection.getId());
//        }
//
//        Integer length = (Integer) engine.executeScript("document.querySelectorAll('[class*=\\\"sg_selected\\\"]').length");
//        System.out.println("[INFO] selected : " + length);
//
//        //hapus not selected
//        if (length == 0) {
//            mainController.getCurrentSelections().clear();
//        } else if (length > 0) {
//            List<String> idListInBrowser = new ArrayList<>();
//            for (int i = 0; i < length; i++) {
//                try {
//                    List<String> list = new ArrayList<>();
//                    Element selectedElement = (Element) engine.executeScript("document.querySelectorAll('[class*=\\\"sg_selected\\\"]').item(" + i + ")");
//                    String id = selectedElement.getAttribute("jfxid");
//                    idListInBrowser.add(id);
//                    String content = selectedElement.getTextContent();
//                    if (content != null && !content.trim().isEmpty()) {
//                        list.add(content.trim());
//                    }
//
//                    URL base = new URL(selectedElement.getBaseURI());
//                    if (selectedElement.getAttribute("href") != null) {
//                        URL url = new URL(base, selectedElement.getAttribute("href"));
//                        String link = url.toString();
//                        if (link != null && !link.trim().isEmpty()) {
//                            list.add(link.trim());
//                        }
//                    }
//
//                    NodeList links = selectedElement.getElementsByTagName("a");
//                    for (int j = 0; j < links.getLength(); j++) {
//                        Element l = (Element) links.item(j);
//                        URL baseInner = new URL(l.getBaseURI());
//                        if (l.getAttribute("href") != null) {
//                            URL urlInner = new URL(baseInner, l.getAttribute("href"));
//                            String linkInner = urlInner.toString();
//                            if (linkInner != null && !linkInner.trim().isEmpty()) {
//                                list.add(linkInner.trim());
//                            }
//                        }
//                    }
//                    //jika sudah ada di tabel
//                    if (idListInTable.contains(id)) {
//                        //do not add
//                    } else {
//                        if (list.size() > 1) {
////                            String resultInput = Dialogs.showInputDialog(mainController.getPrimaryStage(), "Choose Content Extracted: ", "More than one element detected, choose one of content extracted", "Content Extracted", content.trim(), list);
////                            if (resultInput != null) {
////                                content = resultInput;
////                            }
//                            final List<CheckBox> cbs = new ArrayList<>();
//                            GridPane grid = new GridPane();
//                            grid.setHgap(10);
//                            grid.setVgap(10);
//                            grid.setPadding(new Insets(0, 10, 0, 10));
//                            for (int j = 0; j < list.size(); j++) {
//                                CheckBox cb = new CheckBox(list.get(j));
//                                cbs.add(cb);
//                                grid.add(cb, 0, j);
//                            }
//                            final List<String> sel = new ArrayList<>();
//                            Callback<Void, Void> myCallback = new Callback<Void, Void>() {
//                                @Override
//                                public Void call(Void param) {
//                                    for (int k = 0; k < cbs.size(); k++) {
//                                        if (cbs.get(k).isSelected()) {
//                                            sel.add(cbs.get(k).getText());
//                                        }
//                                    }
//
//                                    return null;
//                                }
//                            };
//                            Dialogs.showCustomDialog(mainController.getPrimaryStage(), grid, "More than one element detected. \nSelect content extracted that you want.", "Content Extracted", DialogOptions.OK, myCallback);
//                            if (sel.isEmpty()) {
//                                //hapus selected
//                                deSelectElementByJFXID(Integer.valueOf(id));
//                            } else {
//                                for (int k = 0; k < sel.size(); k++) {
////                                    mainController.getCurrentSelections().add(new CurrentSelection(id + sel.get(k).trim(), sel.get(k).trim()));
//                                    mainController.getCurrentSelections().add(new CurrentSelection(id, sel.get(k).trim()));
//                                }
//                            }
//                        } else {
//                            mainController.getCurrentSelections().add(new CurrentSelection(id, content.trim()));
//                        }
//
//                    }
//                } catch (MalformedURLException ex) {
//                    Logger.getLogger(BrowserController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//            idListInTable.removeAll(idListInBrowser);
//            Collection<CurrentSelection> toRemove = new ArrayList<>();
//            for (int c = 0; c < mainController.getCurrentSelections().size(); c++) {
//                if (idListInTable.contains(mainController.getCurrentSelections().get(c).getId())) {
//                    toRemove.add(mainController.getCurrentSelections().get(c));
//                }
//            }
//            //remove deselected
//            mainController.getCurrentSelections().removeAll(toRemove);
//        }
//    }
//    public void initGetCurrentSelectedElement() {
//        engine.executeScript(INSPECT_SCRIPT);
//        webx.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent t) {
//                try {
//                    List<String> list = new ArrayList<>();
//                    Element selectedElement = (Element) engine.executeScript("document.querySelectorAll('[class*=\\\"sg_selected\\\"]').item(" + 0 + ")");
//                    String id = selectedElement.getAttribute("jfxid");
//                    String content = selectedElement.getTextContent();
//                    if (content != null && !content.trim().isEmpty()) {
//                        list.add(content.trim());
//                    }
//
//                    URL base = new URL(selectedElement.getBaseURI());
//                    if (selectedElement.getAttribute("href") != null) {
//                        URL url = new URL(base, selectedElement.getAttribute("href"));
//                        String link = url.toString();
//                        if (link != null && !link.trim().isEmpty()) {
//                            list.add(link.trim());
//                        }
//                    }
//
//                    NodeList links = selectedElement.getElementsByTagName("a");
//                    for (int j = 0; j < links.getLength(); j++) {
//                        Element l = (Element) links.item(j);
//                        URL baseInner = new URL(l.getBaseURI());
//                        if (l.getAttribute("href") != null) {
//                            URL urlInner = new URL(baseInner, l.getAttribute("href"));
//                            String linkInner = urlInner.toString();
//                            if (linkInner != null && !linkInner.trim().isEmpty()) {
//                                list.add(linkInner.trim());
//                            }
//                        }
//                    }
//                    if (list.size() > 1) {
//                        String value = Dialogs.showInputDialog(mainController.getPrimaryStage(), "Choose element", "More than one element detected. \nSelect content extracted that you want.", "Content Extracted", list.get(0), list);
//                        if (value.isEmpty()) {
//                            //hapus selected
//                            deSelectElementByJFXID(Integer.valueOf(id));
////                            finishGetCurrentSelectedElement();
//                        } else {
//                            addProperty(id, value.trim(), t);
////                            finishGetCurrentSelectedElement();
//                        }
//                    } else {
//                        addProperty(id, content.trim(), t);
////                        finishGetCurrentSelectedElement();
//                    }
//
//                } catch (MalformedURLException ex) {
//                    Logger.getLogger(BrowserController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//
//            void addProperty(final String id, final String value, MouseEvent t) {
//                if (!mainController.getCurrentPropertiesLabel().isEmpty()) {
//                    final Popup popup = new Popup();
//                    final ListView<RDFProperty> listView = new ListView<>(mainController.getCurrentPropertiesLabel());
//                    listView.autosize();
//                    listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                        @Override
//                        public void handle(MouseEvent t) {
//                            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
//                            mainController.getAnnotationTabController().getExtractionPanelController().getIndividualDetails().get(selectedIndex).setRdfValue(new RDFValue(value, value));
//                            popup.hide();
//                            deSelectElementByJFXID(Integer.valueOf(id));
////                            finishGetCurrentSelectedElement();
//
//                        }
//                    });
//
//                    popup.getContent().add(listView);
//                    popup.setX(t.getScreenX());
//                    popup.setY(t.getScreenY());
//                    popup.show(mainController.getPrimaryStage());
//                }
//            }
//        });
//
//    }
//    public void finishGetCurrentSelectedElement() {
//        engine.executeScript(INSPECT_SCRIPT);
//        webx.setOnMouseClicked(null);
//    }
}
