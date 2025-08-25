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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TraduzirJogo extends Fragment {
    private MediaPlayer mediaPlayer;
    private TextToSpeech textToSpeech;
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
        sortearCenario();

        // Inicializa TTS
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("pt", "BR"));

            }
        });

        // Botão para repetir a pergunta
        binding.btnRepetirPergunta.setOnClickListener(v -> falarPergunta());

        // Clique dos botões de opções
        binding.opcao1.setOnClickListener(v -> checarResposta(0));
        binding.opcao2.setOnClickListener(v -> checarResposta(1));
        binding.opcao3.setOnClickListener(v -> checarResposta(2));
        binding.opcao4.setOnClickListener(v -> checarResposta(3));

        return view;
    }

    private void inicializarCenarios() {
        cenarios = new ArrayList<>();

        // ANIMAIS
        cenarios.add(new CenarioIdioma("cachorro", "dog",
                new String[]{"cat", "dog", "bird", "fish"}, "Animais", 1));

        cenarios.add(new CenarioIdioma("gato", "cat",
                new String[]{"dog", "cat", "mouse", "horse"}, "Animais", 1));

        cenarios.add(new CenarioIdioma("pássaro", "bird",
                new String[]{"fish", "bird", "snake", "frog"}, "Animais", 1));

        cenarios.add(new CenarioIdioma("peixe", "fish",
                new String[]{"bird", "cat", "fish", "turtle"}, "Animais", 1));

        // CORES
        cenarios.add(new CenarioIdioma("vermelho", "red",
                new String[]{"blue", "red", "green", "yellow"}, "Cores", 1));

        cenarios.add(new CenarioIdioma("azul", "blue",
                new String[]{"red", "green", "blue", "black"}, "Cores", 1));

        cenarios.add(new CenarioIdioma("verde", "green",
                new String[]{"yellow", "purple", "green", "orange"}, "Cores", 1));

        // NÚMEROS
        cenarios.add(new CenarioIdioma("um", "one",
                new String[]{"one", "two", "three", "four"}, "Números", 1));

        cenarios.add(new CenarioIdioma("dois", "two",
                new String[]{"one", "two", "five", "six"}, "Números", 1));

        cenarios.add(new CenarioIdioma("três", "three",
                new String[]{"seven", "eight", "three", "nine"}, "Números", 1));

        // FAMÍLIA
        cenarios.add(new CenarioIdioma("mãe", "mother",
                new String[]{"father", "mother", "sister", "brother"}, "Família", 1));

        cenarios.add(new CenarioIdioma("pai", "father",
                new String[]{"mother", "father", "uncle", "aunt"}, "Família", 1));

        // COMIDA
        cenarios.add(new CenarioIdioma("maçã", "apple",
                new String[]{"banana", "orange", "apple", "grape"}, "Comida", 1));

        cenarios.add(new CenarioIdioma("banana", "banana",
                new String[]{"apple", "banana", "cherry", "lemon"}, "Comida", 1));

        // ESCOLA
        cenarios.add(new CenarioIdioma("livro", "book",
                new String[]{"pen", "book", "paper", "desk"}, "Escola", 1));

        cenarios.add(new CenarioIdioma("lápis", "pencil",
                new String[]{"pencil", "eraser", "ruler", "bag"}, "Escola", 1));

        // CORPO HUMANO
        cenarios.add(new CenarioIdioma("cabeça", "head",
                new String[]{"hand", "foot", "head", "arm"}, "Corpo", 2));

        cenarios.add(new CenarioIdioma("mão", "hand",
                new String[]{"leg", "hand", "eye", "nose"}, "Corpo", 2));

        // PALAVRAS MAIS DIFÍCEIS
        cenarios.add(new CenarioIdioma("borboleta", "butterfly",
                new String[]{"butterfly", "dragonfly", "beetle", "spider"}, "Insetos", 2));

        cenarios.add(new CenarioIdioma("elefante", "elephant",
                new String[]{"rhinoceros", "elephant", "hippopotamus", "giraffe"}, "Animais", 2));

        // AÇÕES/VERBOS
        cenarios.add(new CenarioIdioma("correr", "run",
                new String[]{"walk", "jump", "run", "swim"}, "Ações", 3));

        cenarios.add(new CenarioIdioma("nadar", "swim",
                new String[]{"fly", "swim", "climb", "dance"}, "Ações", 3));
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

        //Fala o número da rodada antes da pergunta
        if (textToSpeech != null) {
            String anuncioRodada = "Rodada " + rodadaAtual;
            textToSpeech.speak(anuncioRodada, TextToSpeech.QUEUE_FLUSH, null, null);

            //Fala a pergunta após um delay para dar tempo do anúncio da rodada terminar
            new android.os.Handler().postDelayed(() -> {
                falarPergunta();
            }, 1500);
        }
    }

    private void falarPergunta() {
        if (textToSpeech != null) {
            String pergunta = "Como se fala " + cenarioAtual.palavraOriginal + " em inglês?";
            textToSpeech.speak(pergunta, TextToSpeech.QUEUE_FLUSH, null, null);
        }
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
                textToSpeech.speak("Correto! Mais 10 pontos!", TextToSpeech.QUEUE_FLUSH, null, null);
            }, 700);

        } else {
            tocarFeedback(R.raw.wrong_sfx);

            new Handler().postDelayed(() -> {
                textToSpeech.speak("Incorreto", TextToSpeech.QUEUE_FLUSH, null, null);
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
            textToSpeech.speak(resultado, TextToSpeech.QUEUE_FLUSH, null, null);

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
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}