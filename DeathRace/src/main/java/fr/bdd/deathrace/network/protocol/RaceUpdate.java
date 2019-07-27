package fr.bdd.deathrace.network.protocol;

import fr.bdd.deathrace.model.Race;
import fr.bdd.deathrace.model.body.AbstractMobileBody;

import java.io.Serializable;
import java.util.Map;

public class RaceUpdate implements Serializable {
    private RaceOperation raceOperation;
    private Race race;
    private Map<Integer, AbstractMobileBody> positions;

    public RaceUpdate(Race race) {
        this.race = race;
        this.raceOperation = RaceOperation.INIT_RACE;
    }

    public RaceUpdate(Map<Integer, AbstractMobileBody> positions) {
        this.positions = positions;
        this.raceOperation = RaceOperation.UPDATE_POSITION;
    }

    public RaceOperation getOperation() {
        return raceOperation;
    }

    public Race getRace() {
        return race;
    }

    public Map<Integer, AbstractMobileBody> getPositions() {
        return positions;
    }
}
