package com.mundosonoro.models;

public class CenarioRima {
    public String palavraPrincipal;
    public String respostaCorreta;
    public String[] opcoes;
    public String categoria;
    public int dificuldade;

    public CenarioRima(String palavraPrincipal, String respostaCorreta, String[] opcoes, String categoria, int dificuldade) {
        this.palavraPrincipal = palavraPrincipal;
        this.respostaCorreta = respostaCorreta;
        this.opcoes = opcoes;
        this.categoria = categoria;
        this.dificuldade = dificuldade;
    }
}