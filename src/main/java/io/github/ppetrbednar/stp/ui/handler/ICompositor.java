package io.github.ppetrbednar.stp.ui.handler;

/**
 *
 * @author Petr Bednář
 */
public interface ICompositor {

    /**
     * Onload function. Triggers fxml design composition. Takes modules and puts
     * them in their place. Decomposes at start.
     *
     * @param type
     */
    public void compose(ViewType type);

    /**
     * Handles putting contents into pre-assigned setting.
     */
    public void decompose();
}
