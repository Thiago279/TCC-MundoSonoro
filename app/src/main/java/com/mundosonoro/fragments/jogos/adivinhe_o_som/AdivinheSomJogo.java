package com.mundosonoro.fragments.jogos.adivinhe_o_som;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mundosonoro.models.Cenario;
import com.mundosonoro.R;
import com.mundosonoro.databinding.FragmentAdivinheSomJogoBinding; // Corrigido: nome do binding

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class AdivinheSomJogo extends Fragment {
    private MediaPlayer mediaPlayer;
    private TextToSpeech textToSpeech;
    private FragmentAdivinheSomJogoBinding binding; // Corrigido: nome do binding
    private List<Cenario> cenarios;
    private Cenario cenarioAtual;
    private int pontos = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdivinheSomJogoBinding.inflate(inflater, container, false); // Corrigido
        View view = binding.getRoot();

        inicializarCenarios();
        sortearCenario();

        //inicializa TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS){
                int result = textToSpeech.setLanguage(new Locale("pt", "BR"));

                //Fala o subminigame no primeiro round
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED){
                    String nomeSubminigame = binding.subminigame.getText().toString();
                    textToSpeech.speak(nomeSubminigame, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

        //disparar som
        binding.btnOuvirSom.setOnClickListener(v -> tocarSom(cenarioAtual.somResId));

        //Clique dos botões
        binding.opcao1.setOnClickListener(v -> checarResposta(0));
        binding.opcao2.setOnClickListener(v -> checarResposta(1));
        binding.opcao3.setOnClickListener(v -> checarResposta(2));
        binding.opcao4.setOnClickListener(v -> checarResposta(3));

        return view;
    }

    //tipo 0 -> adivinhar cenario com base no som
    //tipo 1 -> adivinhar som com base no cenario
    private void inicializarCenarios(){
        cenarios = new ArrayList<>();

        cenarios.add(new Cenario("Cidade", new String[]{"Floresta", "Cidade", "Fazenda", "Aeroporto"}, R.raw.som_cidade, 0));
        cenarios.add(new Cenario("Aeroporto", new String[]{"Floresta", "Cidade", "Aeroporto", "Rodoviária"},R.raw.som_aviao, 0));
        cenarios.add(new Cenario("Escola", new String[]{"Supermercado", "Escola", "Hospital", "Biblioteca"}, R.raw.som_criancas, 0));
        cenarios.add(new Cenario("Estádio", new String[]{"Teatro", "Parque", "Estádio", "Supermercado"},R.raw.som_estadio, 0));
        cenarios.add(new Cenario("Fazenda", new String[]{"Supermercado", "Parque", "Fazenda", "Cidade"}, R.raw.som_fazenda, 0));
        cenarios.add(new Cenario("Leão Rugindo", new String[]{"Elefante trombeteando", "Leão Rugindo", "Lobo uivando", "Cavalo relinchando"}, R.raw.som_leao, 1));
        cenarios.add(new Cenario("Golfinho fazendo barulho", new String[]{"Golfinho fazendo barulho", "Ondas do mar", "Baleia cantando", "Barco navegando"}, R.raw.som_golfinho, 1));
        cenarios.add(new Cenario("Grilo cantando", new String[]{"Grilo cantando", "Galo cantando", "Passarinhos piando", "Coruja fazendo barulho"}, R.raw.som_grilo, 1));
        cenarios.add(new Cenario("Música do carrossel", new String[]{"Montanha russa descendo", "Música do carrossel", "Pipoca estourando", "Crianças no pula-pula"}, R.raw.som_carrossel, 1));
    }

    private int sortearCenario(){
        Random random = new Random();
        int index = random.nextInt(cenarios.size());
        cenarioAtual = cenarios.get(index);

        //Cria uma copia da lista de opcoes e embaralha
        List<String> opcoesEmbaralhadas = new ArrayList<>();
        Collections.addAll(opcoesEmbaralhadas, cenarioAtual.opcoes);
        Collections.shuffle(opcoesEmbaralhadas);

        // Atualiza texto dos botões com opcoes embaralhadas
        binding.opcao1.setText(opcoesEmbaralhadas.get(0).toUpperCase());
        binding.opcao2.setText(opcoesEmbaralhadas.get(1).toUpperCase());
        binding.opcao3.setText(opcoesEmbaralhadas.get(2).toUpperCase());
        binding.opcao4.setText(opcoesEmbaralhadas.get(3).toUpperCase());

        //Atualiza o texto do subminigame com base no tipo
        if (cenarioAtual.tipo == 0){
            binding.subminigame.setText("Que lugar é esse?");
        } else if(cenarioAtual.tipo == 1){
            binding.subminigame.setText("Que som é esse?");
        }

        return cenarioAtual.somResId;
    }

    private void tocarSom(int somResource){
        pararSomAtual();

        mediaPlayer = MediaPlayer.create(getContext(), somResource);

        if (mediaPlayer != null){
            mediaPlayer.start();

            //libera recursos qnd o som terminar
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        }
    }

    private void pararSomAtual(){
        if (mediaPlayer != null){
            try{
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
            } catch (IllegalStateException e){
                e.printStackTrace(); //debug
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void checarResposta(int indiceEscolhido){
        String respostaEscolhida= "";

        // obtem o texto do botao clicado com base no indice
        switch (indiceEscolhido){
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

        //para o som do cenario imediatamente
        pararSomAtual();

        if (respostaEscolhida.equalsIgnoreCase(cenarioAtual.respostaCorreta)){
            pontos += 10;
            binding.pontos.setText("Pontos: " + pontos);
            tocarFeedback(R.raw.correct_sfx);
            textToSpeech.speak("Mais 10 pontos!", TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tocarFeedback(R.raw.wrong_sfx);
        }

        sortearCenario();
        //fala o subminigame dps de 2,3 segundos
        new android.os.Handler().postDelayed(() -> {
            textToSpeech.speak(binding.subminigame.getText(), TextToSpeech.QUEUE_FLUSH, null, null);
        }, 2300);
    }

    private void tocarFeedback(int resourceId){
        pararSomAtual();
        mediaPlayer = MediaPlayer.create(getContext(), resourceId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        pararSomAtual();
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}