package io.github.ppetrbednar.stp.ui.handler;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import io.github.ppetrbednar.stp.tools.screen.Resizer;

/**
 *
 * @author Petr Bednář
 * @param <T> Controller type
 */
public interface IScreen<T> extends ICallable {

    /**
     * Saves Stage of current FXML Document.
     *
     * @param stage Stage
     */
    public void setStage(Stage stage);

    /**
     * Saves Controller of current FXML Document.
     *
     * @param controller Controller
     */
    public void setController(T controller);

    /**
     * Closes current Window.
     */
    public void close();

    public void maximize(MouseEvent mouseEvent);

    public void restore();

    public void minimize();

    public boolean isMaximized();

    public void setMaximized();

    public void setRestored();

    public void setWindowMove(Node node);

    public void setWindowState(Label label);

    public Stage getStage();

    public void setResizer(Resizer resizer);

    public Resizer getResizer();
}
