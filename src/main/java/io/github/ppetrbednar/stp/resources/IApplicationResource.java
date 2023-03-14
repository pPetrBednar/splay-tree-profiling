package io.github.ppetrbednar.stp.resources;

/**
 * @author Petr Bednář
 */
public interface IApplicationResource<T> {
    public T getResource();

    public static IApplicationResource getResourceByType(Resource resource) {
        return switch (resource.type) {
            case FILE -> new FileResource(resource);
            case IMAGE -> new ImageResource(resource);
            case URL -> new UrlResource(resource);
            default -> null;
        };
    }
}
