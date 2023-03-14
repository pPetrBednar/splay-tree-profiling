package io.github.ppetrbednar.stp.ui.window.alert;

/**
 *
 * @author Petr Bednář
 */
public interface IAlert {

    public void setCancelVisibility(boolean cancel);

    public void setText(String text);

    public void clear();

    public Object getResult();
}
