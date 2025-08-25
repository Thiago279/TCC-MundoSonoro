package com.mundosonoro.models;

public class CenarioIdioma {
    public String palavraOriginal;
    public String[] opcoes;
    public String respostaCorreta;
    public String categoria;
    public int nivel;

    public CenarioIdioma(String palavraOriginal, String respostaCorreta, String[] opcoes, String categoria, int nivel) {
        this.palavraOriginal = palavraOriginal;
        this.respostaCorreta = respostaCorreta;
        this.opcoes = opcoes;
        this.categoria = categoria;
        this.nivel = nivel;
    }

    public CenarioIdioma(String palavraOriginal, String respostaCorreta, String[] opcoes, String categoria) {
        this(palavraOriginal, respostaCorreta, opcoes, categoria, 1);
    }
}