package com.mundosonoro.fragments.jogos.adivinhe_o_som;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mundosonoro.R;
import com.mundosonoro.databinding.FragmentAdivinheSomHomeBinding;
import com.mundosonoro.databinding.FragmentAdivinheSomJogoBinding;

public class AdivinheSomHome extends Fragment {

    private FragmentAdivinheSomHomeBinding binding;
    private Button btn_play;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdivinheSomHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        binding.btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implementar tutorial
                // Pode mostrar instruções ou navegar para uma tela de tutorial
            }
        });
    }
}