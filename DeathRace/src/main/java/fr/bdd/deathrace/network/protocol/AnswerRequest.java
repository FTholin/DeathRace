package fr.bdd.deathrace.network.protocol;

import java.io.Serializable;

public class AnswerRequest implements Serializable {
    private Request request;
    private Serializable response;

    public AnswerRequest(Request request, Serializable response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Object getResponse() {
        return response;
    }
}
