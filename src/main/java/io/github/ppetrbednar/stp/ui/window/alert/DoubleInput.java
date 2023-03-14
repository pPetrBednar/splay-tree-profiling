package io.github.ppetrbednar.stp.ui.window.alert;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.github.ppetrbednar.stp.ui.handler.Screen;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Petr Bednář
 */
public class DoubleInput extends Screen<DoubleInput> implements IAlert {

    private String result1;
    private String result2;

    @FXML
    private Label text;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Pane mover;
    @FXML
    private JFXTextField input1;
    @FXML
    private JFXTextField input2;

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
        result1 = null;
        result2 = null;
        close();
    }

    @FXML
    private void confirm(ActionEvent event) {
        result1 = input1.getText();
        result2 = input2.getText();
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
        input1.clear();
        input2.clear();
        input1.requestFocus();
    }

    @Override
    public Object getResult() {
        return new Pair<String, String>(result1, result2);
    }
}
