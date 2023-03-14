package io.github.ppetrbednar.stp.ui.handler;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 *
 * @author Petr Bednář
 * @param <T> Module Controller
 * @param <C> Controller implementing ICallable
 */
public class ModuleLoader<T extends IModule, C extends ICallable> implements IModuleLoader<T, C> {

    private FXMLLoader loader;
    private Parent content;
    private T controller;

    @SuppressWarnings("unchecked")
    public ModuleLoader(String fxml) throws IOException {
        loader = new FXMLLoader(this.getClass().getResource("/io/github/ppetrbednar/stp/ui/module/" + fxml + ".fxml"));
        content = loader.load();
        controller = loader.getController();
        controller.setController(controller);
    }

    @SuppressWarnings("unchecked")
    public ModuleLoader(String fxml, C callback) throws IOException {
        this(fxml);
        controller.setCallback(callback);
    }

    @Override
    public T getController() {
        return controller;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setCallback(C callback) {
        controller.setCallback(callback);
    }

    @Override
    public Parent getContent() {
        return content;
    }
}
