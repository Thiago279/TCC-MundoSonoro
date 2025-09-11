package com.mundosonoro.fragments.menu;

import android.os.Bundle;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

import com.mundosonoro.R;
import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentTutorialApresentacaoBinding;

public class TutorialApresentacao extends Fragment {
    private MediaPlayer mediaPlayer;
    private TextToSpeech textToSpeech;
    private FragmentTutorialApresentacaoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTutorialApresentacaoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inicializa TTS
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(new Locale("pt", "BR"));

                // Apresenta o tutorial quando o TTS estiver pronto
                new Handler().postDelayed(() -> {
                    apresentarTutorial();
                }, 500);
            }
        });

        // Configurar clique dos botões
        binding.btnIniciarTutorial.setOnClickListener(v -> {
            // Para o TTS atual
            if (textToSpeech != null && textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }

            // Navega para o jogo principal
            ((MainActivity) getActivity()).navegarParaTutorial();
        });

        binding.btnVoltarMenu.setOnClickListener(v -> {
            // Para o TTS atual
            if (textToSpeech != null && textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }

            // Navega para o jogo principal
            ((MainActivity) getActivity()).voltarParaMenu();
        });

        return view;
    }

    private void apresentarTutorial() {
        String apresentacao = "Bem-vindo ao tutorial! " +
                "Aqui você vai aprender como usar o aplicativo. " +
                "Para navegar, deslize o dedo pela tela. " +
                "Para selecionar algo, toque duas vezes. " +
                "Vamos começar?";

        falarTexto(apresentacao);
    }

    private void falarTexto(String texto) {
        if (textToSpeech != null) {
            textToSpeech.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void irParaTutorialInterativo() {
        ((MainActivity) getActivity()).navegarParaTutorial();
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
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}