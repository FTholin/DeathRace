package fr.bdd.deathrace.tools;

import fr.bdd.deathrace.controller.Controller;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Class to persist player data in text file.
 */
public class Persist {

    private static final Logger logger = Logger.getLogger(Persist.class);
    private static final File FILE = new File("storage.txt");
    private static final String ERROR_READING = "Error reading file '";

    /**
     * To delete the default public one.
     */
    private Persist() {
    }

    /**
     * Get pseudo user from text file.
     *
     * @return pseudo player.
     */
    public static String getUserPseudo() {
        return getUserData(0);
    }

    public static String getWinsNumber() {
        return getUserData(1);
    }

    public static String getGamesNumber() {
        return getUserData(2);
    }

    public static String getBestTime() {
        return getUserData(3);
    }

    public static String getWorstTime() {
        return getUserData(4);
    }

    public static void setUserPseudonyme(String pseudo) {
        setUserData(pseudo, 0);
    }

    public static void setUserWinsNumber(String winNumber) {
        setUserData(winNumber, 1);
    }

    public static void setUserGamesNumber(String gameNumber) {
        setUserData(gameNumber, 2);
    }

    public static void setUserBestTime(String bestTime) {
        setUserData(bestTime, 3);
    }

    public static void setUserWorstTime(String worstTime) {
        setUserData(worstTime, 4);
    }

    /**
     * Set user specific data.
     *
     * @param newData data to alter
     * @param index index in text file
     */
    private static void setUserData(String newData, int index) {
        // This will reference one line at a time
        String line;

        try (FileReader fileReader = new FileReader(FILE)) {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();
            String[] datas = line.split(",");

            datas[index] = newData;

            try (FileWriter fileWriter
                    = new FileWriter(FILE)) {

                BufferedWriter bufferedWriter
                        = new BufferedWriter(fileWriter);

                bufferedWriter.write(String.join(",", datas));
                bufferedWriter.close();
            }

        } catch (FileNotFoundException ex) {
            logger.error("Unable to open file '" + FILE + "'");
        } catch (IOException ex) {
            logger.error(ERROR_READING + FILE + "'");
        } catch (NullPointerException e) {
            logger.error("Error processing file '" + FILE + "'");
            saveUserPseudo(Controller.getInstance().getPlayer().getPseudo());
        }
    }

    private static String getUserData(int index) {
        // This will reference one line at a time
        String line;

        try (FileReader fileReader = new FileReader(FILE)) {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            if (line == null) {
                return null;
            }

            String[] datas = line.split(",");

            // Always removePlayer files.
            bufferedReader.close();
            if (datas.length < 5) {
                return null;
            }
            return datas[index];

        } catch (FileNotFoundException ex) {
            logger.error("Unable to open file '" + FILE + "'");
        } catch (IOException ex) {
            logger.error(ERROR_READING + FILE + "'");
        }
        return null;
    }

    /**
     * save pseudo player in text file.
     *
     * @param pseudo pseudo player
     */
    public static void saveUserPseudo(String pseudo) {
        boolean fileExists = FILE.exists();

        if (!fileExists) {
            try {
                fileExists = FILE.createNewFile();
            } catch (IOException e) {
                logger.error("Error when creating file" + FILE + "'");
            }
        }

        if (fileExists) {
            try (FileWriter fw = new FileWriter(FILE)) {

                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(pseudo + ", 0, 0, 0, 0");
                bw.flush();
            } catch (IOException e) {
                logger.error(ERROR_READING + FILE + "'");
            }
        }
    }

    /**
     * reset to 0 the statistics of the player.
     */
    public static void reset() {
        setUserGamesNumber("0");
        setUserWinsNumber("0");
        setUserBestTime("0");
        setUserWorstTime("0");
    }
}
