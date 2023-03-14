package io.github.ppetrbednar.stp.ui.handler;

/**
 *
 * @author Petr Bednář
 */
public enum ViewCategory {
    ROOT(true),
    PANEL(false),
    DEFAULT(true);

    public final boolean decompose;

    private ViewCategory(boolean decompose) {
        this.decompose = decompose;
    }

}
