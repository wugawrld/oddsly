package rw.app;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ResourceBundle;

/**
 * CPSC 233 Project API Controller ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

public class ApiController implements Initializable {

    @FXML
    private TextArea textArea;

    @FXML
    private WebView nbaWebView;

    @FXML
    private WebView nhlWebView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // initalize webviwer and load urls
        WebEngine nbaEngine = nbaWebView.getEngine();
        nbaEngine.load("https://www.espn.com/nba/standings");

        WebEngine nhlEngine = nhlWebView.getEngine();
        nhlEngine.load("https://www.espn.com/nhl/standings");
    }

    // method to fetch api, parse json
    public void fetchAPI() {
        Task<String> fetchTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                HttpClient httpClient = HttpClient.newHttpClient();

                // endpoints for NBA and NHL standings
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
        // update ui after task completion: handlers run on FX thread
        fetchTask.setOnSucceeded(event -> {
            String finalData = fetchTask.getValue();
            textArea.setText(finalData);
        });
        fetchTask.setOnFailed(event -> {
            Throwable exception = fetchTask.getException();
            textArea.setText("Failed to fetch data: " + (exception != null ? exception.getMessage(): "Error"));
        });

        // run task on background
        Thread thread = new Thread(fetchTask);
        thread.setDaemon(true);
        thread.start();
        }
    }