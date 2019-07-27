package fr.bdd.deathrace.model;

import fr.bdd.deathrace.model.body.Type;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PlayerTest {

    @Test
    public void idTest() {
        Player player1 = new Player();
        Player player2 = new Player();

        assertNotEquals(player1.getId(), player2.getId());
    }

    @Test
    public void runnerUidTest() {
        Player player1 = new Player();
        player1.setRunnerUid(1);
        Player player2 = new Player();
        player2.setRunnerUid(2);

        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.getRunnerUid(), player2.getRunnerUid());
        assertEquals(player1.getRunnerUid(), player1.getRunnerUid());
        assertEquals(player2.getRunnerUid(), player2.getRunnerUid());

        player2.setRunnerUid(player1.getRunnerUid());
        assertEquals(player1.getRunnerUid(), player2.getRunnerUid());

    }


    @Test
    public void showRunnerUidTest() {
        Player player1 = new Player();
        player1.setShowRunnerUid(1);
        Player player2 = new Player();
        player2.setShowRunnerUid(2);

        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.getShowRunnerUid(), player2.getShowRunnerUid());
        assertEquals(player1.getShowRunnerUid(), player1.getShowRunnerUid());
        assertEquals(player2.getShowRunnerUid(), player2.getShowRunnerUid());

        player2.setShowRunnerUid(player1.getShowRunnerUid());
        assertEquals(player1.getShowRunnerUid(), player2.getShowRunnerUid());
    }

    @Test
    public void pseudoTest() {
        Player player1 = new Player();
        player1.setPseudo("1");
        Player player2 = new Player();
        player2.setPseudo("2");

        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.getPseudo(), player2.getPseudo());
        assertEquals(player1.getPseudo(), player1.getPseudo());
        assertEquals(player2.getPseudo(), player2.getPseudo());

        player2.setPseudo(player1.getPseudo());
        assertEquals(player1.getPseudo(), player2.getPseudo());

    }

    @Test
    public void typeTest() {
        Player player1 = new Player();
        player1.setType(Type.PONEY);
        Player player2 = new Player();
        player2.setType(Type.CROCODILE);

        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.getType(), player2.getType());
        assertEquals(player1.getType(), player1.getType());
        assertEquals(player2.getType(), player2.getType());

        player2.setType(player1.getType());
        assertEquals(player1.getType(), player2.getType());
    }


    @Test
    public void readyTest() {
        Player player1 = new Player();
        player1.setReady(true);
        Player player2 = new Player();
        player2.setReady(false);

        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.isReady(), player2.isReady());
        assertEquals(player1.isReady(), player1.isReady());
        assertEquals(player2.isReady(), player2.isReady());

        player2.setReady(player1.isReady());
        assertEquals(player1.isReady(), player2.isReady());
    }


    @Test
    public void botTest() {
        Player player1 = new Player();
        player1.setBot(true);
        Player player2 = new Player();
        player2.setBot(false);

        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.isBot(), player2.isBot());
        assertEquals(player1.isBot(), player1.isBot());
        assertEquals(player2.isBot(), player2.isBot());

        player2.setBot(player1.isBot());
        assertEquals(player1.isBot(), player2.isBot());
    }


    @Test
    public void updateExceptPseudoTest() {
        Player player1 = new Player();
        player1.setReady(true);
        player1.setBot(true);
        player1.setType(Type.PONEY);
        player1.setPseudo("1");
        player1.setShowRunnerUid(1);
        player1.setRunnerUid(1);
        Player player2 = new Player();
        player2.setBot(false);
        player2.setReady(false);
        player2.setType(Type.CROCODILE);
        player2.setPseudo("2");
        player2.setShowRunnerUid(2);
        player2.setRunnerUid(2);

        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.isBot(), player2.isBot());
        assertNotEquals(player1.isReady(), player2.isReady());
        assertNotEquals(player1.getType(), player2.getType());
        assertNotEquals(player1.getShowRunnerUid(), player2.getShowRunnerUid());
        assertNotEquals(player1.getRunnerUid(), player2.getRunnerUid());
        assertNotEquals(player1.getPseudo(), player2.getPseudo());

        player2.updateExceptPseudo(player1);

        assertNotEquals(player1.getId(), player2.getId());
        assertEquals(player1.isBot(), player2.isBot());
        assertEquals(player1.isReady(), player2.isReady());
        assertEquals(player1.getType(), player2.getType());
        assertEquals(player1.getShowRunnerUid(), player2.getShowRunnerUid());
        assertEquals(player1.getRunnerUid(), player2.getRunnerUid());
        assertNotEquals(player1.getPseudo(), player2.getPseudo());
    }

    @Test
    public void equalsTest() {
        Player player1 = new Player();
        player1.setReady(true);
        player1.setBot(true);
        player1.setType(Type.PONEY);
        player1.setPseudo("1");
        player1.setShowRunnerUid(1);
        player1.setRunnerUid(1);
        Player player2 = new Player();
        player2.setBot(true);
        player2.setReady(true);
        player2.setType(Type.PONEY);
        player2.setPseudo("1");
        player2.setShowRunnerUid(1);
        player2.setRunnerUid(1);

        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1, player2);
        assertEquals(player1, player1);
        assertEquals(player2, player2);
        assertNotEquals(player1, 2);
    }

    @Test
    public void hashCodeTest() {
        Player player1 = new Player();
        player1.setReady(true);
        player1.setBot(true);
        player1.setType(Type.PONEY);
        player1.setPseudo("1");
        player1.setShowRunnerUid(1);
        player1.setRunnerUid(1);
        Player player2 = new Player();
        player2.setBot(true);
        player2.setReady(true);
        player2.setType(Type.PONEY);
        player2.setPseudo("1");
        player2.setShowRunnerUid(1);
        player2.setRunnerUid(1);

        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.hashCode(), player2.hashCode());
        assertEquals(player1.hashCode(), player1.hashCode());
        assertEquals(player2.hashCode(), player2.hashCode());
    }
}