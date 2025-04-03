package rw.app;

// Interface for all scenes to implement.
public interface SceneController {
    void setSceneManager(SceneManager manager);
    void initialize();
    void onSceneDisplayed();
}