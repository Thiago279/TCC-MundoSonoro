package com.mundosonoro;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mundosonoro.databinding.ActivityMainBinding;
import com.mundosonoro.databinding.FragmentJogo1Binding;
import com.mundosonoro.databinding.FragmentJogo1PlayBinding;
import com.mundosonoro.databinding.FragmentJogosBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;


public class Jogo1Play extends Fragment {
    private MediaPlayer mediaPlayer;
    private TextToSpeech textToSpeech;
    private FragmentJogo1PlayBinding binding;
    private List<Cenario> cenarios;
    private Cenario cenarioAtual;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJogo1PlayBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        inicializarCenarios();

        //inicializa TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR){
                textToSpeech.setLanguage(new Locale("pt", "BR"));
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


    private void inicializarCenarios(){
        cenarios = new ArrayList<>();

        cenarios.add(new Cenario("Cidade", new String[]{"Floresta", "Cidade", "Fazenda", "Aeroporto"}, R.raw.som_cidade));
        cenarios.add(new Cenario("Aeroporto", new String[]{"Floresta", "Cidade", "Aeroporto", "Rodoviária"},R.raw.som_aviao));
        cenarios.add(new Cenario("Escola", new String[]{"Supermercado", "Escola", "Hospital", "Biblioteca"}, R.raw.som_criancas));
        cenarios.add(new Cenario("Estádio", new String[]{"Teatro", "Parque", "Estádio", "Supermercado"},R.raw.som_estadio));
        cenarios.add(new Cenario("Fazenda", new String[]{"Supermercado", "Parque", "Fazenda", "Cidade"}, R.raw.som_fazenda));

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
        binding.opcao1.setText(opcoesEmbaralhadas.get(0));
        binding.opcao2.setText(opcoesEmbaralhadas.get(1));
        binding.opcao3.setText(opcoesEmbaralhadas.get(2));
        binding.opcao4.setText(opcoesEmbaralhadas.get(3));

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
            tocarFeedback(R.raw.correct_sfx);
            textToSpeech.speak("Mais 10 pontos!", TextToSpeech.QUEUE_FLUSH, null, null);
            Toast.makeText(getContext(), "Acertou", Toast.LENGTH_SHORT).show(); //tirar dps
        }else{
            tocarFeedback(R.raw.wrong_sfx);
            Toast.makeText(getContext(), "Errou", Toast.LENGTH_SHORT).show(); //tirar dps
        }

        sortearCenario();
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