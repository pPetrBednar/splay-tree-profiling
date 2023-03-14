package io.github.ppetrbednar.stp.ui.handler;

/**
 * View Type enum for root page
 *
 * @author Petr Bednář
 */
public enum ViewType {

    DEFAULT(ViewCategory.DEFAULT, null),
    HOME_PAGE(ViewCategory.ROOT, "root/Main"),
    ROOT_BAR(ViewCategory.PANEL, "panel/RootBar");

    public final ViewCategory category;
    public final String location;

    private ViewType(ViewCategory category, String location) {
        this.category = category;
        this.location = location;
    }
}
