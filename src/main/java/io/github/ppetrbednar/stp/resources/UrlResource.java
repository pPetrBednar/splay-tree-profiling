package io.github.ppetrbednar.stp.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlResource implements IApplicationResource<URI> {

    private static final Logger LOG = LogManager.getLogger(UrlResource.class);
    private final Resource type;
    private URI resource;

    public UrlResource(Resource type) {
        this.type = type;
        try {
            resource = new URI(type.location);
        } catch (URISyntaxException e) {
            LOG.error(e);
            System.out.println("URL format not valid: " + type.location);
        }
    }

    @Override
    public URI getResource() {
        return resource;
    }
}
