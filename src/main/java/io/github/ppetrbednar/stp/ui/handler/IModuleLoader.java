package io.github.ppetrbednar.stp.ui.handler;

import javafx.scene.Parent;

/**
 *
 * @author Petr Bednář
 * @param <T> Module Controller
 * @param <C> Controller implementing ICallable
 */
public interface IModuleLoader<T extends IModule, C extends ICallable> {

    /**
     * Returns FXML content.
     *
     * @return
     */
    public Parent getContent();

    /**
     * Returns FXMLController.
     *
     * @return
     */
    public T getController();
    
    public void setCallback(C callback);
}
