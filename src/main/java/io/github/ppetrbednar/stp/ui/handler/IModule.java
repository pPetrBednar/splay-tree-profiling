package io.github.ppetrbednar.stp.ui.handler;

/**
 *
 * @author Petr Bednář
 * @param <T> Module Controller
 * @param <C> Callback Controller
 */
public interface IModule<T, C extends ICallable> extends ICallable {

    /**
     * Saves Controller of current FXML Document.
     *
     * @param controller
     */
    public void setController(T controller);

    /**
     * Saves Controller which serves as callback.
     *
     * @param callback
     */
    public void setCallback(C callback);

    /**
     * Returns saved callback.
     *
     * @return callback
     */
    public C getCallback();
}
