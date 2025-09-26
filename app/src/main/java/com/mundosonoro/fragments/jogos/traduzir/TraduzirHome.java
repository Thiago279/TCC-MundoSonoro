package com.mundosonoro.fragments.jogos.traduzir;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentTraduzirHomeBinding;
import java.util.Locale;

public class TraduzirHome extends Fragment {
    private FragmentTraduzirHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTraduzirHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Botão para começar o jogo
        binding.btnComecar.setOnClickListener(v -> {
            // Navega para o jogo principal
            ((MainActivity) getActivity()).navegarParaTraduzirJogo();
        });

        // Botão para ouvir instruções
        binding.btnOuvirInstrucoes.setOnClickListener(v -> {
            String instrucoes = "Você vai ouvir palavras em português. " +
                    "Escolha a tradução correta em inglês entre as quatro opções. " +
                    "Cada resposta certa vale 10 pontos!";

            binding.getRoot().announceForAccessibility(instrucoes);
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setAccessibilityPaneTitle(view, "Tela inicial do jogo Toque e Traduza");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}