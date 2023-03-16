package io.github.ppetrbednar.stp;

import io.github.ppetrbednar.stp.resources.Resource;
import io.github.ppetrbednar.stp.resources.ResourceManager;
import javafx.application.Application;
import javafx.stage.Stage;
import io.github.ppetrbednar.stp.ui.Root;
import io.github.ppetrbednar.stp.ui.handler.ScreenLoader;
import io.github.ppetrbednar.stp.ui.window.alert.AlertManager;

/**
 * @author Petr Bednář
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Fix JavaFX UI glitches
        System.setProperty("prism.dirtyopts", "false");

        // Locate log4j2 config file in executable jar
        System.setProperty("log4j.configurationFile", "app/lib/log4j2.xml");

        ScreenLoader<Root> root = new ScreenLoader<>("Root");
        root.setupRootStage("STP", stage);
        root.setMinSize(1200, 640);
        root.setPrefSize(1200, 640);
        root.setTransparent(true);
        //root.setResizeable();

        stage.getIcons().add(ResourceManager.getImageResource(Resource.APPLICATION_ICON));

        AlertManager.setupStage(root.getStage());

        root.getStage().show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
