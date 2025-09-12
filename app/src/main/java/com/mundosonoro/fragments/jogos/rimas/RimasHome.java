package com.mundosonoro.fragments.jogos.rimas;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentRimasHomeBinding;
import java.util.Locale;

public class RimasHome extends Fragment {
    private TextToSpeech textToSpeech;
    private FragmentRimasHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRimasHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inicializa TextToSpeech
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("pt", "BR"));

                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Fala a introdução do jogo
                    String introducao = "Bem-vindo ao jogo de encontrar as rimas! " +
                            "Você terá quatro palavras na tela e deve encontrar " +
                            "qual par de palavras rima entre elas. " +
                            "As rimas são palavras que terminam com o mesmo som. " +
                            "Por exemplo, gato rima com rato! " +
                            "Selecione duas palavras que rimam entre si. " +
                            "Toque no botão de começar jogo quando estiver pronto!";
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
            ((MainActivity) getActivity()).navegarParaRimasJogo();
        });

        // Botão para ouvir instruções novamente
        binding.btnOuvirInstrucoes.setOnClickListener(v -> {
            String instrucoes = "Entre as quatro opções, você deve encontrar duas palavras que rimam. " +
                    "Clique duas vezes na primeira palavra para selecioná-la. " +
                    "Depois clique duas vezes na segunda palavra. " +
                    "Lembre-se: palavras que rimam terminam com o mesmo som! " +
                    "Como gato e rato, ou coração e paixão. " +
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