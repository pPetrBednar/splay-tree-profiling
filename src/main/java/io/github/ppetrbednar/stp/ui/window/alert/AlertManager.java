package io.github.ppetrbednar.stp.ui.window.alert;

import io.github.ppetrbednar.stp.ui.handler.ScreenLoader;

import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Petr Bednář
 */
public class AlertManager {

    private static ScreenLoader<Information> information;
    private static ScreenLoader<Warning> warning;
    private static ScreenLoader<Failure> failure;
    private static ScreenLoader<SingleInput> singleInput;
    private static ScreenLoader<DoubleInput> doubleInput;
    private static boolean setup = false;

    static {
        try {
            information = new ScreenLoader<>("window/alert/Information", Modality.APPLICATION_MODAL);
            warning = new ScreenLoader<>("window/alert/Warning", Modality.APPLICATION_MODAL);
            failure = new ScreenLoader<>("window/alert/Failure", Modality.APPLICATION_MODAL);
            singleInput = new ScreenLoader<>("window/alert/SingleInput", Modality.APPLICATION_MODAL);
            doubleInput = new ScreenLoader<>("window/alert/DoubleInput", Modality.APPLICATION_MODAL);

        } catch (IOException ex) {
            // Information alerts cannot be loaded
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Application alert system failed to load.");
            alert.showAndWait();
            System.exit(1);
        }
    }

    public enum AlertType {
        INFORMATION,
        WARNING,
        FAILURE,
        SINGLE_INPUT,
        DOUBLE_INPUT;
    }

    private ScreenLoader<? extends IAlert> loader;

    public AlertManager(AlertType type, String text, boolean cancel) {
        switch (type) {
            case INFORMATION -> loader = information;
            case WARNING -> loader = warning;
            case FAILURE -> loader = failure;
            case SINGLE_INPUT -> loader = singleInput;
            case DOUBLE_INPUT -> loader = doubleInput;
        }

        loader.getController().setText(text);
        loader.getController().setCancelVisibility(cancel);
    }

    public void show() {

        Platform.runLater(() -> {
            if (loader != null && !loader.getStage().isShowing()) {
                loader.getStage().showAndWait();
            }
        });
    }

    public void showAndWait(Runnable task) {
        Platform.runLater(() -> {
            if (loader != null && !loader.getStage().isShowing()) {
                loader.getStage().showAndWait();
                task.run();
            }
        });
    }

    public Object getResult() {

        if (loader == null) {
            return false;
        }

        return loader.getController().getResult();
    }

    public void clear() {
        if (loader == null) {
            return;
        }

        loader.getController().clear();
    }

    public static void setupStage(Stage stage) {

        if (setup) {
            return;
        }

        information.setupStage("", stage);
        information.setupStageTitle("Information alert");
        information.setTransparent(true);

        warning.setupStage("", stage);
        warning.setupStageTitle("Warning alert");
        warning.setTransparent(true);

        failure.setupStage("", stage);
        failure.setupStageTitle("Failure alert");
        failure.setTransparent(true);

        singleInput.setupStage("", stage);
        singleInput.setupStageTitle("Data input alert");
        singleInput.setTransparent(true);

        doubleInput.setupStage("", stage);
        doubleInput.setupStageTitle("Data input alert");
        doubleInput.setTransparent(true);

        setup = true;
    }

}
