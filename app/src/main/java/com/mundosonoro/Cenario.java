package com.mundosonoro;

public class Cenario {
    public String[] opcoes;
    public String respostaCorreta;
    public int somResId;
    public int tipo; //tipo 0 -> adivinhar cenario com base no som | tipo 1 -> adivinhar som com base no cenario

    public Cenario(String respostaCorreta, String[] opcoes, int somResId, int tipo){
        this.respostaCorreta = respostaCorreta;
        this.opcoes = opcoes;
        this.somResId = somResId;
        this.tipo = tipo;
    }
}
