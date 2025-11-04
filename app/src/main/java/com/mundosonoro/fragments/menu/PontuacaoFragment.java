package com.mundosonoro.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.mundosonoro.R;
import com.mundosonoro.activities.MainActivity;
import com.mundosonoro.databinding.FragmentPontuacaoBinding;
import com.mundosonoro.utils.PontuacaoManager;

public class PontuacaoFragment extends Fragment {
    private FragmentPontuacaoBinding binding;
    private PontuacaoManager pontuacaoManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPontuacaoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inicializa o gerenciador de pontuações
        pontuacaoManager = new PontuacaoManager(getContext());

        // Carrega e exibe os recordes
        carregarRecordes();

        // Botão voltar
        binding.btnVoltarMenu.setOnClickListener(v -> {
            ((MainActivity) getActivity()).voltarParaMenu();
        });

        return view;
    }

    private void carregarRecordes() {
        // Carrega os recordes salvos
        int recordeAdivinhe = pontuacaoManager.getHighScoreAdivinheSom();
        int recordeRimas = pontuacaoManager.getHighScoreRimas();
        int recordeTraduzir = pontuacaoManager.getHighScoreTraduzir();

        // Atualiza os TextViews
        binding.pontuacaoAdivinhe.setText("Recorde: " + recordeAdivinhe + " pontos");
        binding.pontuacaoRimas.setText("Recorde: " + recordeRimas + " pontos");
        binding.pontuacaoTraduzir.setText("Recorde: " + recordeTraduzir + " pontos");

        // Anuncia para acessibilidade
        String anuncio = "Recordes. Adivinhe o Som: " + recordeAdivinhe + " pontos. " +
                "Rimas: " + recordeRimas + " pontos. " +
                "Traduzir: " + recordeTraduzir + " pontos.";
        binding.getRoot().announceForAccessibility(anuncio);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}