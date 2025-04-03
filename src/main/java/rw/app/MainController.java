package rw.app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rw.data.*;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.input.MouseEvent;
import rw.enums.*;
import rw.util.Reader;
import rw.util.Writer;

import java.io.File;
import java.util.InputMismatchException;

/**
 * Class MainController
 *
 * Used to give functionality to GUI.
 *
 * @author Jarod Rideout, Jonathan Hudson
 * @email jarod.rideout@ucalgary.ca, jwhudson@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 2.0
 */


public class MainController {

    //Store the data of editor
    private static Bet bet;

    // Initialize the scene with base settings.
    @FXML
    public void initialize() {
    }
}
