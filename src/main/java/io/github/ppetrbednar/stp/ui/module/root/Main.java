package io.github.ppetrbednar.stp.ui.module.root;


import io.github.ppetrbednar.stp.ui.Root;
import io.github.ppetrbednar.stp.ui.handler.ICompositor;
import io.github.ppetrbednar.stp.ui.handler.Module;
import io.github.ppetrbednar.stp.ui.handler.ViewType;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Petr Bednář
 */
public class Main extends Module<Main, Root> {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    private final Compositor compositor = new Compositor();

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
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
