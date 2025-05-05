package com.mundosonoro;

public class Cenario {
    public String[] opcoes;
    public String respostaCorreta;
    public int somResId;

    public Cenario(String respostaCorreta, String[] opcoes, int somResId){
        this.respostaCorreta = respostaCorreta;
        this.opcoes = opcoes;
        this.somResId = somResId;
    }
}
