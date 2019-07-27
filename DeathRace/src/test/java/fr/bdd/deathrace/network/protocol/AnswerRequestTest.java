package fr.bdd.deathrace.network.protocol;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnswerRequestTest {

    @Test
    public void answerRequestTest() {
        Request request = Request.PSEUDO;
        String pseudo = "pseudo";

        AnswerRequest answerRequest = new AnswerRequest(request, pseudo);

        assertEquals(request, answerRequest.getRequest());
        assertEquals(pseudo, answerRequest.getResponse());
    }

}