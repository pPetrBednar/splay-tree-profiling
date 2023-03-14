package io.github.ppetrbednar.stp.ui.handler;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Petr Bednář
 * @param <T> Screen Controller
 */
public interface IScreenLoader<T extends IScreen> {

    /**
     * Stage configuration.
     *
     * @param title
     */
    public void setupStage(String title);

    /**
     * Stage configuration.
     *
     * @param title
     */
    public void setupStageTitle(String title);

    /**
     * Stage configuration.
     *
     * @param title
     * @param initOwner
     */
    public void setupStage(String title, Stage initOwner);

    /**
     * Stage configuration.
     *
     * @param title
     * @param initOwner
     * @param modality
     */
    public void setupStage(String title, Stage initOwner, Modality modality);

    /**
     * Main root Stage configuration.
     *
     * @param title
     * @param root
     */
    public void setupRootStage(String title, Stage root);

    /**
     * Sets up transparency of scene
     *
     * @param transparency
     */
    public void setTransparent(boolean transparency);

    /**
     * Sets ut window modality of stage
     *
     * @param modality
     */
    public void setWindowModality(Modality modality);

    /**
     * Sets up resizeablebility of stage.
     *
     */
    public void setResizeable();

    /**
     * Sets up minimal size of window
     *
     * @param width
     * @param height
     */
    public void setMinSize(double width, double height);

    /**
     * Sets up prefered size of window
     *
     * @param width
     * @param height
     */
    public void setPrefSize(double width, double height);

    /**
     * Sets up maximal size of window
     *
     * @param width
     * @param height
     */
    public void setMaxSize(double width, double height);

    /**
     * Returns FXMLController.
     *
     * @return
     */
    public T getController();

    /**
     * Returns FXML Stage.
     *
     * @return
     */
    public Stage getStage();

    /**
     * Returns FXML Scene
     *
     * @return
     */
    public Scene getScene();
}
