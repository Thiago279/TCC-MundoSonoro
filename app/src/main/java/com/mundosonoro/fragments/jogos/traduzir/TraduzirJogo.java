package com.mundosonoro.fragments.jogos.traduzir;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mundosonoro.R;
import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentTraduzirJogoBinding;
import com.mundosonoro.models.CenarioIdioma;
import com.mundosonoro.utils.PontuacaoManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TraduzirJogo extends Fragment {
    private MediaPlayer mediaPlayer;
    private FragmentTraduzirJogoBinding binding;
    private List<CenarioIdioma> cenarios;
    private CenarioIdioma cenarioAtual;
    private int pontos = 0;
    private int rodadaAtual = 1;
    private final int MAX_RODADAS = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTraduzirJogoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        inicializarCenarios();


        // Botão para repetir a pergunta
        binding.btnRepetirPergunta.setOnClickListener(v -> falarPergunta());

        // Clique dos botões de opções
        binding.opcao1.setOnClickListener(v -> checarResposta(0));
        binding.opcao2.setOnClickListener(v -> checarResposta(1));
        binding.opcao3.setOnClickListener(v -> checarResposta(2));
        binding.opcao4.setOnClickListener(v -> checarResposta(3));

        sortearCenario();

        return view;
    }

    private void inicializarCenarios() {
        cenarios = new ArrayList<>();

        // FAMÍLIA
        cenarios.add(new CenarioIdioma("irmão", "brother",
                new String[]{"brother", "sister", "baby", "boy"}, "Família", 1));

        cenarios.add(new CenarioIdioma("irmã", "sister",
                new String[]{"brother", "sister", "baby", "boy"}, "Família", 1));

        cenarios.add(new CenarioIdioma("bebê", "baby",
                new String[]{"baby", "boy", "brother", "sister"}, "Família", 1));

        cenarios.add(new CenarioIdioma("menino", "boy",
                new String[]{"boy", "baby", "brother", "sister"}, "Família", 1));

        cenarios.add(new CenarioIdioma("família", "family",
                new String[]{"family", "brother", "sister", "baby"}, "Família", 1));

        // CASA
        cenarios.add(new CenarioIdioma("casa", "house",
                new String[]{"house", "table", "window", "door"}, "Casa", 1));

        cenarios.add(new CenarioIdioma("mesa", "table",
                new String[]{"table", "house", "window", "door"}, "Casa", 1));

        cenarios.add(new CenarioIdioma("janela", "window",
                new String[]{"window", "door", "table", "house"}, "Casa", 1));

        cenarios.add(new CenarioIdioma("porta", "door",
                new String[]{"door", "window", "table", "house"}, "Casa", 1));

        cenarios.add(new CenarioIdioma("livro", "book",
                new String[]{"book", "toy", "table", "door"}, "Casa", 1));

        cenarios.add(new CenarioIdioma("brinquedo", "toy",
                new String[]{"toy", "book", "table", "house"}, "Casa", 1));

        // ANIMAIS
        cenarios.add(new CenarioIdioma("cachorro", "dog",
                new String[]{"dog", "cat", "bird", "mouse"}, "Animais", 1));

        cenarios.add(new CenarioIdioma("gato", "cat",
                new String[]{"cat", "dog", "bird", "chicken"}, "Animais", 1));

        cenarios.add(new CenarioIdioma("pássaro", "bird",
                new String[]{"bird", "chicken", "cat", "dog"}, "Animais", 1));

        cenarios.add(new CenarioIdioma("galinha", "chicken",
                new String[]{"chicken", "bird", "pig", "mouse"}, "Animais", 1));

        cenarios.add(new CenarioIdioma("porco", "pig",
                new String[]{"pig", "chicken", "dog", "cat"}, "Animais", 1));

        cenarios.add(new CenarioIdioma("rato", "mouse",
                new String[]{"mouse", "cat", "dog", "bird"}, "Animais", 1));

        // COMIDA
        cenarios.add(new CenarioIdioma("maçã", "apple",
                new String[]{"apple", "lemon", "chicken", "cat"}, "Comida", 1));

        cenarios.add(new CenarioIdioma("limão", "lemon",
                new String[]{"lemon", "apple", "chicken", "bird"}, "Comida", 1));

        // CORES
        cenarios.add(new CenarioIdioma("azul", "blue",
                new String[]{"blue", "green", "black", "white"}, "Cores", 1));

        cenarios.add(new CenarioIdioma("verde", "green",
                new String[]{"green", "blue", "brown", "pink"}, "Cores", 1));

        cenarios.add(new CenarioIdioma("preto", "black",
                new String[]{"black", "white", "blue", "green"}, "Cores", 1));

        cenarios.add(new CenarioIdioma("branco", "white",
                new String[]{"white", "black", "pink", "brown"}, "Cores", 1));

        cenarios.add(new CenarioIdioma("marrom", "brown",
                new String[]{"brown", "pink", "black", "white"}, "Cores", 1));

        cenarios.add(new CenarioIdioma("rosa", "pink",
                new String[]{"pink", "brown", "blue", "green"}, "Cores", 1));

        // NATUREZA
        cenarios.add(new CenarioIdioma("lua", "moon",
                new String[]{"moon", "star", "sky", "snow"}, "Natureza", 1));

        cenarios.add(new CenarioIdioma("estrela", "star",
                new String[]{"star", "moon", "sky", "snow"}, "Natureza", 1));

        cenarios.add(new CenarioIdioma("céu", "sky",
                new String[]{"sky", "moon", "star", "snow"}, "Natureza", 1));

        cenarios.add(new CenarioIdioma("neve", "snow",
                new String[]{"snow", "sky", "moon", "star"}, "Natureza", 1));

        // TRANSPORTE
        cenarios.add(new CenarioIdioma("carro", "car",
                new String[]{"car", "bike", "house", "toy"}, "Transporte", 1));

        cenarios.add(new CenarioIdioma("bicicleta", "bike",
                new String[]{"bike", "car", "toy", "house"}, "Transporte", 1));

        // AÇÕES
        cenarios.add(new CenarioIdioma("dormir", "sleep",
                new String[]{"sleep", "sit", "play", "open"}, "Ações", 2));

        cenarios.add(new CenarioIdioma("sentar", "sit",
                new String[]{"sit", "sleep", "play", "close"}, "Ações", 2));

        cenarios.add(new CenarioIdioma("brincar", "play",
                new String[]{"play", "sit", "sleep", "open"}, "Ações", 2));

        cenarios.add(new CenarioIdioma("abrir", "open",
                new String[]{"open", "close", "door", "window"}, "Ações", 2));

        cenarios.add(new CenarioIdioma("fechar", "close",
                new String[]{"close", "open", "door", "window"}, "Ações", 2));
    }

    private void sortearCenario() {
        Random random = new Random();
        int index = random.nextInt(cenarios.size());
        cenarioAtual = cenarios.get(index);

        // Cria uma cópia da lista de opções e embaralha
        List<String> opcoesEmbaralhadas = new ArrayList<>();
        Collections.addAll(opcoesEmbaralhadas, cenarioAtual.opcoes);
        Collections.shuffle(opcoesEmbaralhadas);

        // Atualiza texto dos botões com opções embaralhadas
        binding.opcao1.setText(opcoesEmbaralhadas.get(0).toUpperCase());
        binding.opcao2.setText(opcoesEmbaralhadas.get(1).toUpperCase());
        binding.opcao3.setText(opcoesEmbaralhadas.get(2).toUpperCase());
        binding.opcao4.setText(opcoesEmbaralhadas.get(3).toUpperCase());

        // Atualiza informações da rodada
        binding.rodada.setText("Rodada: " + rodadaAtual + "/" + MAX_RODADAS);
        //binding.categoria.setText("Categoria: " + cenarioAtual.categoria);

        binding.getRoot().announceForAccessibility("Rodada" + rodadaAtual);

        //Fala a pergunta após um delay para dar tempo do anúncio da rodada terminar
        new android.os.Handler().postDelayed(() -> {
            falarPergunta();
        }, 1500);

    }

    private void falarPergunta() {
        binding.getRoot().announceForAccessibility("Como se fala " + cenarioAtual.palavraOriginal + " em inglês?");
    }

    private void checarResposta(int indiceEscolhido) {
        String respostaEscolhida = "";

        // Obtém o texto do botão clicado com base no índice
        switch (indiceEscolhido) {
            case 0:
                respostaEscolhida = binding.opcao1.getText().toString();
                break;
            case 1:
                respostaEscolhida = binding.opcao2.getText().toString();
                break;
            case 2:
                respostaEscolhida = binding.opcao3.getText().toString();
                break;
            case 3:
                respostaEscolhida = binding.opcao4.getText().toString();
                break;
        }

        if (respostaEscolhida.equalsIgnoreCase(cenarioAtual.respostaCorreta)) {
            pontos += 10;
            binding.pontos.setText("Pontos: " + pontos);

            tocarFeedback(R.raw.correct_sfx);

            new Handler().postDelayed(() -> {
                binding.getRoot().announceForAccessibility("Correto! Mais 10 pontos!");
            }, 700);

        } else {
            tocarFeedback(R.raw.wrong_sfx);

            new Handler().postDelayed(() -> {
                binding.getRoot().announceForAccessibility("Incorreto!");
            }, 700);
        }


        rodadaAtual++;

        // Verifica se o jogo acabou
        if (rodadaAtual > MAX_RODADAS) {
            finalizarJogo();
        } else {
            // Próxima rodada após delay
            new android.os.Handler().postDelayed(() -> {
                sortearCenario();
            }, 3000);
        }
    }

    private void finalizarJogo() {
        // Salva o high score
        PontuacaoManager pontuacaoManager = new PontuacaoManager(getContext());
        pontuacaoManager.salvarHighScoreTraduzir(pontos);

        new android.os.Handler().postDelayed(() -> {
            String resultado = "Jogo finalizado! Você fez " + pontos + " pontos!";
            if (pontos >= 80) {
                resultado += " Excelente!";
            } else if (pontos >= 60) {
                resultado += " Muito bom!";
            } else if (pontos >= 40) {
                resultado += " Bom trabalho!";
            } else {
                resultado += " Continue praticando!";
            }
            binding.getRoot().announceForAccessibility(resultado);

            // Volta para a tela inicial após 8 segundos
            new android.os.Handler().postDelayed(() -> {
                ((MainActivity) getActivity()).voltarParaMenu();
            }, 8000);

        }, 2000);
    }

    private void tocarFeedback(int resourceId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(getContext(), resourceId);
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0.5f, 0.5f); // controla volume (E/D)
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}