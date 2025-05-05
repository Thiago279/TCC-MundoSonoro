package com.mundosonoro;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;

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


public class Jogo1Play extends Fragment {
    private MediaPlayer mediaPlayer;
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
        sortearCenario();

        //repetir som
        binding.btnRepetir.setOnClickListener(v -> tocarSom(cenarioAtual.somResId));

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

    private void sortearCenario(){
        Random random = new Random();
        int index = random.nextInt(cenarios.size());
        cenarioAtual = cenarios.get(index);

        //Cria uma copia da lista de opcoes e embaralha
        List<String> opcoesEmbaralhadas = new ArrayList<>();
        Collections.addAll(opcoesEmbaralhadas, cenarioAtual.opcoes);
        Collections.shuffle(opcoesEmbaralhadas);

        // Atualiza texto dos botões com opcoes embaralhadas
        binding.opcao1.setText(cenarioAtual.opcoes[0]);
        binding.opcao2.setText(cenarioAtual.opcoes[1]);
        binding.opcao3.setText(cenarioAtual.opcoes[2]);
        binding.opcao4.setText(cenarioAtual.opcoes[3]);

        // toca o som
        tocarSom(cenarioAtual.somResId);
    }

    private void tocarSom(int somResource){
        if (mediaPlayer != null){
            mediaPlayer.release(); //libera qualquer recurso de audio anterior
        }

        mediaPlayer = MediaPlayer.create(getContext(), somResource);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release(); //libera recursos quando o som terminar
        });
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

        if (respostaEscolhida.equalsIgnoreCase(cenarioAtual.respostaCorreta)){
            Toast.makeText(getContext(), "Acertou", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Errou", Toast.LENGTH_SHORT).show();
        }

        sortearCenario();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release(); //libera o player ao sair do fragment
            mediaPlayer = null;
        }
    }

}