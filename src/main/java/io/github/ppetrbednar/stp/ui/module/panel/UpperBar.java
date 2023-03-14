package io.github.ppetrbednar.stp.ui.module.panel;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import io.github.ppetrbednar.stp.ui.handler.Module;
import io.github.ppetrbednar.stp.ui.handler.IScreen;

/**
 * FXML Controller class
 *
 * @author Petr Bednář
 * @param <C> Screen Controller
 */
public class UpperBar<C extends IScreen<C>> extends Module<UpperBar, C> {

    @FXML
    private Label windowState;
    @FXML
    private Pane moveArea;

    @FXML
    private void maximize(MouseEvent event) {
        if (callback.isMaximized()) {
            callback.restore();
        } else {
            callback.maximize(event);
        }
    }

    @FXML
    private void minimize(MouseEvent event) {
        callback.minimize();
    }

    public Label getWindowState() {
        return windowState;
    }

    public Node getMoveArea() {
        return moveArea;
    }

    @FXML
    private void close(MouseEvent event) {
        callback.close();
    }

}
