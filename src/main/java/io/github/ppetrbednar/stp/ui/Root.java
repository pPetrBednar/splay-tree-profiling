package io.github.ppetrbednar.stp.ui;

import io.github.ppetrbednar.stp.ui.handler.*;
import io.github.ppetrbednar.stp.ui.module.panel.RootBar;
import io.github.ppetrbednar.stp.ui.module.root.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author Petr Bednář
 */
public class Root extends Screen<Root> implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Root.class);

    private final Compositor compositor = new Compositor();
    private final HashMap<ViewType, ModuleLoader> modules = new HashMap<>();

    @FXML
    private BorderPane box;
    @FXML
    private BorderPane mainContainer;

    public Root() {

        Platform.runLater(() -> {
            try {
                for (ViewType type : ViewType.values()) {
                    if (type.category == ViewCategory.ROOT || type == ViewType.ROOT_BAR) {
                        modules.put(
                                type,
                                new ModuleLoader<>(type.location, controller)
                        );
                    }
                }
            } catch (IOException ex) {
                LOG.error(ex);
                System.out.println("Root modules not loaded.");
                System.exit(1);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {

            // Handler for window movement
            setWindowMove(((RootBar) modules.get(ViewType.ROOT_BAR).getController()).getMoveArea());

            // Handler for window state output (maximize, restore icons)
            setWindowState(((RootBar) modules.get(ViewType.ROOT_BAR).getController()).getWindowState());

            compositor.compose(ViewType.HOME_PAGE);
        });
    }

    private final class Compositor implements ICompositor {

        @Override
        public void decompose() {
        }

        @Override
        public void compose(ViewType type) {

            if (type.category.decompose) {
                decompose();
            }

            switch (type.category) {
                case ROOT:
                    switch (type) {
                        case HOME_PAGE:
                            composeHomePage();
                            break;
                    }
                    break;
                case DEFAULT:
                    composeHomePage();
                    break;
            }
        }

        private void composeHomePage() {
            mainContainer.setTop(modules.get(ViewType.ROOT_BAR).getContent());
            box.setCenter(modules.get(ViewType.HOME_PAGE).getContent());
            ((Main) modules.get(ViewType.HOME_PAGE).getController()).compose(ViewType.DEFAULT);
        }
    }

    public void compose(ViewType type) {
        compositor.compose(type);
    }

    public HashMap<ViewType, ModuleLoader> getModules() {
        return modules;
    }

}
