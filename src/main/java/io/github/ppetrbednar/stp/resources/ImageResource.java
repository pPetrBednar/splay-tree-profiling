package io.github.ppetrbednar.stp.resources;

import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Petr Bednář
 */
public class ImageResource implements IApplicationResource<Image> {

    private static final Logger LOG = LogManager.getLogger(ImageResource.class);
    private final Resource type;
    private Image resource;

    public ImageResource(Resource type) {
        this.type = type;
        File file = new File(type.location);

        try {
            resource = new Image(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            LOG.error("Missing resource: " + type.location);
            System.out.println("Missing resource: " + type.location);
            System.exit(1);
        }
    }

    @Override
    public Image getResource() {
        return resource;
    }
}
