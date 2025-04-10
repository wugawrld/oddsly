package rw.app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ApiController implements Initializable{

    @FXML
    private TextArea textArea;

    @FXML
    private WebView nbaWebView;

    @FXML
    private WebView nhlWebView;

    // synchronous updating object
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // start scheduler to call and update method every 5 seconds
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::updateData, 0, 5, TimeUnit.SECONDS);
    }

    // method to fetch api (when fetch api is clicked)
    private void updateData() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            // endpoints
            String endpointStandingsNBA = "https://site.web.api.espn.com/apis/site/v2/sports/basketball/nba/standings?season=2025";
            String endpointStandingsNHL = "https://site.web.api.espn.com/apis/site/v2/sports/hockey/nhl/standings?season=2025";
            HttpRequest standingsNBA = HttpRequest.newBuilder()
                    .uri(new URI(endpointStandingsNBA))
                    .GET()
                    .build();
            HttpRequest standingsNHL = HttpRequest.newBuilder()
                    .uri(new URI(endpointStandingsNHL))
                    .GET()
                    .build();

            // send requests synchronously
            HttpResponse<String> responseNBAStandings = httpClient.send(standingsNBA, BodyHandlers.ofString());
            HttpResponse<String> responseNHLStandings = httpClient.send(standingsNHL, BodyHandlers.ofString());

            // check for status code; send data combined
            String finishedStandingsDataNBA = responseNBAStandings.statusCode() == 200 ? responseNBAStandings.body() : "Error: " + responseNBAStandings.statusCode();
            String finishedStandingsDataNHL = responseNHLStandings.statusCode() == 200 ? responseNHLStandings.body() : "Error: " + responseNHLStandings.statusCode();
            String combinedData = "NBA Standings:\n" + finishedStandingsDataNBA + "\n\nNHL Standings:\n" + finishedStandingsDataNHL;

            // update ui
            Platform.runLater(() -> textArea.setText(combinedData));
            // load into webview
            WebEngine webEngine = nbaWebView.getEngine();
            webEngine.load("https://www.espn.com/nba/standings");
            WebEngine nhlEngine = nhlWebView.getEngine();
            nhlEngine.load("https://www.espn.com/nhl/standings");

        } catch (URISyntaxException uriSyntaxException) {
            Platform.runLater(() -> textArea.setText("Invalid URI: " + uriSyntaxException.getMessage()));

        } catch (Exception exception) {
            Platform.runLater(() -> textArea.setText("Failed to fetch data: " + exception.getMessage()));
        }
}

// safety method to clean up scheduler when app shuts down
public void shutDown() {
    if (scheduledExecutorService != null) {
    scheduledExecutorService.shutdown();}
}
}
