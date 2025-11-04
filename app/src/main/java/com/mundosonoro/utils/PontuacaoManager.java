package com.mundosonoro.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PontuacaoManager {
    private static final String PREFS_NAME = "MundoSonoroPrefs";
    private static final String KEY_HIGH_SCORE_ADIVINHE = "highscore_adivinhe_som";
    private static final String KEY_HIGH_SCORE_RIMAS = "highscore_rimas";
    private static final String KEY_HIGH_SCORE_TRADUZIR = "highscore_traduzir";

    private SharedPreferences prefs;

    public PontuacaoManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Métodos para Adivinhe o Som
    public int getHighScoreAdivinheSom() {
        return prefs.getInt(KEY_HIGH_SCORE_ADIVINHE, 0);
    }

    public void salvarHighScoreAdivinheSom(int pontos) {
        int highScoreAtual = getHighScoreAdivinheSom();
        if (pontos > highScoreAtual) {
            prefs.edit().putInt(KEY_HIGH_SCORE_ADIVINHE, pontos).apply();
        }
    }

    // Métodos para Rimas
    public int getHighScoreRimas() {
        return prefs.getInt(KEY_HIGH_SCORE_RIMAS, 0);
    }

    public void salvarHighScoreRimas(int pontos) {
        int highScoreAtual = getHighScoreRimas();
        if (pontos > highScoreAtual) {
            prefs.edit().putInt(KEY_HIGH_SCORE_RIMAS, pontos).apply();
        }
    }

    // Métodos para Traduzir
    public int getHighScoreTraduzir() {
        return prefs.getInt(KEY_HIGH_SCORE_TRADUZIR, 0);
    }

    public void salvarHighScoreTraduzir(int pontos) {
        int highScoreAtual = getHighScoreTraduzir();
        if (pontos > highScoreAtual) {
            prefs.edit().putInt(KEY_HIGH_SCORE_TRADUZIR, pontos).apply();
        }
    }
}