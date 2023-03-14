package io.github.ppetrbednar.stp.resources;

/**
 * @author Petr Bednář
 */
public enum Resource {
    APPLICATION_ICON("./app/res/icon.png", ResourceType.IMAGE, true),
    CONFIG_FILE("./app/conf/conf.json", ResourceType.FILE, false);


    public final String location;
    public final ResourceType type;
    public final boolean required;

    Resource(String location, ResourceType type, boolean required) {
        this.location = location;
        this.type = type;
        this.required = required;
    }
}
