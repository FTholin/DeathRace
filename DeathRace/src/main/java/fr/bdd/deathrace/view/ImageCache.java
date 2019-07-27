package fr.bdd.deathrace.view;

import javafx.scene.image.Image;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Classe (singleton) de mise en cache des images.
 */
public class ImageCache {
    private static ImageCache instance;
    private ConcurrentMap<String, Image> cache;

    /**
     * Gets lone instance of ImageCache.
     * @return instance
     */
    public static ImageCache getInstance() {
        if (instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    /**
     * Constructor.
     */
    private ImageCache() {
        cache = new ConcurrentHashMap<>();
    }

    /**
     * Gets image if exists.
     * @param path path
     * @return image if exists.
     */
    public static Optional<Image> getImage(String path) {
        ConcurrentMap<String, Image> cache = getInstance().cache;
        try {
            cache.putIfAbsent(path, new Image(path));
            return Optional.ofNullable(cache.get(path));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
