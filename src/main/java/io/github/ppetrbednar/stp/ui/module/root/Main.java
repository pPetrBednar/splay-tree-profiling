package io.github.ppetrbednar.stp.ui.module.root;


import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import io.github.ppetrbednar.stp.logic.STProfiler;
import io.github.ppetrbednar.stp.logic.structures.Item;
import io.github.ppetrbednar.stp.logic.structures.SplayTree;
import io.github.ppetrbednar.stp.ui.Root;
import io.github.ppetrbednar.stp.ui.handler.ICompositor;
import io.github.ppetrbednar.stp.ui.handler.Module;
import io.github.ppetrbednar.stp.ui.handler.ViewType;
import io.github.ppetrbednar.stp.ui.window.alert.AlertManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Petr Bednář
 */
public class Main extends Module<Main, Root> {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    private final Compositor compositor = new Compositor();
    private final STProfiler stProfiler = new STProfiler();
    private SplayTree<Integer, Item> splayTree = new SplayTree<>();
    @FXML
    private ListView<Object> listViewItems;
    @FXML
    private TextArea textAreaVisualization;
    @FXML
    private JFXTextField textFieldUID;
    @FXML
    private JFXTextField textFieldTitle;
    @FXML
    private JFXTextField textFieldIterations;
    @FXML
    private JFXTextField textFieldPerIteration;
    @FXML
    private JFXTextField textFieldToGenerate;
    @FXML
    private Label labelItems;
    @FXML
    private Label labelTreeDepth;
    @FXML
    private JFXToggleButton toggleBtnPrettyPrint;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        textAreaVisualization.setEditable(false);
    }

    @FXML
    public void insert(ActionEvent actionEvent) {

        if (textFieldUID.getText().isEmpty()) {
            return;
        }

        Integer uid = null;
        try {
            uid = Integer.parseInt(textFieldUID.getText());
        } catch (Exception e) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Item UID must be number.", false);
            am.show();
            return;
        }

        if (splayTree.contains(uid)) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Item with UID (" + textFieldUID.getText() + ") already exists.", false);
            am.show();
            return;
        }

        Item item = new Item(uid, textFieldTitle.getText());
        splayTree.add(item.getUid(), item);
        updateVisualization();

    }

    @FXML
    public void update(ActionEvent actionEvent) {

        if (textFieldUID.getText().isEmpty()) {
            return;
        }

        Integer uid = null;
        try {
            uid = Integer.parseInt(textFieldUID.getText());
        } catch (Exception e) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Item UID must be number.", false);
            am.show();
            return;
        }

        if (!splayTree.contains(uid)) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Item with UID (" + textFieldUID.getText() + ") not found.", false);
            am.show();
            return;
        }

        splayTree.get(uid).setTitle(textFieldTitle.getText());
        updateVisualization();
    }

    @FXML
    public void delete(ActionEvent actionEvent) {

        if (textFieldUID.getText().isEmpty()) {
            return;
        }

        Integer uid = null;
        try {
            uid = Integer.parseInt(textFieldUID.getText());
        } catch (Exception e) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Item UID must be number.", false);
            am.show();
            return;
        }

        if (!splayTree.contains(uid)) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Item with UID (" + textFieldUID.getText() + ") not found.", false);
            am.show();
            return;
        }

        splayTree.remove(uid);
        updateVisualization();
    }

    @FXML
    public void clear(ActionEvent actionEvent) {
        splayTree.clear();
        updateVisualization();
    }

    private void updateVisualization() {
        Platform.runLater(() -> {
            listViewItems.getItems().clear();
            textAreaVisualization.setText(toggleBtnPrettyPrint.isSelected() ? splayTree.print() : splayTree.printLight());
            Iterator<Item> it = splayTree.inorderValueIterator();
            while (it.hasNext()) {
                listViewItems.getItems().add(it.next());
            }

            labelItems.setText(splayTree.size() + "");
            labelTreeDepth.setText(splayTree.depth() + "");
        });
    }

    @FXML
    public void profile(ActionEvent actionEvent) {
        try {
            int iterations = Integer.parseInt(textFieldIterations.getText());
            int values = Integer.parseInt(textFieldPerIteration.getText());

            Platform.runLater(() -> {
                stProfiler.profile(iterations, values);
                try {
                    JsonObject stats = (JsonObject) Jsoner.deserialize(FileUtils.readFileToString(STProfiler.PROFILING_STATISTICS_FILE, StandardCharsets.UTF_8));

                    StringBuilder out = new StringBuilder();
                    out.append("Iterations: ").append(stats.get("iterations")).append("\n");
                    out.append("Values per iteration: ").append(stats.get("values")).append("\n");
                    out.append("Minimum depth: ").append(stats.get("min")).append("\n");
                    out.append("Maximum depth: ").append(stats.get("max")).append("\n");
                    out.append("Mean (average) depth: ").append(stats.get("mean")).append("\n");
                    out.append("Median of depth: ").append(stats.get("median")).append("\n");
                    var arr = (JsonArray) stats.get("modes");
                    out.append("Modes of depth: ");
                    for (var mode : arr) {
                        out.append(mode).append(" ");
                    }
                    out.append("\n");

                    AlertManager am = new AlertManager(AlertManager.AlertType.INFORMATION, out.toString(), false);
                    am.show();
                } catch (JsonException | IOException e) {
                    AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Profiling file not found.", false);
                    am.show();
                }
            });
        } catch (Exception e) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Incorrect value provided.", false);
            am.show();
        }
    }

    @FXML
    public void generate(ActionEvent actionEvent) {
        try {
            int values = Integer.parseInt(textFieldToGenerate.getText());

            Platform.runLater(() -> {
                splayTree = stProfiler.generate(values);
                textAreaVisualization.setText(splayTree.toString());
                updateVisualization();
            });
        } catch (Exception e) {
            AlertManager am = new AlertManager(AlertManager.AlertType.WARNING, "Incorrect value provided.", false);
            am.show();
        }
    }

    @FXML
    public void togglePrettyPrint(ActionEvent actionEvent) {
        Platform.runLater(this::updateVisualization);
    }

    @FXML
    public void openProfilingFolder(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                Desktop.getDesktop().open(STProfiler.PROFILING_FOLDER);
            } catch (IOException ignored) {
            }
        });
    }

    private class Compositor implements ICompositor {

        @Override
        public void compose(ViewType type) {
            decompose();

            Platform.runLater(() -> {

            });
        }

        @Override
        public void decompose() {

        }
    }

    public void compose(ViewType type) {
        compositor.compose(type);
    }

}
