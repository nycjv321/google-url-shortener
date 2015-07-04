package utilities.nycjv321.googleurlshortener;

import com.nycjv321.utilities.FXUtilities;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Javier on 11/1/2014.
 */
public class GoogleURLShortenerUI extends Application implements FXUtilities {
    private static final Logger logger = LogManager.getLogger(GoogleURLShortenerUI.class.getName());
    private final GoogleURLShortenerRecordManager recordManager = new GoogleURLShortenerRecordManager();
    private final GoogleURLShortener googleURLShortener = new GoogleURLShortener();
    private ObservableList<GoogleURLShortenerRecord> data;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane grid = createShortenView();
        Scene scene = new Scene(grid, 310, 200);
        primaryStage.setTitle("Google URL Shortener");

        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/logo.png")));


        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private GridPane createShortenView() {
        GridPane shortenView = createGrid();

        Label apiKeyLabel = createLabel("API Key: ", 90, Pos.CENTER_RIGHT);
        PasswordField apiKeyField = new PasswordField();
        apiKeyField.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {

                String text = apiKeyField.getText();
                if (!text.isEmpty()) {
                    googleURLShortener.setApiKey(text);
                }
            }
        });

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

            URL shortenedURL = googleURLShortener.getShortenedURL(uri.toString());
            try {
                GoogleURLShortenerRecord insert = recordManager.insert(uri.toURL(), shortenedURL);
                if (!data.contains(insert)) {
                    data.add(insert);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            shortenedUrlTextField.setText(String.valueOf(shortenedURL));
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

            URL expandedURL = googleURLShortener.getExpandedURL(uri.toString());
            try {
                GoogleURLShortenerRecord insert = recordManager.insert(expandedURL, uri.toURL());
                if (!data.contains(insert)) {
                    data.add(insert);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            urlTextField.setText(String.valueOf(expandedURL));

        });

        urlTextField.setPrefWidth(175);
        shortenedUrlTextField.setPrefWidth(175);

        TableView table = new TableView();
        table.setEditable(true);

        TableColumn urlCol = new TableColumn("URL");
        urlCol.setPrefWidth(130);
        TableColumn shortenedCol = new TableColumn("Shortened URL");

        urlCol.setCellValueFactory(
                new PropertyValueFactory<GoogleURLShortenerRecord, URL>("url")
        );
        shortenedCol.setCellValueFactory(
                new PropertyValueFactory<GoogleURLShortenerRecord, URL>("shortenedURL")
        );


        table.getColumns().addAll(urlCol, shortenedCol);

        data = FXCollections.observableArrayList(recordManager.getRecords());
        table.setItems(data);

        table.setMinHeight(shortenView.getMinWidth());

        shortenView.add(apiKeyLabel, 0, 0);
        shortenView.add(apiKeyField, 1, 0);
        shortenView.add(urlLabel, 0, 1);
        shortenView.add(urlTextField, 1, 1);
        shortenView.add(shortenedUrlLabel, 0, 2);
        shortenView.add(shortenedUrlTextField, 1, 2);
        shortenView.add(table, 0, 3, 3, 1);

        return shortenView;
    }
}
