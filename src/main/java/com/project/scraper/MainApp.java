package com.project.scraper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main start point for the application
 */
public class MainApp extends Application {

  private static final Logger log = LoggerFactory.getLogger(MainApp.class);

  public static void main(String[] args) throws Exception {

    launch(args);
  }

  public void start(Stage stage) throws Exception {

    log.info("Starting scraper beta application");

    String fxmlFile = "/fxml/simpleGUIView.fxml";
    log.debug("Loading FXML for main view from: {}", fxmlFile);

    FXMLLoader loader = new FXMLLoader();
    Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

    log.debug("Showing JFX scene");
    Scene scene = new Scene(rootNode);
    scene.getStylesheets().add("/styles/styles.css");

    stage.setTitle("Scraper");
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest((x) -> Platform.exit());
  }
}
