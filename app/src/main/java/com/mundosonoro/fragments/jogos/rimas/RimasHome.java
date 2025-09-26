package com.mundosonoro.fragments.jogos.rimas;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentRimasHomeBinding;
import java.util.Locale;

public class RimasHome extends Fragment {

    private FragmentRimasHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRimasHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Botão para começar o jogo
        binding.btnComecar.setOnClickListener(v -> {
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
            binding.getRoot().announceForAccessibility(instrucoes);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setAccessibilityPaneTitle(view, "Tela inicial do jogo Encontre as Rimas");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}