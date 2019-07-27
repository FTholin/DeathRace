package fr.bdd.deathrace;

import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassConstructor {

    private static final Logger logger = Logger.getLogger(ClassConstructor.class);
    Object object;

    /**
     * ClassConstructor's constructor with two floats (for objects of the map).
     * @param string the name of the class that we want to instantiate.
     * @param xPos the x position of the object.
     * @param yPos the y position of the object.
     */
    public ClassConstructor(String string, Long xPos, Long yPos) {
        Class<?> cl;
        object = null;
        try {
            cl = Class.forName(string);
            Constructor<?> cons = cl.getConstructor(Long.class, Long.class);
            object = cons.newInstance(xPos, yPos);
        } catch (ClassNotFoundException e) {
            logger.error("Class not found : " + e.getMessage());
        } catch (NoSuchMethodException e) {
            logger.error("No such method : " + e.getMessage());
        } catch (InstantiationException e) {
            logger.error("Instantiation problem : " + e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error("Illegal access : " + e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error("Invocation target problem : " + e.getMessage());
        }
    }

    /**
     * Returns the instantiated object.
     * @return the instantiated object.
     */
    public Object getObject() {
        return object;
    }
}
