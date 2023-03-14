package io.github.ppetrbednar.stp.resources;

import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URI;
import java.util.HashMap;

/**
 * @author Petr Bednář
 */
public class ResourceManager {

    private static final Logger LOG = LogManager.getLogger(ResourceManager.class);
    public static final ResourceManager instance;

    private final HashMap<Resource, IApplicationResource> resources;

    static {
        instance = new ResourceManager();
    }

    private ResourceManager() {
        resources = new HashMap<>();
        for (Resource resource : Resource.values()) {
            resources.put(resource, IApplicationResource.getResourceByType(resource));
        }
    }

    @SuppressWarnings("unchecked")
    public static File getFileResource(Resource resource) {
        return resource.type == ResourceType.FILE ? ((IApplicationResource<File>) instance.resources.get(resource)).getResource() : null;
    }

    @SuppressWarnings("unchecked")
    public static Image getImageResource(Resource resource) {
        return resource.type == ResourceType.IMAGE ? ((IApplicationResource<Image>) instance.resources.get(resource)).getResource() : null;
    }

    @SuppressWarnings("unchecked")
    public static URI getUrlResource(Resource resource) {
        return resource.type == ResourceType.URL ? ((IApplicationResource<URI>) instance.resources.get(resource)).getResource() : null;
    }
}
