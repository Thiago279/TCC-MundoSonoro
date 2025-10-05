package com.mundosonoro.fragments.menu;

import android.os.Bundle;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mundosonoro.R;
import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentTutorialApresentacaoBinding;

public class TutorialApresentacao extends Fragment {
    private MediaPlayer mediaPlayer;
    private FragmentTutorialApresentacaoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTutorialApresentacaoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Configurar clique dos botões
        binding.btnIniciarTutorial.setOnClickListener(v -> {
            // Navega para o tutorial principal
            ((MainActivity) getActivity()).navegarParaTutorial();
        });

        binding.btnVoltarMenu.setOnClickListener(v -> {
            // Navega de volta para o menu
            ((MainActivity) getActivity()).voltarParaMenu();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setAccessibilityPaneTitle(view, "Tela inicial do Tutorial");

        // Apresenta o tutorial após a view estar pronta
        new Handler().postDelayed(() -> {
            apresentarTutorial();
        }, 500);
    }

    private void apresentarTutorial() {
        String apresentacao = "Bem-vindo ao tutorial! " +
                "Aqui você vai aprender como usar o aplicativo. " +
                "Para navegar, deslize o dedo pela tela. " +
                "Para selecionar algo, toque duas vezes. " +
                "Vamos começar?";

        anunciar(apresentacao);
    }

    private void anunciar(String texto) {
        if (binding != null && binding.getRoot() != null) {
            binding.getRoot().announceForAccessibility(texto);
        }
    }

    private void tocarFeedback(int resourceId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(getContext(), resourceId);
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0.5f, 0.5f);
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