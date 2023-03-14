package io.github.ppetrbednar.stp.ui.window.alert;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.github.ppetrbednar.stp.ui.handler.Screen;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Petr Bednář
 */
public class SingleInput extends Screen<SingleInput> implements IAlert {

    private String result;

    @FXML
    private Label text;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Pane mover;
    @FXML
    private JFXTextField input;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setWindowMove(mover);
        });
    }

    @FXML
    private void closeWindow(MouseEvent event) {
        close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        result = null;
        close();
    }

    @FXML
    private void confirm(ActionEvent event) {
        result = input.getText();
        close();
    }

    @Override
    public void setCancelVisibility(boolean cancel) {
        btnCancel.setVisible(cancel);
    }

    @Override
    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public void clear() {
        input.clear();
    }

    @Override
    public Object getResult() {
        return result;
    }
}
