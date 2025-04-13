package rw.app;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ResourceBundle;

public class ApiController implements Initializable{

    @FXML
    private TextArea textArea;

    @FXML
    private WebView nbaWebView;

    @FXML
    private WebView nhlWebView;

    // service for periodic updating
    private ApiFetchService apiFetchService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initalize webviwer and load urls with custom agent
        WebEngine nbaEngine = nbaWebView.getEngine();
        nbaEngine.load("https://www.espn.com/nba/standings");

        WebEngine nhlEngine = nhlWebView.getEngine();
        nhlEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36");
        nhlEngine.load("https://www.espn.com/nhl/standings");

        // initialize/start scheduledservice to perform API updates every 5 seconds
        apiFetchService = new ApiFetchService();
        apiFetchService.setDelay(Duration.ZERO);
        apiFetchService.setPeriod(Duration.seconds(5));

        // when service succeeds, update ui (this handler runs on the FX thread)
        apiFetchService.setOnSucceeded(event -> {
            String finalData = apiFetchService.getValue();
            textArea.setText(finalData);
        });
        // when service fails, update ui with error message
        apiFetchService.setOnFailed(event -> {
            Throwable apiFetchServiceException = apiFetchService.getException();
            textArea.setText("Failed to fetch data: " + (apiFetchServiceException != null ? apiFetchServiceException.getMessage() : "unknown error"));
        });
        apiFetchService.start();
    }

    private static class ApiFetchService extends ScheduledService<String> {
        @Override
        protected Task<String> createTsk() {

        }
    }

    // method to fetch api, parse json, and update ui periodically (when fetch api is clicked)
    private static class ApiFetchService extends ScheduledService<String> {
        @Override
        protected Task<String> createTask() {
            return new Task<>() {
                @Override
                protected String call() throws Exception {
                    HttpClient httpClient = HttpClient.newHttpClient();

                    // define endpoints for NBA and NHL standings
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

                    // run HTTP requests synchronously
                    HttpResponse<String> responseNBA = httpClient.send(standingsNBA, BodyHandlers.ofString());
                    HttpResponse<String> responseNHL = httpClient.send(standingsNHL, BodyHandlers.ofString());

                    // get responses: status is 200, use body; otherwise, store error message
                    String finishedStandingsDataNBA = responseNBA.statusCode() == 200 ?
                            responseNBA.body() : "Error: " + responseNBA.statusCode();
                    String finishedStandingsDataNHL = responseNHL.statusCode() == 200 ?
                            responseNHL.body() : "Error: " + responseNHL.statusCode();

                    // parse JSON
                    Gson gson = new Gson();
                    StandingsData nbaData = gson.fromJson(finishedStandingsDataNBA, StandingsData.class);
                    StandingsData nhlData = gson.fromJson(finishedStandingsDataNHL, StandingsData.class);

                    // build display strings
                    String nbaDisplay = (nbaData != null && nbaData.getFullViewLink() != null) ?
                            "NBA Standings Link: " + nbaData.getFullViewLink().getText() +
                                    "\nURL: " + nbaData.getFullViewLink().getHref() :
                            finishedStandingsDataNBA;
                    String nhlDisplay = (nhlData != null && nhlData.getFullViewLink() != null) ?
                            "NHL Standings Link: " + nhlData.getFullViewLink().getText() +
                                    "\nURL: " + nhlData.getFullViewLink().getHref() :
                            finishedStandingsDataNHL;
                    return "NBA Standings:\n" + nbaDisplay + "\n\nNHL Standings:\n" + nhlDisplay;
                }
            };
        }
    }
    // safety method to clean up the service on app shut down
    public void shutDown() {
        if (apiFetchService != null) {
            apiFetchService.cancel();
        }
    }

    // model class for the "fullViewLink"
    public static class FullViewLink {
        private String text;
        private String href;

        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }

        public String getHref() {
            return href;
        }
        public void setHref(String href) {
            this.href = href;
        }
    }

    // model class for JSON structure for standings data
    public static class StandingsData {
        private FullViewLink fullViewLink;

        public FullViewLink getFullViewLink() {
            return fullViewLink;
        }
        public void setFullViewLink(FullViewLink fullViewLink) {
            this.fullViewLink = fullViewLink;
        }
    }
}