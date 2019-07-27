package fr.bdd.deathrace.model.world;

import fr.bdd.deathrace.ClassConstructor;
import fr.bdd.deathrace.model.body.AbstractPhysicBody;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;

public class WorldFactory {

    private static final Logger logger = Logger.getLogger(WorldFactory.class);

    private String path = "src&main&resources&assets&maps&".replaceAll(
            "&", Matcher.quoteReplacement(File.separator));
    private String extension = ".json";

    private World world;

    /**
     * WorldFactory's constructor.
     */
    public WorldFactory(String string) {
        world = new World();
        Object mapFile = null;
        try {
            mapFile = (new JSONParser()).parse(new FileReader(path + string + extension));
        } catch (IOException e) {
            logger.error("Failed to open file : " + e.getMessage()
                    + ". The current directory is : " + System.getProperty("user.dir"));
        } catch (ParseException e) {
            logger.error("Failed to parse the map's json : " + e.getMessage());
        }
        if (mapFile != null) {
            JSONObject mapObject = (JSONObject) mapFile;
            Long x = (Long) mapObject.get("x");
            Long y = (Long) mapObject.get("y");
            world.setSize(new Point2D.Double(x, y));
            world.shape.width = x;
            world.shape.height = y;

            JSONArray objectsArray = (JSONArray) mapObject.get("objects");
            for (Object object : objectsArray) {
                JSONObject jsonObject = (JSONObject) object;
                Long xPos = (Long) jsonObject.get("x");
                Long yPos = (Long) jsonObject.get("y");
                String className = "fr.bdd.deathrace.model.body." + jsonObject.get("name");
                ClassConstructor classConstructor = new ClassConstructor(className, xPos, yPos);
                world.addBody((AbstractPhysicBody) classConstructor.getObject());
            }
        }
    }

    /**
     * Returns the WorldFactory's world.
     *
     * @return the world.
     */
    public World getWorld() {
        return world;
    }
}
