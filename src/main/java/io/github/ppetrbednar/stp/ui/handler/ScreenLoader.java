package io.github.ppetrbednar.stp.ui.handler;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import io.github.ppetrbednar.stp.tools.screen.Resizer;

/**
 *
 * @author Petr Bednář
 * @param <T> Screen Controller
 */
public class ScreenLoader<T extends Screen> implements IScreenLoader<T> {

    private FXMLLoader loader;
    private Parent parent;
    private T controller;
    private Stage stage;

    public ScreenLoader(String fxml) throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("/io/github/ppetrbednar/stp/ui/" + fxml + ".fxml"));
        parent = loader.load();
        controller = loader.getController();
        stage = new Stage();
    }

    public ScreenLoader(String fxml, Modality modality) throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("/io/github/ppetrbednar/stp/ui/" + fxml + ".fxml"));
        parent = loader.load();
        controller = loader.getController();
        stage = new Stage();
        stage.initModality(modality);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setupStage(String title) {
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        controller.setController(controller);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setupStageTitle(String title) {
        stage.setTitle(title);
    }

    @Override
    public void setupStage(String title, Stage initOwner) {
        stage.initOwner(initOwner);
        controller.setStage(stage);
        setupStage(title);
    }

    @Override
    public void setupStage(String title, Stage initOwner, Modality modality) {
        stage.initModality(modality);
        setupStage(title, initOwner);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setupRootStage(String title, Stage root) {
        stage = root;
        controller.setStage(stage);
        controller.setController(controller);
        setupStage(title);
    }

    @Override
    public void setWindowModality(Modality modality) {
        stage.initModality(modality);
    }

    @Override
    public void setTransparent(boolean transparency) {
        if (transparency) {
            stage.initStyle(StageStyle.UNDECORATED);
            stage.getScene().setFill(Color.TRANSPARENT);
        } else {
            stage.initStyle(StageStyle.DECORATED);
            stage.getScene().setFill(Color.WHITE);
        }
    }

    @Override
    public void setMinSize(double width, double height) {
        stage.setMinWidth(width);
        stage.setMinHeight(height);
    }

    @Override
    public void setPrefSize(double width, double height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    @Override
    public void setMaxSize(double width, double height) {
        stage.setMaxWidth(width);
        stage.setMaxHeight(height);
    }

    @Override
    public T getController() {
        return controller;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public Scene getScene() {
        return stage.getScene();
    }

    @Override
    public void setResizeable() {
        Resizer resizer = new Resizer(stage);
        Resizer.addResizerRecursive(resizer, parent);
        controller.setResizer(resizer);
    }
}
