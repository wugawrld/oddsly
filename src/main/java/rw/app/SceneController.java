package rw.app;
/**
 * CPSC 233 Project SceneController  ...
 * @author Jarod Rideout, Risha Vaghani, Sardar Waheed
 * @email jarod.rideout@ucalgary.ca, risha.vaghani@ucalgary.ca, sardar.waheed@ucalgary.ca
 * @tutorial Tutorial 1 Tuesdays 11:00 - 13:00
 * @version 3.0
 */

// Interface for all scenes to implement.
public interface SceneController {
    void setSceneManager(SceneManager manager);
    void initialize();
    void onSceneDisplayed();
}