package fr.bdd.deathrace.network.protocol;

import fr.bdd.deathrace.model.Lobby;
import fr.bdd.deathrace.model.Player;
import fr.bdd.deathrace.model.Race;
import fr.bdd.deathrace.model.body.AbstractMobileBody;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RaceUpdateTest {

    @Test
    public void getRace() {
        Race race = new Race(new Lobby(new Player(), 5));
        RaceUpdate raceUpdate = new RaceUpdate(race);

        assertEquals(race, raceUpdate.getRace());
        assertNull(raceUpdate.getPositions());
        assertEquals(RaceOperation.INIT_RACE, raceUpdate.getOperation());
    }

    @Test
    public void getPositions() {
        Map<Integer, AbstractMobileBody> positions = new HashMap<>();
        RaceUpdate raceUpdate = new RaceUpdate(positions);

        assertEquals(positions, raceUpdate.getPositions());
        assertNull(raceUpdate.getRace());
        assertEquals(RaceOperation.UPDATE_POSITION, raceUpdate.getOperation());
    }
}