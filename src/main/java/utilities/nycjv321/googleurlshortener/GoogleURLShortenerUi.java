package utilities.nycjv321.googleurlshortener;

import com.nycjv321.utilities.FXUtilities;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Javier on 11/1/2014.
 */
public class GoogleURLShortenerUI extends Application implements FXUtilities {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane grid = createShortenView();
        Scene scene = new Scene(grid, 300, 80);
        primaryStage.setTitle("Google URL Shortener");

        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/logo.png")));


        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private GridPane createShortenView() {
        GridPane shortenView = createGrid();

        Label urlLabel = createLabel("URL: ", 90, Pos.CENTER_RIGHT);
        TextField urlTextField = createTextField(Pos.CENTER);

        Label shortenedUrlLabel = createLabel("Shortened URL: ", 90, Pos.CENTER_RIGHT);
        TextField shortenedUrlTextField = createTextField(Pos.CENTER);

        urlTextField.setOnMouseClicked(event -> shortenedUrlTextField.setText(""));
        shortenedUrlTextField.setOnMouseClicked(event -> urlTextField.setText(""));

        urlTextField.setOnKeyTyped(event -> {
            if (!urlTextField.getText().contains("http")) {
                return;
            }
            URI uri = null;
            try {
                uri = new URI(urlTextField.getText());
            } catch (URISyntaxException e) {
                return;
            }

            if (uri != null) {
                String shortenedURL = GoogleURLShortener.getShortenedURL(uri.toString());
                shortenedUrlTextField.setText(shortenedURL);
            }

        });

        shortenedUrlTextField.setOnKeyTyped(event -> {
            if (!shortenedUrlTextField.getText().contains("http")) {
                return;
            }
            urlTextField.setText("");
            URI uri = null;
            try {
                uri = new URI(shortenedUrlTextField.getText());
            } catch (URISyntaxException e) {
                return;
            }

            if (uri != null) {
                String expandedURL = GoogleURLShortener.getExpandedURL(uri.toString());
                urlTextField.setText(expandedURL);
            }
        });

        urlTextField.setPrefWidth(175);
        shortenedUrlTextField.setPrefWidth(175);

        shortenView.add(urlLabel, 0, 0);
        shortenView.add(urlTextField, 1, 0);
        shortenView.add(shortenedUrlLabel, 0, 1);
        shortenView.add(shortenedUrlTextField, 1, 1);

        return shortenView;
    }
}
