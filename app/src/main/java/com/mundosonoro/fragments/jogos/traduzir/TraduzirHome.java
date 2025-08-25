package com.mundosonoro.fragments.jogos.traduzir;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentTraduzirHomeBinding;
import java.util.Locale;

public class TraduzirHome extends Fragment {
    private TextToSpeech textToSpeech;
    private FragmentTraduzirHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTraduzirHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inicializa TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("pt", "BR"));

                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Fala a introdução do jogo
                    String introducao = "Bem-vindo ao jogo de tradução! " +
                            "Você vai ouvir uma palavra em português e deve escolher " +
                            "a tradução correta em inglês. " +
                            "Toque em começar quando estiver pronto!";
                    textToSpeech.speak(introducao, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

        // Botão para começar o jogo
        binding.btnComecar.setOnClickListener(v -> {
            // Para o TTS atual
            if (textToSpeech != null && textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }

            // Navega para o jogo principal
            ((MainActivity) getActivity()).navegarParaTraduzirJogo();
        });

        // Botão para ouvir instruções novamente
        binding.btnOuvirInstrucoes.setOnClickListener(v -> {
            String instrucoes = "Você vai ouvir palavras em português. " +
                    "Escolha a tradução correta em inglês entre as quatro opções. " +
                    "Cada resposta certa vale 10 pontos!";
            textToSpeech.speak(instrucoes, TextToSpeech.QUEUE_FLUSH, null, null);
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}