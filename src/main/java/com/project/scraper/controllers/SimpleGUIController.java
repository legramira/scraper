package com.project.scraper.controllers;

import com.project.scraper.algorithm.SearchEngine;
import com.project.scraper.exceptions.NoResultsFoundException;
import com.project.scraper.exceptions.SingleAnswerException;
import com.project.scraper.models.ArticleAbstract;
import com.project.scraper.models.ArticleSearchAgainst;
import com.project.scraper.models.ArticleSearchFrom;
import com.project.scraper.executorAlgo.ExecutorLogic;
import com.project.scraper.executorAlgo.MasterBuilder;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


/** Main Controller for the GUI in javafx
 *
 */
public class SimpleGUIController {

  private static final Logger log = LoggerFactory.getLogger(SimpleGUIController.class);

  private Thread backgroundThread = null;
  @FXML
  private TextField searchText;

  @FXML
  private Label iconicName;

  @FXML
  private Label iconicPrice;

  @FXML
  private ImageView iconicImage;

  @FXML
  private Label asosName;

  @FXML
  private Label asosPrice;

  @FXML
  private ImageView asosImage;

  @FXML
  private Label messageLabel;

  @FXML
  private ProgressBar progressBar;

  @FXML
  private Label progressMessage;


  @FXML
  public void startScraping() {
    messageLabel.setText("");
    progressMessage.setText("");

    Task task = new Task<Void>() {

      @Override
      protected Void call() {
        if (!Thread.interrupted()) {
          String textToSearch = searchText.getText();
          log.info(String.format("text to search is: %s", textToSearch));
          log.info("creating process plan");
          MasterBuilder masterBuilder = new MasterBuilder(textToSearch);
          log.info("creating search engine");
          SearchEngine searchEngine = null;
          updateProgress(1, 4);
          Platform.runLater(() -> progressMessage.setText("Getting data from the stores\nthis could take a while be patience :)"));

          try {
            searchEngine = masterBuilder.createSearchEngine();
          } catch (NoResultsFoundException e) {
            Thread.currentThread().interrupt();
            log.error(String.format("no results for the query %s", e.getMessage()));
            Platform.runLater(() -> messageLabel.setText(e.getMessage()));
            return null;
          } catch (SingleAnswerException e) {
            log.info("Single article as answer returned");
            Platform.runLater(() -> {
              updateProgress(4, 4);
              messageLabel.setText(e.getMessage());
              progressMessage.setText("Done");

              iconicName.setText("Product name: " + e.getSingleArticleFrom().getName());
              iconicPrice.setText("Product price: " + e.getSingleArticleFrom().getPrice().toString());

              asosName.setText("Product name: " + e.getSingleArticleAgainst().getName());
              asosPrice.setText("Product price: " + e.getSingleArticleAgainst().getPrice().toString());

              iconicImage.setImage(new Image(new File(e.getSingleArticleFrom().getVisualDescriptor().stream().findFirst().get()).toURI().toString()));
              asosImage.setImage(new Image(e.getSingleArticleAgainst().getVisualDescriptor()));

            });
            return null;
          }
          if(Thread.interrupted()){
            Platform.runLater(() -> {
              progressMessage.setText("Search Stopped");
              progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            });
            return null;
          }
          log.info("executing search engine");
          updateProgress(2, 4);
          Platform.runLater(() -> progressMessage.setText("Calculating the similarities"));
          ExecutorLogic executorLogic = new ExecutorLogic(searchEngine);
          Pair<ArticleAbstract, ArticleAbstract> result = executorLogic.calculateMatch();
          updateProgress(3, 4);
          ArticleSearchFrom iconicArticle = (ArticleSearchFrom) result.getKey();
          ArticleSearchAgainst asosArticle = (ArticleSearchAgainst) result.getValue();

          Optional<String> firstImageIconic = iconicArticle.getVisualDescriptor().stream().findFirst();
          String firstImageAsos = asosArticle.getVisualDescriptor();

          updateProgress(4, 4);
          Platform.runLater(() -> {
            progressMessage.setText("Done");

            iconicName.setText("Product name: " + iconicArticle.getName());
            iconicPrice.setText("Product price: " + iconicArticle.getPrice().toString());

            asosName.setText("Product name: " + asosArticle.getName());
            asosPrice.setText("Product price: " + asosArticle.getPrice().toString());

            iconicImage.setImage(new Image(new File(firstImageIconic.get()).toURI().toString()));
            asosImage.setImage(new Image(new File(firstImageAsos).toURI().toString()));
          });

        }
        return null;

      }
    };
    progressBar.progressProperty().bind(task.progressProperty());
    backgroundThread = new Thread(task);
    backgroundThread.setDaemon(true);
    backgroundThread.start();


  }


  public void stopScraping() {
    if(backgroundThread != null && backgroundThread.isAlive()){
      log.info("search stop has been triger, interrupting process Thread");
      progressMessage.setText("Stop search has been triggered,\ninterrupting process gracefully");
      backgroundThread.interrupt();
    }
  }

  public void openModal() throws IOException {

    Stage stage;
    Parent root;

    stage = new Stage();
    root = FXMLLoader.load(getClass().getResource("/fxml/modalAbout.fxml"));
    stage.setScene(new Scene(root));
    stage.setTitle("About");
    stage.initModality(Modality.APPLICATION_MODAL);
    root.getStylesheets().add("/styles/styles.css");
    stage.showAndWait();

  }

  public void closeApplication() {
    Platform.exit();
  }

}
