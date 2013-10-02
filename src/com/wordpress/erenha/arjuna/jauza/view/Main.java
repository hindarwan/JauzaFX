/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.erenha.arjuna.jauza.view;

import com.wordpress.erenha.arjuna.jauza.controller.MainController;
import com.wordpress.erenha.arjuna.jauza.util.StaticValue;
import java.io.IOException;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Hindarwan
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Map<String, String> named = this.getParameters().getNamed();
        StaticValue.baseURL = named.get("baseURL");
        StaticValue.sesameServer = named.get("sesameServer");
        StaticValue.sesameRepositoryID = named.get("sesameID");
//        StaticValue.baseURL = "http://localhost:8888";
//        StaticValue.sesameServer = "http://localhost:8080/openrdf-sesame";
//        StaticValue.sesameRepositoryID = "onelink-repository";
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        MainController controller = (MainController) fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
