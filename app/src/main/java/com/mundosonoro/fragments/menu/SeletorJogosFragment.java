package com.mundosonoro.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mundosonoro.databinding.FragmentSeletorJogosBinding;
import com.mundosonoro.fragments.jogos.adivinhe_o_som.AdivinheSomHome;
import com.mundosonoro.fragments.jogos.traduzir.TraduzirHome; // Adicionado import
import com.mundosonoro.R;
import com.mundosonoro.databinding.FragmentSeletorJogosBinding;

public class SeletorJogosFragment extends Fragment {
    private FragmentSeletorJogosBinding binding;
    private Button btn_jg1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSeletorJogosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Jogo 1 - Adivinhe o Som
        binding.btnJg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdivinheSomHome adivinheSomHome = new AdivinheSomHome();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, adivinheSomHome)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Jogo 2 - Traduzir (Adicionado)
        // Certifique-se de ter um botão com ID btnJg2 no layout FragmentJogosBinding
        if (binding.btnJg2 != null) { // Verificação de segurança
            binding.btnJg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TraduzirHome traduzirHome = new TraduzirHome();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, traduzirHome)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}