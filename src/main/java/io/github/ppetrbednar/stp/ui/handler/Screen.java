package io.github.ppetrbednar.stp.ui.handler;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import io.github.ppetrbednar.stp.tools.screen.Move;
import io.github.ppetrbednar.stp.tools.screen.Resizer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author Petr Bednář
 * @param <T> Controller type
 */
public abstract class Screen<T> implements IScreen<T>, Initializable {

    // Stage of current FXML Document
    protected Stage stage;
    protected T controller;
    private Resizer resizer;

    protected Label windowState;

    private boolean maximized;
    private boolean fullscreen;

    private Point2D stagePos;
    private Point2D stageSize;

    public Screen() {
        this.maximized = false;
        this.fullscreen = false;

        Platform.runLater(() -> {
            if (stage == null) {
                return;
            }

            stagePos = new Point2D(stage.getX(), stage.getY());
            stageSize = new Point2D(stage.getWidth(), stage.getHeight());
        });
    }

    @Override
    public void setWindowMove(Node node) {
        Move.addMoveListener(this, stage, node);
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setController(T controller) {
        this.controller = controller;
    }

    @Override
    public void close() {
        stage.close();
    }

    @Override
    public void maximize(MouseEvent mouseEvent) {
        setMaximized();
        ObservableList<javafx.stage.Screen> screens = javafx.stage.Screen.getScreensForRectangle(mouseEvent.getScreenX(), mouseEvent.getScreenY(), 1, 1);
        if (screens.isEmpty()) {
            return;
        }
        Rectangle2D screen = screens.get(0).getVisualBounds();

        stagePos = new Point2D(stage.getX(), stage.getY());
        stageSize = new Point2D(stage.getWidth(), stage.getHeight());

        stage.setX(screen.getMinX());
        stage.setY(screen.getMinY());
        stage.setWidth(screen.getWidth());
        stage.setHeight(screen.getHeight());
    }

    @Override
    public void minimize() {
        stage.setIconified(true);
    }

    @Override
    public void restore() {
        setRestored();
        stage.setX(stagePos.getX());
        stage.setY(stagePos.getY());
        stage.setWidth(stageSize.getX());
        stage.setHeight(stageSize.getY());
    }

    @Override
    public boolean isMaximized() {
        return maximized;
    }

    @Override
    public void setMaximized() {
        if (windowState != null) {
            windowState.setText("🗗"); // restore char
        }

        maximized = true;
    }

    @Override
    public void setRestored() {
        if (windowState != null) {
            windowState.setText("🗖"); // maximize char
        }

        maximized = false;
    }

    @Override
    public void setWindowState(Label label) {
        windowState = label;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setResizer(Resizer resizer) {
        this.resizer = resizer;
    }

    @Override
    public Resizer getResizer() {
        return resizer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
