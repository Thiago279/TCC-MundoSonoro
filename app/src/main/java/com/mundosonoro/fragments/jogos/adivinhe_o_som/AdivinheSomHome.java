package com.mundosonoro.fragments.jogos.adivinhe_o_som;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mundosonoro.R;
import com.mundosonoro.databinding.FragmentAdivinheSomHomeBinding;
import com.mundosonoro.databinding.FragmentAdivinheSomJogoBinding;

import java.util.Locale;

public class AdivinheSomHome extends Fragment {

    private FragmentAdivinheSomHomeBinding binding;
    private Button btn_play;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdivinheSomHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setAccessibilityPaneTitle(view, "Tela inicial do jogo Adivinhe o Som");

        binding.btnPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdivinheSomJogo adivinheSomJogo = new AdivinheSomJogo();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, adivinheSomJogo)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Botão para ouvir instruções (repetir o som)
        binding.btnRepetir.setOnClickListener(v -> {
            String instrucoes = "Você vai ouvir diferentes sons. " +
                    "Tente adivinhar de que som se trata escolhendo " +
                    "a resposta correta entre as opções disponíveis. " +
                    "Cada resposta certa vale pontos!";
            binding.getRoot().announceForAccessibility(instrucoes);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}