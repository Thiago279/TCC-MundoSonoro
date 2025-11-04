package com.mundosonoro.fragments.jogos.adivinhe_o_som;

import android.os.Bundle;
import android.os.Handler;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mundosonoro.models.Cenario;
import com.mundosonoro.R;
import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentAdivinheSomJogoBinding;
import com.mundosonoro.utils.PontuacaoManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class AdivinheSomJogo extends Fragment {
    private MediaPlayer mediaPlayer;
    private FragmentAdivinheSomJogoBinding binding;
    private List<Cenario> cenarios;
    private List<Cenario> cenariosDisponiveis; // cenarios nao usados
    private Cenario cenarioAtual;
    private int pontos = 0;
    private int rodadaAtual = 1;
    private final int MAX_RODADAS = 10;

    private PontuacaoManager pontuacaoManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdivinheSomJogoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        inicializarCenarios();

        // Inicializa a lista de cenários disponíveis com uma CÓPIA da lista original
        cenariosDisponiveis = new ArrayList<>(cenarios);

        sortearCenario();


        // Disparar som
        binding.btnOuvirSom.setOnClickListener(v -> tocarSom(cenarioAtual.somResId));

        // Clique dos botões
        binding.opcao1.setOnClickListener(v -> checarResposta(0));
        binding.opcao2.setOnClickListener(v -> checarResposta(1));
        binding.opcao3.setOnClickListener(v -> checarResposta(2));
        binding.opcao4.setOnClickListener(v -> checarResposta(3));

        return view;
    }

    // tipo 0 -> adivinhar cenario com base no som
    // tipo 1 -> adivinhar som com base no cenario
    private void inicializarCenarios() {
        cenarios = new ArrayList<>();

        cenarios.add(new Cenario("Cidade", new String[]{"Floresta", "Cidade", "Fazenda", "Aeroporto"}, R.raw.som_cidade, 0));
        cenarios.add(new Cenario("Aeroporto", new String[]{"Floresta", "Cidade", "Aeroporto", "Rodoviária"}, R.raw.som_aviao, 0));
        cenarios.add(new Cenario("Escola", new String[]{"Supermercado", "Escola", "Hospital", "Biblioteca"}, R.raw.som_criancas, 0));
        cenarios.add(new Cenario("Estádio", new String[]{"Teatro", "Parque", "Estádio", "Supermercado"}, R.raw.som_estadio, 0));
        cenarios.add(new Cenario("Fazenda", new String[]{"Supermercado", "Parque", "Fazenda", "Cidade"}, R.raw.som_fazenda, 0));
        cenarios.add(new Cenario("Leão Rugindo", new String[]{"Elefante trombeteando", "Leão Rugindo", "Lobo uivando", "Cavalo relinchando"}, R.raw.som_leao, 1));
        cenarios.add(new Cenario("Golfinho fazendo barulho", new String[]{"Golfinho fazendo barulho", "Ondas do mar", "Baleia cantando", "Barco navegando"}, R.raw.som_golfinho, 1));
        cenarios.add(new Cenario("Grilo cantando", new String[]{"Grilo cantando", "Galo cantando", "Passarinhos piando", "Coruja fazendo barulho"}, R.raw.som_grilo, 1));
        cenarios.add(new Cenario("Música do carrossel", new String[]{"Montanha russa descendo", "Música do carrossel", "Pipoca estourando", "Crianças no pula-pula"}, R.raw.som_carrossel, 1));
        cenarios.add(new Cenario("Gato miando", new String[]{"Cachorro latindo", "Gato miando", "Rato fazendo barulho", "Passarinhos piando"}, R.raw.cat_sfx, 1));
    }

    private void sortearCenario() {
        // Verifica se ainda há cenários disponíveis
        if (cenariosDisponiveis.isEmpty()) {
            // Se acabaram os cenários, recarrega a lista
            cenariosDisponiveis = new ArrayList<>(cenarios);
        }

        Random random = new Random();
        int index = random.nextInt(cenariosDisponiveis.size());
        cenarioAtual = cenariosDisponiveis.get(index);

        // REMOVE o cenário da lista de disponíveis
        cenariosDisponiveis.remove(index);

        // Cria uma copia da lista de opcoes e embaralha
        List<String> opcoesEmbaralhadas = new ArrayList<>();
        Collections.addAll(opcoesEmbaralhadas, cenarioAtual.opcoes);
        Collections.shuffle(opcoesEmbaralhadas);

        // Atualiza texto dos botões com opcoes embaralhadas
        binding.opcao1.setText(opcoesEmbaralhadas.get(0).toUpperCase());
        binding.opcao2.setText(opcoesEmbaralhadas.get(1).toUpperCase());
        binding.opcao3.setText(opcoesEmbaralhadas.get(2).toUpperCase());
        binding.opcao4.setText(opcoesEmbaralhadas.get(3).toUpperCase());

        // Atualiza informações da rodada
        binding.rodada.setText("Rodada: " + rodadaAtual + "/" + MAX_RODADAS);


        // Fala o número da rodada antes da pergunta
        binding.getRoot().announceForAccessibility("Rodada " + rodadaAtual);
    }

    private void tocarSom(int somResource) {
        pararSomAtual();

        mediaPlayer = MediaPlayer.create(getContext(), somResource);

        if (mediaPlayer != null) {
            mediaPlayer.start();

            // libera recursos qnd o som terminar
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        }
    }

    private void pararSomAtual() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace(); // debug
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void checarResposta(int indiceEscolhido) {
        String respostaEscolhida = "";

        // obtem o texto do botao clicado com base no indice
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

        // para o som do cenario imediatamente
        pararSomAtual();

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
                binding.getRoot().announceForAccessibility("Incorreto");
            }, 700);
        }

        rodadaAtual++;

        // Verifica se o jogo acabou
        if (rodadaAtual > MAX_RODADAS) {
            finalizarJogo();
        } else {
            // Próxima rodada após delay
            new Handler().postDelayed(() -> {
                sortearCenario();
            }, 3000);
        }
    }

    private void finalizarJogo() {
        // Salva o high score
        PontuacaoManager pontuacaoManager = new PontuacaoManager(getContext());
        pontuacaoManager.salvarHighScoreAdivinheSom(pontos);

        new Handler().postDelayed(() -> {
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
            new Handler().postDelayed(() -> {
                ((MainActivity) getActivity()).voltarParaMenu();
            }, 8000);

        }, 2000);
    }

    private void tocarFeedback(int resourceId) {
        pararSomAtual();
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
        pararSomAtual();
    }
}