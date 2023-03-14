package io.github.ppetrbednar.stp.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @author Petr Bednář
 */
public class FileResource implements IApplicationResource<File> {

    private static final Logger LOG = LogManager.getLogger(FileResource.class);
    private final Resource type;
    private final File resource;

    public FileResource(Resource type) {
        this.type = type;
        resource = new File(type.location);

        if (type.required && !resource.exists()) {
            LOG.error("Missing resource: " + type.location);
            System.out.println("Missing resource: " + type.location);
            System.exit(1);
        }
    }

    @Override
    public File getResource() {
        return resource;
    }
}
