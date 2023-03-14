package io.github.ppetrbednar.stp.ui.handler;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author Petr Bednář
 * @param <T> Module Controller
 * @param <C> Callback Controller
 */
public abstract class Module<T, C extends ICallable> implements IModule<T, C>, Initializable {

    protected T controller;
    protected C callback;

    @Override
    public void setController(T controller) {
        this.controller = controller;
    }

    @Override
    public void setCallback(C callback) {
        this.callback = callback;
    }

    @Override
    public C getCallback() {
        return callback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
