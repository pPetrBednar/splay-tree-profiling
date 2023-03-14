package io.github.ppetrbednar.stp.system;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import io.github.ppetrbednar.stp.resources.Resource;
import io.github.ppetrbednar.stp.resources.ResourceManager;
import io.github.ppetrbednar.stp.ui.window.alert.AlertManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author Petr Bednář
 */
public class Configuration {

    private static HashMap<Type, Object> settings;
    private static final Logger LOG = LogManager.getLogger(Configuration.class);
    private static final File CONF = ResourceManager.getFileResource(Resource.CONFIG_FILE);

    static {
        if (!CONF.exists()) {
            reloadDefault();
        }

        reload();
    }

    public static void reload() {
        try {
            settings = new HashMap<>();

            String jsonString = FileUtils.readFileToString(CONF, StandardCharsets.UTF_8);

            JsonObject json = (JsonObject) Jsoner.deserialize(jsonString);
            for (Type type : Type.values()) {
                Object data = json.get(type.name());

                if (data == null) {
                    LOG.error("Required config data missing.");
                    AlertManager am = new AlertManager(AlertManager.AlertType.FAILURE, "Required config data missing. Reloading default config.", false);
                    am.show();
                    reloadDefault();
                    return;
                }

                settings.put(type, data);
            }
        } catch (IOException | JsonException | ClassCastException ex) {
            LOG.error(ex);
            AlertManager am = new AlertManager(AlertManager.AlertType.FAILURE, "Required config files failed to load.", false);
            am.showAndWait(() -> {
                System.exit(0);
            });
        }
    }

    public static void reloadDefault() {
        JsonObject json = new JsonObject();

        for (Type type : Type.values()) {
            json.put(type.name(), type.getDefaultValue());
        }

        try {
            if (!CONF.exists()) {
                CONF.getParentFile().mkdirs();
                CONF.createNewFile();
            }
            FileUtils.writeStringToFile(CONF, Jsoner.prettyPrint(json.toJson()), StandardCharsets.UTF_8, false);
        } catch (IOException ex) {
            LOG.error(ex);
            AlertManager am = new AlertManager(AlertManager.AlertType.FAILURE, "Application failed to create required files.", false);
            am.showAndWait(() -> {
                System.exit(0);
            });
        }

    }

    public static void saveCurrent() {
        JsonObject json = new JsonObject();

        for (Type type : settings.keySet()) {
            json.put(type.name(), settings.get(type));
        }

        try {
            if (!CONF.exists()) {
                CONF.getParentFile().mkdirs();
                CONF.createNewFile();
            }
            FileUtils.writeStringToFile(CONF, Jsoner.prettyPrint(json.toJson()), StandardCharsets.UTF_8, false);
        } catch (IOException ex) {
            LOG.error(ex);
            AlertManager am = new AlertManager(AlertManager.AlertType.FAILURE, "Application failed to save current configuration.", false);
            am.showAndWait(() -> {
                System.exit(0);
            });
        }
    }

    public static Object getSetting(Type type) {
        return settings.get(type);
    }

    public static void setSetting(Type type, Object newValue) {
        settings.put(type, newValue);
    }

    public enum Type {
        API_KEY(""),
        MOVIE_CREDITS_CAST(15),
        MOVIE_CREDITS_DIRECTOR(3),
        MOVIE_CREDITS_SCREENPLAYER(3),
        MOVIE_CREDITS_PRODUCER(3),
        MOVIE_CREDITS_CREW(0),
        SERIES_CREDITS_CAST(30),
        SERIES_CREDITS_DIRECTOR(10),
        SERIES_CREDITS_PRODUCER(10),
        SERIES_CREDITS_CREW(0),
        SERIES_CREDITS_GUEST_STARS(0);

        private final Object defaultValue;

        private Type(Object defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public static Type getValueOf(String type) {
            try {
                return valueOf(type);
            } catch (IllegalArgumentException | NullPointerException e) {
                return null;
            }
        }
    }
}
