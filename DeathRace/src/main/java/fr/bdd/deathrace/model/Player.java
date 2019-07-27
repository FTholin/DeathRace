package fr.bdd.deathrace.model;

import fr.bdd.deathrace.model.body.Type;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    private final int id = UUID.randomUUID().hashCode();
    private String pseudo;
    private int runnerUid;
    private int showRunnerUid;
    private Type type;
    private boolean ready;
    private boolean bot;

    /**
     * Constructor.
     */
    public Player() {
        this.pseudo = null;
        this.runnerUid = -1;
        this.showRunnerUid = -1;
        this.type = Type.values()[0];
        this.ready = false;
        this.bot = false;
    }

    public int getId() {
        return id;
    }

    public int getRunnerUid() {
        return runnerUid;
    }

    public void setRunnerUid(int runnerUid) {
        this.runnerUid = runnerUid;
    }

    public int getShowRunnerUid() {
        return showRunnerUid;
    }

    public void setShowRunnerUid(int showRunnerUid) {
        this.showRunnerUid = showRunnerUid;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    /**
     * Copy all data of player except pseudo to current player.
     * @param player player
     */
    public void updateExceptPseudo(Player player) {
        this.setRunnerUid(player.getRunnerUid());
        this.setShowRunnerUid(player.getShowRunnerUid());
        this.setType(player.getType());
        this.setReady(player.isReady());
        this.setBot(player.isBot());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            Player other = (Player) obj;
            return id == other.getId();
        }
    }

    @Override
    public int hashCode() {
        return id;
    }
}
